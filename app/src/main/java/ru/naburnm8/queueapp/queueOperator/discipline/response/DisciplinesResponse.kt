package ru.naburnm8.queueapp.queueOperator.discipline.response

import kotlinx.serialization.Serializable

@Serializable
data class DisciplinesResponse(
    val disciplines: List<DisciplineDto>
)
