package ru.naburnm8.queueapp.queueOperator.queues.repository

import okio.IOException
import retrofit2.Response
import ru.naburnm8.queueapp.queueOperator.queues.api.QueuesApi
import ru.naburnm8.queueapp.queueOperator.queues.response.QueueSnapshotResponse
import java.util.UUID

class QueuesRepository (
    private val queuesApi: QueuesApi
) {
    private fun <T> returnResult(response: Response<T>): Result<T> {
        return if(response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed queue request: ${response.code()}"))
        }
    }

    suspend fun view(queuePlanId: UUID, force: Boolean = false): Result<QueueSnapshotResponse> {
        return returnResult(queuesApi.view(queuePlanId, force))
    }

    suspend fun takeNext(queuePlanId: UUID): Result<Unit> {
        return returnResult(queuesApi.takeNext(queuePlanId))
    }

    suspend fun take(queuePlanId: UUID, requestId: UUID): Result<Unit> {
        return returnResult(queuesApi.take(queuePlanId, requestId))
    }
}