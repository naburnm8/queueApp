package ru.naburnm8.queueapp.queueOperator.metrics.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.naburnm8.queueapp.queueOperator.metrics.repository.StudentMetricsRepository

class StudentMetricsViewmodel(
    private val repository: StudentMetricsRepository
) : ViewModel() {
    private var _stateFlow = MutableStateFlow<StudentMetricsState>(StudentMetricsState.Loading)
    val stateFlow = _stateFlow.asStateFlow()
}