package ru.naburnm8.queueapp.authorization.repository

import okio.IOException
import ru.naburnm8.queueapp.authorization.api.IntegrationApi
import ru.naburnm8.queueapp.authorization.request.RegisterStudentRequest
import ru.naburnm8.queueapp.authorization.response.IntegrationResponse
import java.util.UUID

class IntegrationRepository(
    private val integrationApi: IntegrationApi
) {

    suspend fun getAllIntegrations(): Result<List<IntegrationResponse>> {
        val result = integrationApi.getAllIntegrations()
        return if (result.isSuccessful) {
            if (result.body() == null) Result.failure(IllegalStateException("Response body is null"))
            else Result.success(result.body()!!)
        } else {
            Result.failure(IOException("Failed to get integrations: ${result.code()} ${result.message()}"))
        }
    }

    suspend fun registerWithIntegration(id: UUID, req: RegisterStudentRequest): Result<UUID> {
        val result = integrationApi.registerWithIntegration(id, req)
        return if (result.isSuccessful) {
            if (result.body() == null) Result.failure(IllegalStateException("Response body is null"))
            else Result.success(result.body()!!)
        } else {
            Result.failure(IOException("Failed to register with integration: ${result.code()} ${result.message()}"))
        }
    }
}