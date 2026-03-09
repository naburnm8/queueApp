package ru.naburnm8.queueapp.authorization.api


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.naburnm8.queueapp.authorization.request.RegisterStudentRequest
import ru.naburnm8.queueapp.authorization.response.RegisterResponse

interface RegistrationApi {

    @POST("/api/auth/register/student")
    suspend fun registerStudent(@Body req: RegisterStudentRequest): Response<RegisterResponse>

}