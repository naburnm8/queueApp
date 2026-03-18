package ru.naburnm8.queueapp.queueOperator.metrics.entity

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import java.util.UUID

data class StudentMetricEntity(
    val id: UUID?,
    val discipline: DisciplineEntity,
    val student: ProfileEntity,
    val teacher: ProfileEntity,
    val debtsCount: Int,
    val personalAchievementsScore: Int
)
