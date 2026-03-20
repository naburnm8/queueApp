package ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity

import java.util.UUID

data class QueueRuleEntity(
    val id: UUID? = null,
    val type: RuleType,
    val enabled: Boolean = true,
    val payload: String,
    val queuePlanId: UUID,
)
