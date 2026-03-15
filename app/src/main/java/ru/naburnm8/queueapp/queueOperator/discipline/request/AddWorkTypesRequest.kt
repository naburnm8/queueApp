package ru.naburnm8.queueapp.queueOperator.discipline.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import ru.naburnm8.queueapp.queueOperator.discipline.response.WorkTypeDto
import java.util.UUID

@Serializable
data class AddWorkTypesRequest(
    @Serializable(with = UUIDSerializer::class)
    val disciplineId: UUID,
    val workTypes: List<WorkTypeDto>
)
