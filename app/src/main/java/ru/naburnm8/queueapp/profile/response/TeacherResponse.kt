package ru.naburnm8.queueapp.profile.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.authorization.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class TeacherResponse (
    @Serializable(with = UUIDSerializer::class)
    var id: UUID,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val department: String,
    val telegram: String?,
    val avatarUrl: String?
) {
}