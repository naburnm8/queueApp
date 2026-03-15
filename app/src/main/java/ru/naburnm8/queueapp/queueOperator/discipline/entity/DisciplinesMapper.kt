package ru.naburnm8.queueapp.queueOperator.discipline.entity

import ru.naburnm8.queueapp.queueOperator.discipline.request.CreateNewDisciplineRequest
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplineDto
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplinesResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.WorkTypeDto
import ru.naburnm8.queueapp.queueOperator.discipline.response.WorkTypesResponse

object DisciplinesMapper {
    fun map(response: DisciplinesResponse): List<DisciplineEntity> {
        return response.disciplines.map { discipline ->
            DisciplineEntity(
                id = discipline.id,
                name = discipline.name,
                personalAchievementsScoreLimit = discipline.personalAchievementsScoreLimit,
            )
        }
    }

    fun map(response: WorkTypesResponse): List<WorkTypeEntity> {
        return response.workTypes.map { workType ->
            WorkTypeEntity(
                id = workType.id,
                name = workType.name,
                estimatedTimeMinutes = workType.estimatedTimeMinutes
            )
        }
    }

    fun toCreateRequest(discipline: DisciplineEntity): CreateNewDisciplineRequest {
        return CreateNewDisciplineRequest(
            name = discipline.name,
            personalAchievementsScoreLimit = discipline.personalAchievementsScoreLimit,
        )
    }

    fun toDto(workType: WorkTypeEntity): WorkTypeDto {
        return WorkTypeDto(
            id = workType.id,
            name = workType.name,
            estimatedTimeMinutes = workType.estimatedTimeMinutes
        )
    }

    fun toDto(discipline: DisciplineEntity): DisciplineDto {
        return DisciplineDto(
            id = discipline.id,
            name = discipline.name,
            personalAchievementsScoreLimit = discipline.personalAchievementsScoreLimit,
        )
    }
}