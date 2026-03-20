package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.serialization.InstantSerializer
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.time.Instant
import java.util.UUID

@Serializable
data class QueuePlanResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    @Serializable(with = UUIDSerializer::class)
    val disciplineId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val createdByTeacherId: UUID,
    val title: String,
    val status: QueueStatus,
    val useDebts: Boolean,
    val wDebts: Double,
    val useTime: Boolean,
    val wTime: Double,
    val useAchievements: Boolean,
    val wAchievements: Double,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    val slotDurationMinutes: Int,
)
