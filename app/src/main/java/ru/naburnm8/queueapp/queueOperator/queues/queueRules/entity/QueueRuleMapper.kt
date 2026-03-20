package ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity

import ru.naburnm8.queueapp.queueOperator.queues.queueRules.request.QueueRuleRequest
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.response.QueueRuleResponse

object QueueRuleMapper {

    fun map(response: QueueRuleResponse): QueueRuleEntity {
        return QueueRuleEntity(
            id = response.id,
            type = response.type,
            enabled = response.enabled,
            payload = response.payload,
            queuePlanId = response.queuePlanId
        )
    }

    fun toRequest(entity: QueueRuleEntity): QueueRuleRequest {
        return QueueRuleRequest(
            id = entity.id,
            type = entity.type,
            enabled = entity.enabled,
            payload = entity.payload,
        )
    }
}