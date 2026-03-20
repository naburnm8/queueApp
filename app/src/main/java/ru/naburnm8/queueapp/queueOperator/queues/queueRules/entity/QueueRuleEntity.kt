package ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity

import kotlinx.serialization.json.JsonElement
import java.util.UUID

data class QueueRuleEntity(
    val id: UUID? = null,
    val type: RuleType,
    val enabled: Boolean = true,
    val payload: JsonElement,
    val queuePlanId: UUID,
)
