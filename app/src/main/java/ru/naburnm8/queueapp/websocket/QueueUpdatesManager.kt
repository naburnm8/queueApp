package ru.naburnm8.queueapp.websocket

import android.util.Log
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.naburnm8.queueapp.authorization.token.TokenStorage
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.Volatile

class QueueUpdatesManager (
    api: String,
    private val tokenStorage: TokenStorage,
) {

    companion object {
        private const val NAME = "QueueUpdatesManager"
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val stompClient: StompClient =
        Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://$api/ws"
        )

    private val trackedQueueRefs = ConcurrentHashMap<UUID, Int>()
    private val subscriptions = ConcurrentHashMap<UUID, Disposable>()

    private var lifecycleDisposable: Disposable? = null

    private val _updates = MutableSharedFlow<QueueUpdateEvent> (
        replay = 0,
        extraBufferCapacity = 64
    )

    val updates = _updates.asSharedFlow()

    private val _sessionExpired = MutableSharedFlow<Unit> (
        replay = 0,
        extraBufferCapacity = 1
    )

    val sessionExpired = _sessionExpired.asSharedFlow()

    private val _untrackFlow = MutableSharedFlow<UUID>()
    val untrackFlow = _untrackFlow.asSharedFlow()

    private val _trackFlow = MutableSharedFlow<UUID>()
    val trackFlow = _trackFlow.asSharedFlow()

    @Volatile
    private var isConnecting = false

    @Volatile
    private var manuallyDisconnected = false

    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 8

    fun connect() {
        if (stompClient.isConnected || isConnecting) return

        val accessToken = runBlocking { tokenStorage.getAccessToken() }
        if (accessToken.isNullOrBlank()) {
            scope.launch { _sessionExpired.emit(Unit) }
            return
        }

        manuallyDisconnected = false
        isConnecting = true

        lifecycleDisposable?.dispose()
        lifecycleDisposable = stompClient.lifecycle().subscribe(
            { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        isConnecting = false
                        reconnectAttempts = 0
                        clearAllSubscriptions()
                        resubscribeAll()
                        Log.d(NAME, "WebSocket connection opened")
                    }

                    LifecycleEvent.Type.ERROR -> {
                        isConnecting = false
                        Log.d(NAME, "WebSocket error: ${event.exception.message}")
                        if (!manuallyDisconnected) {
                            scheduleReconnect()
                        }
                    }

                    LifecycleEvent.Type.CLOSED -> {
                        isConnecting = false
                        Log.d(NAME, "WebSocket closed")
                        if (!manuallyDisconnected) {
                            scheduleReconnect()
                        }
                    }

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        isConnecting = false
                        Log.d(NAME, "Failed server heartbeat")
                        if (!manuallyDisconnected) {
                            scheduleReconnect()
                        }
                    }
                }
            },
            {
                isConnecting = false
                if (!manuallyDisconnected) {
                    scheduleReconnect()
                }
            }
        )

        val headers = mutableListOf<StompHeader>()
        headers += StompHeader("Authorization", "Bearer $accessToken")

        try {
            stompClient.connect(headers)
        } catch (e: Exception) {
            isConnecting = false
            scheduleReconnect()
        }

        Log.d(NAME, "Connecting to WebSocket with access token: $accessToken")
    }

    fun disconnect() {
        manuallyDisconnected = true
        isConnecting = false
        reconnectAttempts = 0

        clearAllSubscriptions()

        lifecycleDisposable?.dispose()
        lifecycleDisposable = null

        Log.d(NAME, "Disconnecting from WebSocket")

        if (stompClient.isConnected) {
            stompClient.disconnect()
        }
    }

    fun trackQueue(queuePlanId: UUID, notify: Boolean = false) {
        Log.d(NAME, "Tracking queue $queuePlanId")
        val newCount = (trackedQueueRefs[queuePlanId] ?: 0) + 1
        trackedQueueRefs[queuePlanId] = newCount

        if (newCount == 1 && stompClient.isConnected) {
            subscribe(queuePlanId)
            if (notify) {
                scope.launch {
                    _trackFlow.emit(queuePlanId)
                }
            }
        }
    }

    fun trackQueues(queuePlanIds: List<UUID>) {
        Log.d(NAME, "Tracking queues: $queuePlanIds")
        queuePlanIds.forEach { trackQueue(it) }
    }

    fun untrackQueue(queuePlanId: UUID, notify: Boolean = false) {
        val current = trackedQueueRefs[queuePlanId] ?: return

        if (current <= 1) {
            trackedQueueRefs.remove(queuePlanId)
            subscriptions.remove(queuePlanId)?.dispose()
        } else {
            trackedQueueRefs[queuePlanId] = current - 1
        }

        if (notify) {
            scope.launch {
                Log.d(NAME, "Notifying of queue untracking with id $queuePlanId")
                _untrackFlow.emit(queuePlanId)
            }
        }
    }
    fun untrackAll() {
        Log.d(NAME, "Untracking all queues")
        trackedQueueRefs.keys.forEach { queueId ->
            untrackQueue(queueId)
        }
    }

    private fun resubscribeAll() {
        trackedQueueRefs.keys.forEach { queueId ->
            subscribe(queueId)
        }
    }

    private fun subscribe(queuePlanId: UUID) {
        if (subscriptions.containsKey(queuePlanId)) return

        val topic = "/topic/queue/$queuePlanId"

        val disposable = stompClient.topic(topic).subscribe(
            { message ->
                try {
                    val version = message.payload.toLong()

                    Log.d(NAME, "Received update for queue $queuePlanId with version $version")

                    scope.launch {
                        _updates.emit(
                            QueueUpdateEvent(
                                queueId = queuePlanId,
                                version = version
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()

                if (!manuallyDisconnected) {
                    scheduleReconnect()
                }
            }
        )

        subscriptions[queuePlanId] = disposable
    }

    private fun scheduleReconnect() {
        if (manuallyDisconnected) return
        if (isConnecting) return

        if (reconnectAttempts >= maxReconnectAttempts) {
            scope.launch {
                _sessionExpired.emit(Unit)
            }
            return
        }

        Log.d(NAME, "Scheduling reconnect attempt #$reconnectAttempts")

        reconnectAttempts++

        val delayMillis = calculateBackoffMillis(reconnectAttempts)

        scope.launch {
            delay(delayMillis)

            val accessToken = tokenStorage.getAccessToken()
            if (accessToken.isNullOrBlank()) {
                _sessionExpired.emit(Unit)
                return@launch
            }
            // если REST-слой уже успел refresh-нуть токен, мы возьмём новый токен отсюда
            connect()
        }
    }

    private fun calculateBackoffMillis(attempt: Int): Long {
        // 1s, 2s, 4s, 8s... максимум 30s
        val base = 1000L * (1 shl (attempt - 1))
        return base.coerceAtMost(30_000L)
    }

    private fun clearAllSubscriptions() {
        subscriptions.values.forEach { it.dispose() }
        subscriptions.clear()
    }


}