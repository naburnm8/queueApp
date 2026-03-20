package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel

import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity

sealed class QueuePlansState {
    data object Loading : QueuePlansState()
    data class Error(val errorMessage: String): QueuePlansState()
    data class Main(
        val plansOfDisciplines: Map<DisciplineEntity, List<QueuePlanEntity>>,
        val activePlan: QueuePlanEntity? = null,
        val creatingForBundle: CreatingForBundle? = null
    ) : QueuePlansState()
}