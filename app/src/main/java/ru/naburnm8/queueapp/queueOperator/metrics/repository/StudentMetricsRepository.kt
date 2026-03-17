package ru.naburnm8.queueapp.queueOperator.metrics.repository

import ru.naburnm8.queueapp.queueOperator.metrics.api.StudentMetricsApi
import ru.naburnm8.queueapp.queueOperator.metrics.request.StudentMetricsRequest
import ru.naburnm8.queueapp.queueOperator.metrics.response.StudentMetricsResponse
import java.io.IOException
import java.util.UUID

class StudentMetricsRepository (
    private val api: StudentMetricsApi
) {

    suspend fun metricsByDiscipline(disciplineId: UUID): Result<List<StudentMetricsResponse>> {
        val response = api.getMetricsByDiscipline(disciplineId)

        return if (response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body() ?: emptyList())
        } else {
            Result.failure(IOException("Failed to fetch metrics: ${response.code()} ${response.message()}"))
        }
    }

    suspend fun createStudentMetrics(disciplineId: UUID, studentId: UUID, req: StudentMetricsRequest): Result<StudentMetricsResponse> {
        val response = api.createStudentMetrics(disciplineId, studentId, req)
        return if (response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed to create metrics: ${response.code()} ${response.message()}"))
        }
    }

    suspend fun updateStudentMetrics(disciplineId: UUID, studentId: UUID, req: StudentMetricsRequest): Result<StudentMetricsResponse> {
        val response = api.updateStudentMetrics(disciplineId, studentId, req)
        return if (response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed to update metrics: ${response.code()} ${response.message()}"))
        }
    }

    suspend fun getStudentMetrics(disciplineId: UUID, studentId: UUID): Result<StudentMetricsResponse> {
        val response = api.getStudentMetrics(disciplineId, studentId)
        return if (response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed to fetch metrics: ${response.code()} ${response.message()}"))
        }
    }

    suspend fun deleteMetrics(metricId: UUID): Result<Unit> {
        val response = api.deleteMetrics(metricId)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(IOException("Failed to delete metrics: ${response.code()} ${response.message()}"))
        }
    }

}