package ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.util.UUID

data class QueueRuleEntity(
    val id: UUID? = null,
    val type: RuleType,
    val enabled: Boolean = true,
    val payload: JsonElement,
    val queuePlanId: UUID,
) {
    companion object {
        val mocks = generateMocks(3)

        fun generateMocks(count: Int): List<QueueRuleEntity> {
            return (1..count).map {
                QueueRuleEntity(
                    id = UUID.randomUUID(),
                    type = RuleType.entries.toTypedArray().random(),
                    enabled = listOf(true, false).random(),
                    payload = Json.encodeToJsonElement(mapOf("key" to "value$it")),
                    queuePlanId = UUID.randomUUID()
                )
            }
        }
    }
}
