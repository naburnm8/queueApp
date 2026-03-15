package ru.naburnm8.queueapp.profile.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
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

    @GET("/api/profile/groups")
    suspend fun getAllDistinctGroups(): Response<List<String>>

    @GET("/api/profile/departments")
    suspend fun getAllDistinctDepartments(): Response<List<String>>

    @GET("/api/profile/teachers")
    suspend fun getAllOrByDepartmentTeachers(@Query("department") department: String? = null): Response<List<TeacherResponse>>

    @GET("/api/profile/students")
    suspend fun getAllOrByGroupStudents(@Query("group") group: String? = null): Response<List<StudentResponse>>
}