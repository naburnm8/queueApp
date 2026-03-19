package ru.naburnm8.queueapp.queueOperator.queues.queueRules.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class QueueRuleRequest(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val type: RuleType,
    val enabled: Boolean = true,
    val payload: String
)
