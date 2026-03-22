package ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import java.util.UUID

sealed class QueueRulesState {
    data object Loading : QueueRulesState()
    data class Error(val errorMessage: String) : QueueRulesState()
    open class Main (
        val queuePlan: QueuePlanEntity,
        val queueRules: List<QueueRuleEntity>,
        val activeRule: QueueRuleEntity? = null,
    ) : QueueRulesState() {
        class GroupBonus (
            queuePlan: QueuePlanEntity,
            queueRules: List<QueueRuleEntity>,
            activeRule: QueueRuleEntity? = null,
            val distinctGroups: List<String>,
        ) : Main(queuePlan, queueRules, activeRule)

        class IdentifierBonus (
            queuePlan: QueuePlanEntity,
            queueRules: List<QueueRuleEntity>,
            activeRule: QueueRuleEntity? = null,
            val studentsToGroups: Map<String, List<ProfileEntity>>
        ) : Main(queuePlan, queueRules, activeRule)

        class OtherType (
            queuePlan: QueuePlanEntity,
            queueRules: List<QueueRuleEntity>,
            activeRule: QueueRuleEntity? = null,
        ) : Main(queuePlan, queueRules, activeRule)
    }
}