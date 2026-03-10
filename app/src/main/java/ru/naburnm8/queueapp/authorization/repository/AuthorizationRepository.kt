package ru.naburnm8.queueapp.authorization.repository

import okio.IOException
import ru.naburnm8.queueapp.authorization.api.AuthorizationApi
import ru.naburnm8.queueapp.authorization.request.LoginRequest
import ru.naburnm8.queueapp.authorization.request.LogoutRequest
import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse


class AuthorizationRepository (
    private val authorizationApi: AuthorizationApi,
) {
    suspend fun login (req: LoginRequest): Result<AuthorizationResponse> {
        val result = authorizationApi.login(req)
        if (result.isSuccessful) {
            if (result.body() == null) return Result.failure(IllegalStateException("Response body is null"))
            return Result.success(result.body()!!)
        } else {
            return Result.failure(IOException("Failed to login: ${result.code()} ${result.message()}"))
        }
    }

    suspend fun logout(req: LogoutRequest) : Result<Unit> {
        val result = authorizationApi.logout(req)
        return if (result.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(IOException("Failed to logout: ${result.code()} ${result.message()}"))
        }
    }
}