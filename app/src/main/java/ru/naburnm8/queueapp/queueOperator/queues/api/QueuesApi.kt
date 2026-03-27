package ru.naburnm8.queueapp.queueOperator.queues.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.naburnm8.queueapp.queueOperator.queues.response.QueueSnapshotResponse
import java.util.UUID

interface QueuesApi {
    companion object {
        const val URI_BASE = "/api/queuePlans"
    }

    @GET("$URI_BASE/{queuePlanId}/view")
    suspend fun view(
        @Path("queuePlanId") queuePlanId: UUID,
        @Query("force") force: Boolean = false,
    ) : Response<QueueSnapshotResponse>

    @POST("$URI_BASE/{queuePlanId}/takeNext")
    suspend fun takeNext(@Path("queuePlanId") queuePlanId: UUID) : Response<Unit>

    @POST("$URI_BASE/{queuePlanId}/take/{requestId}")
    suspend fun take(
        @Path("queuePlanId") queuePlanId: UUID,
        @Path("requestId") requestId: UUID
    ) : Response<Unit>


}