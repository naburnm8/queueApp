package ru.naburnm8.queueapp.authorization.response

data class AuthorizationResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
)
