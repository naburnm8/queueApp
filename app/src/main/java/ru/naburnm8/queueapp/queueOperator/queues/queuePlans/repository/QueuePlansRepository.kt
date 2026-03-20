package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.repository

import okio.IOException
import retrofit2.Response
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.api.QueuePlansApi
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueuePlanRequest
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.response.QueuePlanResponse
import java.util.UUID

class QueuePlansRepository (
    private val queuePlansApi: QueuePlansApi
) {
    private fun <T> returnResult(response: Response<T>): Result<T> {
        return if(response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed queue plan request: ${response.code()}"))
        }
    }

    suspend fun createQueuePlan(
        disciplineId: UUID,
        req: QueuePlanRequest
    ) : Result<QueuePlanResponse> {
        val response = queuePlansApi.createQueuePlan(disciplineId, req)
        return returnResult(response)
    }

    suspend fun getQueuePlansByDiscipline (
        disciplineId: UUID
    ) : Result<List<QueuePlanResponse>> {
        val response = queuePlansApi.getQueuePlansByDiscipline(disciplineId)
        return returnResult(response)
    }

    suspend fun updateQueuePlan(
        disciplineId: UUID,
        req: QueuePlanRequest
    ) : Result<QueuePlanResponse> {
        val response = queuePlansApi.updateQueuePlan(disciplineId, req)
        return returnResult(response)
    }

    suspend fun getMyQueuePlans(): Result<List<QueuePlanResponse>> {
        val response = queuePlansApi.getMyQueuePlans()
        return returnResult(response)
    }

    suspend fun getQueuePlanById(
        queuePlanId: UUID
    ) : Result<QueuePlanResponse> {
        val response = queuePlansApi.getQueuePlanById(queuePlanId)
        return returnResult(response)
    }

    suspend fun activate(
        queuePlanId: UUID
    ) : Result<Unit> {
        val response = queuePlansApi.activate(queuePlanId)
        return returnResult(response)
    }

    suspend fun close(
        queuePlanId: UUID
    ) : Result<Unit> {
        val response = queuePlansApi.close(queuePlanId)
        return returnResult(response)
    }


}