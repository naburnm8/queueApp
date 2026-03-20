package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity

import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import java.time.Instant
import java.util.UUID

data class QueuePlanEntity(
    val id: UUID? = null,
    val createdByTeacherId: UUID,
    val title: String,
    val status: QueueStatus,
    val useDebts: Boolean,
    val wDebts: Double,
    val useTime: Boolean,
    val wTime: Double,
    val useAchievements: Boolean,
    val wAchievements: Double,
    val createdAt: Instant,
    val slotDurationMinutes: Int,
)
