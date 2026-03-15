package ru.naburnm8.queueapp.queueOperator.discipline.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class AddOwnersRequest(
    val idsToAdd: List<@Serializable(with = UUIDSerializer::class) UUID>
)
