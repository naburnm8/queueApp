package ru.naburnm8.queueapp.profile.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import ru.naburnm8.queueapp.profile.request.UpdateProfileRequest
import ru.naburnm8.queueapp.profile.response.StudentResponse
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.profile.response.UpdateProfileResponse

interface ProfileApi {

    @GET("/api/profile/me/student")
    suspend fun getMeStudent(): Response<StudentResponse>

    @GET("/api/profile/me/teacher")
    suspend fun getMeTeacher(): Response<TeacherResponse>

    @PUT("/api/profile/me")
    suspend fun updateMe(@Body req: UpdateProfileRequest): Response<UpdateProfileResponse>
}