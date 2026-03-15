package ru.naburnm8.queueapp.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class DeleteRequest(
    val ids: List<@Serializable(with = UUIDSerializer::class) UUID>
)
