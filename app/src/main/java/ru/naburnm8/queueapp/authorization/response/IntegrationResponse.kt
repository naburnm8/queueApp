package ru.naburnm8.queueapp.authorization.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.util.UUID
import ru.naburnm8.queueapp.authorization.serialization.UUIDSerializer

@Serializable
data class IntegrationResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val payload: JsonObject
)
