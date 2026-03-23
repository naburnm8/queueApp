package ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestsMapper
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.repository.SubmissionRequestsRepository

class SubmissionRequestsViewmodel (
    private val submissionRequestsRepository: SubmissionRequestsRepository
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

    suspend fun loadMyRequestsInner(onSuccess: () -> Unit = {}) {
        runCatching {
            val result = submissionRequestsRepository.getAllMySubmissionRequests().getOrThrow()
            _stateFlow.value = SubmissionRequestsState.Main(result.map { SubmissionRequestsMapper.map(it) })
            onSuccess()
        }.onFailure {
            _stateFlow.value = SubmissionRequestsState.Error(it.message ?: "Unknown error")
        }
    }
}