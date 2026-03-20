package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class QueuePlanRequest(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val title: String,
    val status: QueueStatus,
    val useDebts: Boolean,
    val wDebts: Double,
    val useTime: Boolean,
    val wTime: Double,
    val useAchievements: Boolean,
    val wAchievements: Double,
    val slotDurationMinutes: Int,
)
