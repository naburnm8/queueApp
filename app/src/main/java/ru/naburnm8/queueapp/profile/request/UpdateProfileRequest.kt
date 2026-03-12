package ru.naburnm8.queueapp.profile.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.profile.entity.ProfileMultifieldType

@Serializable
data class UpdateProfileRequest (
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val telegram: String?,
    val avatarUrl: String?,
    val multifield: String,
    val multifieldType: ProfileMultifieldType,
)