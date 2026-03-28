package ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import java.util.UUID

data class QueuePlanShortEntity(
    val id: UUID,
    val title: String,
    val discipline: DisciplineEntity,
    val status: QueueStatus,
    val teacher: ProfileEntity,
    val slotDurationMinutes: Int,
    val useTime: Boolean,
    val wTime: Double,
    val useDebts: Boolean,
    val wDebts: Double,
    val useAchievements: Boolean,
    val wAchievements: Double
) {
    companion object {
        val mock = QueuePlanShortEntity(
            id = UUID(0,0),
            title = "Очередь 1",
            discipline = DisciplineEntity.mock,
            status = QueueStatus.ACTIVE,
            teacher = ProfileEntity.teacherMock,
            slotDurationMinutes = 15,
            useTime = true,
            wTime = 0.5,
            useDebts = true,
            wDebts = 0.3,
            useAchievements = true,
            wAchievements = 0.2
        )
    }
}
