package ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.api

import retrofit2.Response
import retrofit2.http.GET
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.response.QueuePlanShortResponse

interface QueuePlansShortApi {
    companion object {
        const val URI_BASE = "/api"
    }

    @GET("/queuePlans")
    suspend fun getQueuePlans(): Response<List<QueuePlanShortResponse>>

}