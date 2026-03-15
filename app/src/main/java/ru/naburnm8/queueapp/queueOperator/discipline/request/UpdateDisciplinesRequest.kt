package ru.naburnm8.queueapp.queueOperator.discipline.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplineDto

@Serializable
data class UpdateDisciplinesRequest(
    val newDisciplines: List<DisciplineDto>
)
