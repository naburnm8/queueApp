package ru.naburnm8.queueapp.authorization.request

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String
)
