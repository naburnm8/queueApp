package ru.naburnm8.queueapp.queueOperator.queues.invitations.repository

import okio.IOException
import retrofit2.Response
import ru.naburnm8.queueapp.queueOperator.queues.invitations.api.InvitationsApi
import ru.naburnm8.queueapp.queueOperator.queues.invitations.request.InvitationRequest
import ru.naburnm8.queueapp.queueOperator.queues.invitations.response.InvitationResponse
import java.util.UUID

class InvitationsRepository (
    private val invitationsApi: InvitationsApi
) {
    private fun <T> returnResult(response: Response<T>): Result<T> {
        return if(response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed invitations request: ${response.code()}"))
        }
    }

    suspend fun createInvitation(queuePlanId: UUID, req: InvitationRequest): Result<InvitationResponse> {
        val response = invitationsApi.createInvitation(queuePlanId, req)
        return returnResult(response)
    }

    suspend fun getAllByQueuePlan(queuePlanId: UUID): Result<List<InvitationResponse>> {
        val response = invitationsApi.getAllByQueuePlan(queuePlanId)
        return returnResult(response)
    }

    suspend fun deleteInvitation(queuePlanId: UUID, invitationId: UUID): Result<Unit> {
        val response = invitationsApi.deleteInvitation(queuePlanId, invitationId)
        return returnResult(response)
    }
}