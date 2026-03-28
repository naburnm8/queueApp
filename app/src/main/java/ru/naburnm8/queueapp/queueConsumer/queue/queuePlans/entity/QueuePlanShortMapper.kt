package ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity

import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.response.QueuePlanShortResponse
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplinesMapper

object QueuePlanShortMapper {
    fun map(response: QueuePlanShortResponse) : QueuePlanShortEntity {
        return QueuePlanShortEntity (
            id = response.id,
            title = response.title,
            discipline = DisciplinesMapper.map(response.discipline),
            status = response.status,
            teacher = ProfileMapper.map(response.teacher),
            slotDurationMinutes = response.slotDurationMinutes,
            useTime = response.useTime,
            wTime = response.wTime,
            useDebts = response.useDebts,
            wDebts = response.wDebts,
            useAchievements = response.useAchievements,
            wAchievements = response.wAchievements
        )
    }
}