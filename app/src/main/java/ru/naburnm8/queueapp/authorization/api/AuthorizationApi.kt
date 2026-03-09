package ru.naburnm8.queueapp.authorization.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.naburnm8.queueapp.authorization.request.LoginRequest
import ru.naburnm8.queueapp.authorization.request.LogoutRequest
import ru.naburnm8.queueapp.authorization.request.RefreshRequest
import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse

interface AuthorizationApi {

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthorizationResponse>

    @POST("/api/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<AuthorizationResponse>

    @POST("/api/auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>

}