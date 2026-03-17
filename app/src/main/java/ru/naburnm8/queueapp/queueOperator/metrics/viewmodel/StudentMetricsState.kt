package ru.naburnm8.queueapp.queueOperator.metrics.viewmodel

import ru.naburnm8.queueapp.queueOperator.metrics.entity.StudentMetricEntity
import java.util.UUID

sealed class StudentMetricsState {
    data object Loading : StudentMetricsState()

    data class Error(val message: String) : StudentMetricsState()

    data class Main(
        val metricToDiscipline: Map<UUID, List<StudentMetricEntity>>
    )
}