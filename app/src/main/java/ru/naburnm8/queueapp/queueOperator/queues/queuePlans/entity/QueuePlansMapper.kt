package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity

import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueuePlanRequest
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.response.QueuePlanResponse

object QueuePlansMapper {
    fun map (response: QueuePlanResponse): QueuePlanEntity {
        return QueuePlanEntity(
            id = response.id,
            createdByTeacherId = response.createdByTeacherId,
            title = response.title,
            status = response.status,
            useDebts = response.useDebts,
            wDebts = response.wDebts,
            useTime = response.useTime,
            wTime = response.wTime,
            useAchievements = response.useAchievements,
            wAchievements = response.wAchievements,
            createdAt = response.createdAt,
            slotDurationMinutes = response.slotDurationMinutes
        )
    }

    fun toRequest(entity: QueuePlanEntity): QueuePlanRequest {
        return QueuePlanRequest(
            id = entity.id,
            title = entity.title,
            useDebts = entity.useDebts,
            wDebts = entity.wDebts,
            useTime = entity.useTime,
            wTime = entity.wTime,
            useAchievements = entity.useAchievements,
            wAchievements = entity.wAchievements,
            slotDurationMinutes = entity.slotDurationMinutes,
            status = entity.status
        )
    }
}