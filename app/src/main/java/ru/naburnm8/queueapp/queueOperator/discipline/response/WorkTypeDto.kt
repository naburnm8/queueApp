package ru.naburnm8.queueapp.queueOperator.discipline.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class WorkTypeDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val name: String,
    val estimatedTimeMinutes: Int,
)
