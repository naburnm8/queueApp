package ru.naburnm8.queueapp.authorization.entity

data class RegisterTeacherEntity(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val department: String,
    val telegram: String?,
    val avatarUrl: String?,
)
