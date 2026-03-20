package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity

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
}