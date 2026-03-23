package ru.naburnm8.queueapp.queueConsumer.submissionRequests.repository

import okio.IOException
import retrofit2.Response
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.api.SubmissionRequestsApi
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.request.SubmissionRequestRequest
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestResponse
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import java.util.UUID

class SubmissionRequestsRepository (
    private val api: SubmissionRequestsApi
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

    suspend fun getAllMySubmissionRequests(

    ) : Result<List<SubmissionRequestResponse>> {
        val response = api.getAllMySubmissionRequests()
        return returnResult(response)
    }

    suspend fun getMySubmissionRequest(
        queuePlanId: UUID
    ) : Result<SubmissionRequestResponse> {
        val response = api.getMySubmissionRequest(queuePlanId)
        return returnResult(response)
    }

    suspend fun createMySubmissionRequest(
        queuePlanId: UUID,
        req: SubmissionRequestRequest
    ) : Result<SubmissionRequestResponse> {
        val response = api.createMySubmissionRequest(queuePlanId, req)
        return returnResult(response)
    }

    suspend fun updateMySubmissionRequest(
        queuePlanId: UUID,
        req: SubmissionRequestRequest
    ) : Result<SubmissionRequestResponse> {
        val response = api.updateMySubmissionRequest(queuePlanId, req)
        return returnResult(response)
    }

    suspend fun deleteMySubmissionRequest(
        queuePlanId: UUID
    ) : Result<Unit> {
        val response = api.deleteMySubmissionRequest(queuePlanId)
        return returnResult(response)
    }

    suspend fun getAllSubmissionRequests(
        queuePlanId: UUID,
        status: SubmissionStatus? = null,
    ) : Result<List<SubmissionRequestResponse>> {
        val response = api.getAllSubmissionRequests(queuePlanId, status)
        return returnResult(response)
    }

    suspend fun updateSubmissionRequestStatus(
        queuePlanId: UUID,
        requestId: UUID,
        status: SubmissionStatus,
    ) : Result<Unit> {
        val response = api.updateSubmissionRequestStatus(queuePlanId, requestId, status)
        return returnResult(response)
    }
}