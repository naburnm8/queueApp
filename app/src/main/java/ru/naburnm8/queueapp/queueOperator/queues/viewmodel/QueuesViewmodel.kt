package ru.naburnm8.queueapp.queueOperator.queues.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestsMapper
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.repository.SubmissionRequestsRepository
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueMapper
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlansMapper
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.repository.QueuePlansRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.queueOperator.queues.repository.QueuesRepository
import ru.naburnm8.queueapp.websocket.QueueUpdatesManager
import java.util.UUID

class QueuesViewmodel (
    private val queuePlansRepository: QueuePlansRepository,
    private val queuesRepository: QueuesRepository,
    private val submissionRequestsRepository: SubmissionRequestsRepository,
    private val queueUpdatesManager: QueueUpdatesManager,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<QueuesState>(QueuesState.Loading)

    val stateFlow = _stateFlow.asStateFlow()

    private val stateMutex = Mutex()

    init {
        loadQueues()

        viewModelScope.launch {
            queueUpdatesManager.updates.collect { event ->
                reloadOneQueue(event.queueId)
            }
        }
    }

    fun loadQueues(onSuccess: () -> Unit = {}) {
        _stateFlow.value = QueuesState.Loading
        viewModelScope.launch {
            loadQueuesInner(onSuccess)
        }
    }

    fun reloadOneQueue(queueId: UUID, onSuccess: () -> Unit = {}) {
        val currentState = _stateFlow.value
        if (currentState !is QueuesState.Main) {
            return
        }
        viewModelScope.launch {
            runCatching {
                val newQueue = queuesRepository.view(queueId).getOrThrow()
                val mapped = QueueMapper.map(newQueue)

                val newRequests = submissionRequestsRepository
                    .getAllSubmissionRequests(queueId)
                    .getOrThrow()
                    .map { SubmissionRequestsMapper.map(it) }

                stateMutex.withLock {
                    val currentState = _stateFlow.value
                    if (currentState !is QueuesState.Main) {
                        return@withLock
                    }

                    val newList = currentState.queues.toMutableList()
                    newList.removeIf { it.queuePlanId == queueId }
                    newList.add(mapped)

                    val newMap = currentState.submissionRequestsToQueues.toMutableMap()
                    newMap.keys.removeIf { it.queuePlanId == queueId }
                    newMap[mapped] = newRequests

                    _stateFlow.value = QueuesState.Main(newList, newMap)
                }
                onSuccess()
            }.onFailure {
                _stateFlow.value = QueuesState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun toggleStatus(queueId: UUID, forceReload: Boolean = false, onSuccess: () -> Unit = {}) {
        val currentState = _stateFlow.value
        if (currentState !is QueuesState.Main) {
            return
        }
        _stateFlow.value = QueuesState.Loading
        viewModelScope.launch {
            runCatching {
                val queue = currentState.queues.find {it.queuePlanId == queueId}
                if (queue == null) {
                    throw IllegalStateException("No such queue")
                }
                if (queue.queueStatus == QueueStatus.ACTIVE) {
                    queuePlansRepository.close(queueId).getOrThrow()
                } else if (queue.queueStatus == QueueStatus.DRAFT) {
                    queuePlansRepository.activate(queueId).getOrThrow()
                }
                if (forceReload) {
                    loadQueuesInner(onSuccess)
                }
            }.onFailure {
                _stateFlow.value = QueuesState.Error(it.message ?: "Unknown error")
            }
        }
    }

    private suspend fun loadQueuesInner(onSuccess: () -> Unit = {}) {
        runCatching {
            queueUpdatesManager.untrackAll()
            val myQueuePlanIds = queuePlansRepository
                .getMyQueuePlans()
                .getOrThrow()
                .map {it.id!!}

            val queues = mutableListOf<QueueSnapshotEntity>()
            val requestsToQueues: MutableMap<QueueSnapshotEntity, List<SubmissionRequestEntity>> = mutableMapOf()
            for (id in myQueuePlanIds) {
                val queueSnapshot = queuesRepository.view(id).getOrThrow()
                val mapped = QueueMapper.map(queueSnapshot)
                queues.add(mapped)
                val requests = submissionRequestsRepository
                    .getAllSubmissionRequests(id)
                    .getOrThrow()
                    .filter { it.status != SubmissionStatus.ENQUEUED }

                requestsToQueues[mapped] = requests.map { SubmissionRequestsMapper.map(it) }
            }
            _stateFlow.value = QueuesState.Main(queues, requestsToQueues)
            queueUpdatesManager.trackQueues(myQueuePlanIds)
            onSuccess()
        }.onFailure {
            _stateFlow.value = QueuesState.Error(it.message ?: "Unknown error")
            Log.e("QueuesViewmodel", "Failed to load queues", it)
        }
    }

}