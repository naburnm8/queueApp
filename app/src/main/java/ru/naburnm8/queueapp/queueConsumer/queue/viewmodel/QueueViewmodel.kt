package ru.naburnm8.queueapp.queueConsumer.queue.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortMapper
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.repository.QueuePlansShortRepository
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestsMapper
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.repository.SubmissionRequestsRepository
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueMapper
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleMapper
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.repository.QueueRulesRepository
import ru.naburnm8.queueapp.queueOperator.queues.repository.QueuesRepository
import ru.naburnm8.queueapp.websocket.QueueUpdatesManager
import java.util.UUID

class QueueViewmodel (
    private val queuePlansRepository: QueuePlansShortRepository,
    private val queuesRepository: QueuesRepository,
    private val submissionRequestsRepository: SubmissionRequestsRepository,
    private val queueUpdatesManager: QueueUpdatesManager,
    private val queueRulesRepository: QueueRulesRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<QueueState>(QueueState.Loading)

    val stateFlow = _stateFlow.asStateFlow()

    private val stateMutex = Mutex()

    private var loadingRules = false

    init {
        loadQueues()

        viewModelScope.launch {
            queueUpdatesManager.updates.collect { event ->
                reloadOneQueue(event.queueId)
            }
        }
    }

    fun loadQueueRules(queueId: UUID, onSuccess: () -> Unit) {
        val currentState = _stateFlow.value
        if (currentState !is QueueState.Main) return
        viewModelScope.launch {
            runCatching {
                if (loadingRules) return@launch
                loadingRules = true
                val rules = queueRulesRepository
                    .getRules(queueId)
                    .getOrThrow()
                    .map{ QueueRuleMapper.map(it) }

                if (currentState.currentQueueRules != null && currentState.currentQueueRules.find {it.queuePlanId == queueId} != null) {
                    onSuccess()
                    return@runCatching
                }

                _stateFlow.value = QueueState.Main(
                    currentState.queues,
                    currentState.queuePlans,
                    currentState.myRequestByQueue,
                    rules
                )
                loadingRules = false
                onSuccess()
            }.onFailure {
                Log.e("QueueViewmodel", "Failed to load queue rules", it)
                _stateFlow.value = QueueState.Main(
                    currentState.queues,
                    currentState.queuePlans,
                    currentState.myRequestByQueue,
                    null
                )
                loadingRules = false
                onSuccess()
            }
        }
    }


    fun leaveQueue(queueId: UUID, onSuccess: () -> Unit = {}) {
        val currentState = _stateFlow.value
        if (currentState !is QueueState.Main) return
        viewModelScope.launch {
            runCatching {
                val plan = currentState.queuePlans.find {it.id == queueId} ?: throw IllegalStateException("Queue plan not found")
                queueUpdatesManager.untrackQueue(queueId)
                submissionRequestsRepository.leaveQueue(plan.id).getOrThrow()
                loadQueuesInner(onSuccess)
            }.onFailure {
                _stateFlow.value = QueueState.Error(it.message ?: "Unknown error")
            }
        }
    }


    fun reloadOneQueue(queueId: UUID, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            runCatching {
                val newQueue = queuesRepository.view(queueId).getOrThrow()
                val mapped = QueueMapper.map(newQueue)

                val newQueuePlan = queuePlansRepository
                    .getShortQueuePlan(queueId)
                    .getOrThrow()

                val newRequest = submissionRequestsRepository.getMySubmissionRequest(queueId)
                    .getOrThrow()

                stateMutex.withLock {
                    val currentState = _stateFlow.value
                    if (currentState !is QueueState.Main) return@withLock

                    val newQueues = currentState.queues.toMutableList()
                    newQueues.removeIf { it.queuePlanId == queueId }
                    newQueues.add(mapped)

                    val newQueuePlans = currentState.queuePlans.toMutableList()
                    newQueuePlans.removeIf { it.id == queueId }
                    newQueuePlans.add(QueuePlanShortMapper.map(newQueuePlan))

                    val newRequests = currentState.myRequestByQueue.toMutableMap()
                    newRequests.keys.removeIf { it.queuePlanId == queueId }
                    newRequests[QueueMapper.map(newQueue)] = SubmissionRequestsMapper.map(newRequest)

                    _stateFlow.value = QueueState.Main(
                        queues = newQueues,
                        queuePlans = newQueuePlans,
                        myRequestByQueue = newRequests
                    )
                    onSuccess()
                }

            }.onFailure {
                _stateFlow.value = QueueState.Error(it.message ?: "Unknown error")
            }
        }
    }


    fun loadQueues(onSuccess: () -> Unit = {}) {
        _stateFlow.value = QueueState.Loading
        viewModelScope.launch {
            loadQueuesInner(onSuccess)
        }
    }


    private suspend fun loadQueuesInner(onSuccess: () -> Unit = {}) {
        runCatching {
            queueUpdatesManager.untrackAll()

            val myQueuePlansIds = submissionRequestsRepository
                .getAllMySubmissionRequests()
                .getOrThrow()
                .map {it.queuePlanId}

            val queuePlans = queuePlansRepository.getAllQueuePlans()
                .getOrThrow()
                .filter { it.id in myQueuePlansIds }
                .map { QueuePlanShortMapper.map(it) }

            val queues = mutableListOf<QueueSnapshotEntity>()
            val queuesToRequests = mutableMapOf<QueueSnapshotEntity, SubmissionRequestEntity>()
            for (id in myQueuePlansIds) {
                val queue = queuesRepository.view(id)
                    .getOrThrow()
                queues.add(QueueMapper.map(queue))

                val request = submissionRequestsRepository.getMySubmissionRequest(id)
                    .getOrThrow()
                queuesToRequests[QueueMapper.map(queue)] = SubmissionRequestsMapper.map(request)
            }
            _stateFlow.value = QueueState.Main(
                queues = queues,
                queuePlans = queuePlans,
                myRequestByQueue = queuesToRequests
            )
            queueUpdatesManager.trackQueues(myQueuePlansIds)
            onSuccess()

        }.onFailure {
            _stateFlow.value = QueueState.Error(it.message ?: "Unknown error")
        }
    }

}