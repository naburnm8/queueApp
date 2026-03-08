package ru.naburnm8.queueapp.authorization.entity

import ru.naburnm8.queueapp.authorization.request.RegisterStudentRequest
import ru.naburnm8.queueapp.authorization.request.RegisterTeacherRequest

object AuthorizationMapper {

    fun toDto(entity: RegisterStudentEntity): RegisterStudentRequest {
        return RegisterStudentRequest(
            email = entity.email,
            password = entity.password,
            firstName = entity.firstName,
            lastName = entity.lastName,
            patronymic = entity.patronymic,
            academicGroup = entity.academicGroup,
            telegram = entity.telegram,
            avatarUrl = entity.avatarUrl
        )
    }

    fun toDto(entity: RegisterTeacherEntity): RegisterTeacherRequest {
        return RegisterTeacherRequest(
            email = entity.email,
            password = entity.password,
            firstName = entity.firstName,
            lastName = entity.lastName,
            patronymic = entity.patronymic,
            telegram = entity.telegram,
            avatarUrl = entity.avatarUrl,
            department = entity.department
        )
    }



}