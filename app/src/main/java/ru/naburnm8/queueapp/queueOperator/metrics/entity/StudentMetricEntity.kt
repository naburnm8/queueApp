package ru.naburnm8.queueapp.queueOperator.metrics.entity

import ru.naburnm8.queueapp.profile.response.StudentResponse
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplineDto
import java.util.UUID

data class StudentMetricEntity(
    val id: UUID,
    val discipline: DisciplineDto,
    val student: StudentResponse,
    val teacher: TeacherResponse,
    val debtsCount: Int,
    val personalAchievementsScore: Int
)
