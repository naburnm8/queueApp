package ru.naburnm8.queueapp.queueOperator.metrics.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.profile.response.StudentResponse
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplineDto
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class StudentMetricsResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val discipline: DisciplineDto,
    val student: StudentResponse,
    val teacher: TeacherResponse,
    val debtsCount: Int,
    val personalAchievementsScore: Int
)
