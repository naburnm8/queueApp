package ru.naburnm8.queueapp.queueOperator.metrics.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class StudentMetricsRequest(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val debtsCount: Int,
    val personalAchievementsScore: Int
)
