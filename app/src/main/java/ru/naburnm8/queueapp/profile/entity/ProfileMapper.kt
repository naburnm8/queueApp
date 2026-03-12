package ru.naburnm8.queueapp.profile.entity

import ru.naburnm8.queueapp.profile.request.UpdateProfileRequest
import ru.naburnm8.queueapp.profile.response.StudentResponse
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.profile.response.UpdateProfileResponse

object ProfileMapper {
    fun map(resp: StudentResponse): ProfileEntity {
        return ProfileEntity(
            id = resp.id,
            firstName = resp.firstName,
            lastName = resp.lastName,
            patronymic = resp.patronymic,
            multifield = resp.academicGroup,
            telegram = resp.telegram,
            avatarUrl = resp.avatarUrl,
            multifieldType = ProfileMultifieldType.ACADEMIC_GROUP
        )
    }

    fun map(resp: TeacherResponse): ProfileEntity {
        return ProfileEntity(
            id = resp.id,
            firstName = resp.firstName,
            lastName = resp.lastName,
            patronymic = resp.patronymic,
            multifield = resp.department,
            telegram = resp.telegram,
            avatarUrl = resp.avatarUrl,
            multifieldType = ProfileMultifieldType.DEPARTMENT
        )
    }

    fun map(resp: UpdateProfileResponse): UpdateProfileEntity {
        return UpdateProfileEntity(
            firstName = resp.firstName,
            lastName = resp.lastName,
            patronymic = resp.patronymic,
            multifield = resp.multifield,
            telegram = resp.telegram,
            avatarUrl = resp.avatarUrl,
            multifieldType = resp.multifieldType
        )
    }

    fun toRequest(entity: UpdateProfileEntity): UpdateProfileRequest {
        return UpdateProfileRequest(
            firstName = entity.firstName,
            lastName = entity.lastName,
            patronymic = entity.patronymic,
            multifield = entity.multifield,
            telegram = entity.telegram,
            avatarUrl = entity.avatarUrl,
            multifieldType = entity.multifieldType
        )
    }
}