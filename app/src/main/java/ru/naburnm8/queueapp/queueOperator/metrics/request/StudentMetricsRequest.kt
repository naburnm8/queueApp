package ru.naburnm8.queueapp.queueOperator.metrics.request

import java.util.UUID

data class StudentMetricsRequest(
    val id: UUID? = null,
    val debtsCount: Int,
    val personalAchievementsScore: Int
)
