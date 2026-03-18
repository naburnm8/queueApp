package ru.naburnm8.queueapp.queueOperator.metrics.viewmodel

import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.metrics.entity.StudentMetricEntity

sealed class StudentMetricsState {
    data object Loading : StudentMetricsState()

    data class Error(val message: String) : StudentMetricsState()

    data class Main(
        val metricToDiscipline: Map<DisciplineEntity, List<StudentMetricEntity>>,
        val activeMetric: StudentMetricEntity? = null,
        val creatingFor: CreatingForBundle? = null
    ) : StudentMetricsState()
}