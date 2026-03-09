package ru.naburnm8.queueapp.authorization.api


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.naburnm8.queueapp.authorization.request.RegisterStudentRequest
import ru.naburnm8.queueapp.authorization.response.IntegrationResponse
import java.util.UUID

interface IntegrationApi {

    @GET("/api/auth/integration/")
    suspend fun getAllIntegrations(): Response<List<IntegrationResponse>>

    @POST("/api/auth/integration/{id}/register/")
    suspend fun registerWithIntegration(@Path("id") id: UUID, @Body req: RegisterStudentRequest): Response<UUID>
}