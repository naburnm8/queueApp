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
) {
    companion object {
        val mock = QueuePlanEntity(
            id = UUID.randomUUID(),
            createdByTeacherId = UUID.randomUUID(),
            title = "План очереди 1",
            status = QueueStatus.ACTIVE,
            useDebts = true,
            wDebts = 0.5,
            useTime = true,
            wTime = 0.3,
            useAchievements = true,
            wAchievements = 0.2,
            createdAt = Instant.now(),
            slotDurationMinutes = 15
        )
    }
}
