package ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import java.util.UUID

sealed class QueueRulesState {
    data object Loading : QueueRulesState()
    data class Error(val errorMessage: String) : QueueRulesState()
    open class Main (
        val queuePlanId: UUID,
        val queueRules: List<QueueRuleEntity>
    ) : QueueRulesState() {
        class GroupBonus (
            queuePlanId: UUID,
            queueRules: List<QueueRuleEntity>,
            val distinctGroups: List<String>,
        ) : Main(queuePlanId, queueRules)

        class IdentifierBonus (
            queuePlanId: UUID,
            queueRules: List<QueueRuleEntity>,
            val studentsToGroups: Map<String, List<ProfileEntity>>
        ) : Main(queuePlanId, queueRules)

        class OtherType (
            queuePlanId: UUID,
            queueRules: List<QueueRuleEntity>,
        ) : Main(queuePlanId, queueRules)
    }
}