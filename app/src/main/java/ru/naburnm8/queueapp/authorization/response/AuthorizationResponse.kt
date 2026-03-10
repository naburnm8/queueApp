package ru.naburnm8.queueapp.authorization.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.authorization.entity.Role

@Serializable
data class AuthorizationResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val userRole: Role
)
