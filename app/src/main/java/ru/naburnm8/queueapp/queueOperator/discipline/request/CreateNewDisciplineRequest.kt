package ru.naburnm8.queueapp.queueOperator.discipline.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateNewDisciplineRequest(
    val name: String,
    val personalAchievementsScoreLimit: Int,
)
