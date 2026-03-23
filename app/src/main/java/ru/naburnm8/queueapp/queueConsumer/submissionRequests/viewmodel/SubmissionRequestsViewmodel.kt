package ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortMapper
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.repository.QueuePlansShortRepository
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestsMapper
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.repository.SubmissionRequestsRepository
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplinesMapper
import ru.naburnm8.queueapp.queueOperator.discipline.repository.StudentDisciplineRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus

class SubmissionRequestsViewmodel (
    private val submissionRequestsRepository: SubmissionRequestsRepository,
    private val queuePlansShortRepository: QueuePlansShortRepository,
    private val profileRepository: ProfileRepository,
    private val disciplinesRepository: StudentDisciplineRepository
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<SubmissionRequestsState>(SubmissionRequestsState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        loadMyRequests()
    }

    fun loadMyRequests(onSuccess: () -> Unit = {}) {
        _stateFlow.value = SubmissionRequestsState.Loading
        viewModelScope.launch {
            loadMyRequestsInner(onSuccess)
        }
    }

    fun deleteRequest(req: SubmissionRequestEntity, onSuccess: () -> Unit = {}) {
        _stateFlow.value = SubmissionRequestsState.Loading
        viewModelScope.launch {
            runCatching {
                submissionRequestsRepository.deleteMySubmissionRequest(req.queuePlanId).getOrThrow()
                loadMyRequestsInner(onSuccess)
            }.onFailure {
                _stateFlow.value = SubmissionRequestsState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun switchInputContext(queuePlan: QueuePlanShortEntity, onSuccess: () -> Unit = {}) {
        val currentState = _stateFlow.value
        if (currentState !is SubmissionRequestsState.Main) {
            return
        }
        _stateFlow.value = SubmissionRequestsState.Loading
        viewModelScope.launch {
            switchInputContextInner(currentState, queuePlan, onSuccess)
        }
    }

    private suspend fun switchInputContextInner(
        currentState: SubmissionRequestsState.Main,
        queuePlan: QueuePlanShortEntity,
        onSuccess: () -> Unit = {}
    ) {
        runCatching {
            val me = ProfileMapper.map(profileRepository.getMeStudent().getOrThrow())
            val workTypes = disciplinesRepository
                .getWorkTypesById(queuePlan.discipline.id)
                .getOrThrow()
                .workTypes
                .map { DisciplinesMapper.map(it) }
            _stateFlow.value = SubmissionRequestsState.Main(
                requests = currentState.requests,
                queuePlans = currentState.queuePlans,
                activeRequest = currentState.activeRequest,
                inputBundle = SubmissionRequestInputBundle(
                    queuePlan = queuePlan,
                    workTypes = workTypes,
                    me = me
                )
            )
            onSuccess()
        }.onFailure {
                _stateFlow.value = SubmissionRequestsState.Error(it.message ?: "Unknown error")
        }
    }

    fun createSubmissionRequest(req: SubmissionRequestEntity, code: String, onSuccess: () -> Unit = {}) {
        val currentState = _stateFlow.value
        if (currentState !is SubmissionRequestsState.Main || currentState.inputBundle == null) {
            return
        }
        _stateFlow.value = SubmissionRequestsState.Loading
        viewModelScope.launch {
            runCatching {
                submissionRequestsRepository.createMySubmissionRequest(
                    currentState.inputBundle.queuePlan.id,
                    SubmissionRequestsMapper.toRequest(req, code)
                ).getOrThrow()
                loadMyRequestsInner(onSuccess)
            }.onFailure {
                _stateFlow.value = SubmissionRequestsState.Error(it.message ?: "Unknown error")
            }
        }
    }

    suspend fun loadMyRequestsInner(onSuccess: () -> Unit = {}) {
        runCatching {
            val requests = submissionRequestsRepository.getAllMySubmissionRequests().getOrThrow()
            val queuePlans = queuePlansShortRepository
                .getAllQueuePlans()
                .getOrThrow()
                .filter {
                    it.status == QueueStatus.DRAFT || it.status == QueueStatus.ACTIVE
                }

            _stateFlow.value = SubmissionRequestsState.Main(
                requests.map { SubmissionRequestsMapper.map(it) },
                queuePlans.map { QueuePlanShortMapper.map(it) }
            )
            onSuccess()
        }.onFailure {
            _stateFlow.value = SubmissionRequestsState.Error(it.message ?: "Unknown error")
        }
    }
}