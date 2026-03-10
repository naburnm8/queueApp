package ru.naburnm8.queueapp.authorization.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterTeacherRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val department: String,
    val telegram: String?,
    val avatarUrl: String?,
)
