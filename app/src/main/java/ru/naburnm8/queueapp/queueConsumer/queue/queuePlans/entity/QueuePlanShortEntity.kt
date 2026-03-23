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
) {
    companion object {
        val mock = QueuePlanShortEntity(
            id = UUID(0,0),
            title = "Очередь 1",
            discipline = DisciplineEntity.mock,
            status = QueueStatus.ACTIVE,
            teacher = ProfileEntity.teacherMock,
            slotDurationMinutes = 15,
        )
    }
}
