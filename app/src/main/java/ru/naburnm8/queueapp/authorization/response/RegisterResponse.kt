package ru.naburnm8.queueapp.authorization.response

import kotlinx.serialization.Serializable


@Serializable
data class RegisterResponse(
    val lastName: String,
    val email: String,
    val role: String
)
