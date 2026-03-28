package ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.repository

import okio.IOException
import retrofit2.Response
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.api.QueuePlansShortApi
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.response.QueuePlanShortResponse
import java.util.UUID

class QueuePlansShortRepository (
    private val api: QueuePlansShortApi
) {
    private fun <T> returnResult(response: Response<T>): Result<T> {
        return if(response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed submission requests request: ${response.code()}"))
        }
    }

    suspend fun getAllQueuePlans() : Result<List<QueuePlanShortResponse>> {
        val response = api.getQueuePlans()
        return returnResult(response)
    }

    suspend fun getShortQueuePlan(queuePlanId: UUID) : Result<QueuePlanShortResponse> {
        val response = api.getShortQueuePlan(queuePlanId)
        return returnResult(response)
    }
}