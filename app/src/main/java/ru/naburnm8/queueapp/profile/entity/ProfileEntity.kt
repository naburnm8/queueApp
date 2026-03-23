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
) {
    companion object {
        val teacherMock = ProfileEntity(
            id = UUID(0,0),
            firstName = "Иван",
            lastName = "Иванов",
            patronymic = "Иванович",
            multifield = "ИУ5",
            telegram = "@ivanov",
            avatarUrl = null,
            multifieldType = ProfileMultifieldType.DEPARTMENT,
        )

        val studentMock = ProfileEntity(
            id = UUID(0,0),
            firstName = "Петр",
            lastName = "Петров",
            patronymic = "Петрович",
            multifield = "ИУ5-52Б",
            telegram = "@petrov",
            avatarUrl = null,
            multifieldType = ProfileMultifieldType.ACADEMIC_GROUP,
        )
    }
}
