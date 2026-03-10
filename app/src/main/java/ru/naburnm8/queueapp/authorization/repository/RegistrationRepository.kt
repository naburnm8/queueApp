package ru.naburnm8.queueapp.authorization.repository

import okio.IOException
import ru.naburnm8.queueapp.authorization.api.RegistrationApi
import ru.naburnm8.queueapp.authorization.request.RegisterStudentRequest
import ru.naburnm8.queueapp.authorization.request.RegisterTeacherRequest
import ru.naburnm8.queueapp.authorization.response.RegisterResponse

class RegistrationRepository (
    private val registrationApi: RegistrationApi
) {

    suspend fun registerStudent(req: RegisterStudentRequest) : Result<RegisterResponse> {
        val result = registrationApi.registerStudent(req)
        return if (result.isSuccessful) {
            if (result.body() == null) throw IllegalStateException("Response body is null")
            Result.success(result.body()!!)
        } else {
            Result.failure(IOException("Failed to register student: ${result.code()} ${result.message()}"))
        }
    }

    suspend fun registerTeacher(req: RegisterTeacherRequest) : Result<RegisterResponse> {
        val result = registrationApi.registerTeacher(req)
        return if (result.isSuccessful) {
            if (result.body() == null) throw IllegalStateException("Response body is null")
            Result.success(result.body()!!)
        } else {
            Result.failure(IOException("Failed to register student: ${result.code()} ${result.message()}"))
        }
    }

}