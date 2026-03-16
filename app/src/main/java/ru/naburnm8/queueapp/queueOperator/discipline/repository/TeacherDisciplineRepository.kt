package ru.naburnm8.queueapp.queueOperator.discipline.repository

import okio.IOException
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.queueOperator.discipline.api.DisciplineApi
import ru.naburnm8.queueapp.queueOperator.discipline.request.AddOwnersRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.AddWorkTypesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.CreateNewDisciplineRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.UpdateDisciplinesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.UpdateWorkTypesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplinesResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.WorkTypesResponse
import ru.naburnm8.queueapp.request.DeleteRequest
import java.util.UUID

class TeacherDisciplineRepository(
    disciplineApi: DisciplineApi
) : StudentDisciplineRepository (disciplineApi) {

    suspend fun getOwners(disciplineId: UUID): Result<List<TeacherResponse>> {
        val response = disciplineApi.getOwners(disciplineId)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(IllegalStateException("Response body is null"))
        } else Result.failure(IOException("Failed to get owners: ${response.code()} ${response.message()}"))
    }

    suspend fun addOwners(disciplineId: UUID, req: AddOwnersRequest): Result<Unit> {
        val response = disciplineApi.addOwners(disciplineId, req)
        return if (response.isSuccessful) Result.success(Unit)
        else Result.failure(IOException("Failed to add owners: ${response.code()} ${response.message()}"))
    }

    suspend fun getMyDisciplines(): Result<DisciplinesResponse> {
        val response = disciplineApi.getMyDisciplines()
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(IllegalStateException("Response body is null"))
        } else Result.failure(IOException("Failed to get disciplines: ${response.code()} ${response.message()}"))
    }

    suspend fun createDiscipline(req: CreateNewDisciplineRequest): Result<DisciplinesResponse> {
        val response = disciplineApi.createDiscipline(req)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(IllegalStateException("Response body is null"))
        } else Result.failure(IOException("Failed to create discipline: ${response.code()} ${response.message()}"))
    }

    suspend fun addWorkTypes(req: AddWorkTypesRequest): Result<WorkTypesResponse> {
        val response = disciplineApi.addWorkTypes(req)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(IllegalStateException("Response body is null"))
        } else Result.failure(IOException("Failed to add work types: ${response.code()} ${response.message()}"))
    }

    suspend fun deleteDisciplines(req: DeleteRequest): Result<Unit> {
        val response = disciplineApi.deleteDisciplines(req)
        return if (response.isSuccessful) Result.success(Unit)
        else Result.failure(IOException("Failed to delete disciplines: ${response.code()} ${response.message()}"))
    }

    suspend fun deleteWorkTypes(req: DeleteRequest): Result<Unit> {
        val response = disciplineApi.deleteWorkTypes(req)
        return if (response.isSuccessful) Result.success(Unit)
        else Result.failure(IOException("Failed to delete work types: ${response.code()} ${response.message()}"))
    }

    suspend fun updateDisciplines(req: UpdateDisciplinesRequest): Result<DisciplinesResponse> {
        val response = disciplineApi.updateDisciplines(req)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(IllegalStateException("Response body is null"))
        } else Result.failure(IOException("Failed to update disciplines: ${response.code()} ${response.message()}"))
    }

    suspend fun updateWorkTypes(req: UpdateWorkTypesRequest): Result<WorkTypesResponse> {
        val response = disciplineApi.updateWorkTypes(req)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(IllegalStateException("Response body is null"))
        } else Result.failure(IOException("Failed to update work types: ${response.code()} ${response.message()}"))
    }

    suspend fun leaveDiscipline(disciplineId: UUID): Result<Unit> {
        val response = disciplineApi.leaveDiscipline(disciplineId)
        return if (response.isSuccessful) Result.success(Unit)
        else Result.failure(IOException("Failed to leave discipline: ${response.code()} ${response.message()}"))
    }
}