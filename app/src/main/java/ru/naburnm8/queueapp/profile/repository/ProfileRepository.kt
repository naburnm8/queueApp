package ru.naburnm8.queueapp.profile.repository

import okio.IOException
import ru.naburnm8.queueapp.profile.api.ProfileApi
import ru.naburnm8.queueapp.profile.request.UpdateProfileRequest
import ru.naburnm8.queueapp.profile.response.StudentResponse
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.profile.response.UpdateProfileResponse

class ProfileRepository (
    private val profileApi: ProfileApi
) {
    suspend fun getMeStudent(): Result<StudentResponse> {
        val response = profileApi.getMeStudent()
        if (!response.isSuccessful) {
            return Result.failure(IOException("${response.code()}, ${response.errorBody()}"))
        }
        if (response.body() == null) {
            return Result.failure(IllegalStateException("Body is null"))
        }
        return Result.success(response.body()!!)
    }

    suspend fun getMeTeacher(): Result<TeacherResponse> {
        val response = profileApi.getMeTeacher()
        if (!response.isSuccessful) {
            return Result.failure(IOException("${response.code()}, ${response.errorBody()}"))
        }
        if (response.body() == null) {
            return Result.failure(IllegalStateException("Body is null"))
        }
        return Result.success(response.body()!!)
    }

    suspend fun updateMe(req: UpdateProfileRequest): Result<UpdateProfileResponse> {
        val response = profileApi.updateMe(req)
        if (!response.isSuccessful) {
            return Result.failure(IOException("${response.code()}, ${response.errorBody()}"))
        }
        if (response.body() == null) {
            return Result.failure(IllegalStateException("Body is null"))
        }
        return Result.success(response.body()!!)
    }

    suspend fun getAllDistinctGroups() : Result<List<String>> {
        val response = profileApi.getAllDistinctGroups()
        if (!response.isSuccessful) {
            return Result.failure(IOException("${response.code()}, ${response.errorBody()}"))
        }
        if (response.body() == null) {
            return Result.failure(IllegalStateException("Body is null"))
        }
        return Result.success(response.body()!!)
    }

    suspend fun getAllDistinctDepartments() : Result<List<String>> {
        val response = profileApi.getAllDistinctDepartments()
        if (!response.isSuccessful) {
            return Result.failure(IOException("${response.code()}, ${response.errorBody()}"))
        }
        if (response.body() == null) {
            return Result.failure(IllegalStateException("Body is null"))
        }
        return Result.success(response.body()!!)
    }

    suspend fun getAllOrByDepartmentTeachers(department: String?) : Result<List<TeacherResponse>> {
        val response = profileApi.getAllOrByDepartmentTeachers(department)
        if (!response.isSuccessful) {
            return Result.failure(IOException("${response.code()}, ${response.errorBody()}"))
        }
        if (response.body() == null) {
            return Result.failure(IllegalStateException("Body is null"))
        }
        return Result.success(response.body()!!)
    }

    suspend fun getAllOrByGroupStudents(group: String?) : Result<List<StudentResponse>> {
        val response = profileApi.getAllOrByGroupStudents(group)
        if (!response.isSuccessful) {
            return Result.failure(IOException("${response.code()}, ${response.errorBody()}"))
        }
        if (response.body() == null) {
            return Result.failure(IllegalStateException("Body is null"))
        }
        return Result.success(response.body()!!)
    }
}