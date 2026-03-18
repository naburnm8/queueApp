package ru.naburnm8.queueapp.queueOperator.metrics.entity

import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplinesMapper
import ru.naburnm8.queueapp.queueOperator.metrics.request.StudentMetricsRequest
import ru.naburnm8.queueapp.queueOperator.metrics.response.StudentMetricsResponse

object StudentMetricMapper {
    fun map(response: StudentMetricsResponse): StudentMetricEntity {
        return StudentMetricEntity(
            id = response.id,
            discipline = DisciplinesMapper.map(response.discipline),
            student = ProfileMapper.map(response.student),
            teacher = ProfileMapper.map(response.teacher),
            debtsCount = response.debtsCount,
            personalAchievementsScore = response.personalAchievementsScore
        )
    }

    fun toRequest(entity: StudentMetricEntity): StudentMetricsRequest {
        return StudentMetricsRequest(
            id = entity.id,
            debtsCount = entity.debtsCount,
            personalAchievementsScore = entity.personalAchievementsScore
        )
    }
}