package ru.naburnm8.queueapp.profile.entity

import java.util.UUID

data class ProfileEntity(
    var id: UUID,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val multifield: String,
    val telegram: String?,
    val avatarUrl: String?,
    val multifieldType: ProfileMultifieldType
)
