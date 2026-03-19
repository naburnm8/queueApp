package ru.naburnm8.queueapp.queueOperator.queues.queueRules.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class QueueRuleResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val type: RuleType,
    val enabled: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val queuePlanId: UUID,
    val payload: String
)
