package ru.naburnm8.queueapp.queueOperator.discipline.entity

import java.util.UUID

data class DisciplineEntity(
    val id: UUID = UUID(0,0),
    val name: String,
    val personalAchievementsScoreLimit: Int,
) {
    companion object {
        val mock = DisciplineEntity(
            id = UUID(0,0),
            name = "Дисциплина 1",
            personalAchievementsScoreLimit = 100,
        )
    }
}
