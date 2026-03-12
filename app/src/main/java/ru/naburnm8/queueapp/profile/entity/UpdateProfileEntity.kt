package ru.naburnm8.queueapp.profile.entity

data class UpdateProfileEntity(
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val multifield: String,
    val telegram: String?,
    val avatarUrl: String?,
    val multifieldType: ProfileMultifieldType
)
