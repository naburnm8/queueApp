package ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.response.QueuePlanShortResponse
import java.util.UUID

interface QueuePlansShortApi {
    companion object {
        const val URI_BASE = "/api"
    }

    @GET("$URI_BASE/queuePlans")
    suspend fun getQueuePlans(): Response<List<QueuePlanShortResponse>>


    @GET("$URI_BASE/queuePlans/short/{queuePlanId}")
    suspend fun getShortQueuePlan(@Path("queuePlanId") queuePlanId: UUID): Response<QueuePlanShortResponse>

}