package ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplineDto
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class QueuePlanShortResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    val discipline: DisciplineDto,
    val status: QueueStatus,
    val teacher: TeacherResponse,
    val slotDurationMinutes: Int,
)
