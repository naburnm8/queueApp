package ru.naburnm8.queueapp.authorization.entity

data class RegisterStudentEntity(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val academicGroup: String,
    val telegram: String?,
    val avatarUrl: String?
)
