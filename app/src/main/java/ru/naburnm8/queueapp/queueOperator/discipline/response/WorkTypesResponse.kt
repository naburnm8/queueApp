package ru.naburnm8.queueapp.queueOperator.discipline.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkTypesResponse(
    val workTypes: List<WorkTypeDto>
)
