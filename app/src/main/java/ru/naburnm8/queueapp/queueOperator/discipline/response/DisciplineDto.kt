package ru.naburnm8.queueapp.queueOperator.discipline.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class DisciplineDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val personalAchievementsScoreLimit: Int,
)
