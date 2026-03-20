package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.item

import java.util.UUID

data class QueuePlanItem(
    val id: UUID?,
    val title: String,
    val useDebts: Boolean,
    val wDebts: Double,
    val useTime: Boolean,
    val wTime: Double,
    val useAchievements: Boolean,
    val wAchievements: Double,
    val slotDurationMinutes: Int,
)
