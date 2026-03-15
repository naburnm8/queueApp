package ru.naburnm8.queueapp.queueOperator.discipline.entity

import java.util.UUID

data class DisciplineEntity(
    val id: UUID = UUID(0,0),
    val name: String,
    val personalAchievementsScoreLimit: Int,
)
