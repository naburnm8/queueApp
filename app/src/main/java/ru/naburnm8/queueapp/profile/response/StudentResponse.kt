package ru.naburnm8.queueapp.profile.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.authorization.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class StudentResponse(
    @Serializable(with = UUIDSerializer::class)
    var id: UUID,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val academicGroup: String,
    val telegram: String?,
    val avatarUrl: String?
)
