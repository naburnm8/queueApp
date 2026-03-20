package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueuePlanRequest
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.response.QueuePlanResponse
import java.util.UUID

interface QueuePlansApi {
    companion object {
        private const val URI_BASE = "/api"
    }

    @POST("$URI_BASE/disciplines/{disciplineId}/queuePlans")
    suspend fun createQueuePlan(
        @Path("disciplineId") disciplineId: UUID,
        @Body req: QueuePlanRequest,
    ) : Response<QueuePlanResponse>

    @GET("$URI_BASE/disciplines/{disciplineId}/queuePlans")
    suspend fun getQueuePlansByDiscipline(
        @Path("disciplineId") disciplineId: UUID,
    ) : Response<List<QueuePlanResponse>>

    @PUT("$URI_BASE/disciplines/{disciplineId}/queuePlans")
    suspend fun updateQueuePlan(
        @Path("disciplineId") disciplineId: UUID,
        @Body req: QueuePlanRequest
    ) : Response<QueuePlanResponse>

    @GET("$URI_BASE/queuePlans/my")
    suspend fun getMyQueuePlans(): Response<List<QueuePlanResponse>>

    @GET("$URI_BASE/queuePlans/{queuePlanId}")
    suspend fun getQueuePlanById(
        @Path("queuePlanId") queuePlanId: UUID,
    ) : Response<QueuePlanResponse>

    @POST("$URI_BASE/queuePlans/{queuePlanId}/activate")
    suspend fun activate(@Path("queuePlanId") queuePlanId: UUID): Response<Unit>

    @POST("$URI_BASE/queuePlans/{queuePlanId}/close")
    suspend fun close(@Path("queuePlanId") queuePlanId: UUID): Response<Unit>
}