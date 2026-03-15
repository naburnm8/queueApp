package ru.naburnm8.queueapp.queueOperator.discipline.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.discipline.response.WorkTypeDto

@Serializable
data class UpdateWorkTypesRequest(
    val updated: List<WorkTypeDto>
)
