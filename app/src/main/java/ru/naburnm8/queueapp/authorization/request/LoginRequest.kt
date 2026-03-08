package ru.naburnm8.queueapp.authorization.request

data class LoginRequest(
    val email: String,
    val password: String,
)
