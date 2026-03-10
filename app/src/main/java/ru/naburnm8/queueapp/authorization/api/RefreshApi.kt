package ru.naburnm8.queueapp.authorization.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.naburnm8.queueapp.authorization.request.RefreshRequest
import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse

interface RefreshApi {
    @POST("/api/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<AuthorizationResponse>

    @POST("/api/auth/refresh")
    fun refreshBlocking(@Body request: RefreshRequest): Call<AuthorizationResponse>
}