package ru.naburnm8.queueapp.queueOperator.discipline.repository

import okio.IOException
import ru.naburnm8.queueapp.queueOperator.discipline.api.DisciplineApi
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplinesResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.WorkTypesResponse
import java.util.UUID

open class StudentDisciplineRepository (
    protected val disciplineApi: DisciplineApi
) {
    open suspend fun getDisciplines(): Result<DisciplinesResponse> {
        val response = disciplineApi.getDisciplines()
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(IllegalStateException("Response body is null"))
            }
        } else {
            Result.failure(IOException("Failed to fetch disciplines: ${response.code()} ${response.message()}"))
        }
    }

    open suspend fun getWorkTypesById(disciplineId: UUID): Result<WorkTypesResponse> {
        val response = disciplineApi.getWorkTypesById(disciplineId)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(IllegalStateException("Response body is null"))
            }
        } else {
            Result.failure(IOException("Failed to fetch work types: ${response.code()} ${response.message()}"))
        }
    }
}