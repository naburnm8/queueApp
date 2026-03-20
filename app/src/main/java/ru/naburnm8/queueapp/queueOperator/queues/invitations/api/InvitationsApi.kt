package ru.naburnm8.queueapp.queueOperator.queues.invitations.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.naburnm8.queueapp.queueOperator.queues.invitations.request.InvitationRequest
import ru.naburnm8.queueapp.queueOperator.queues.invitations.response.InvitationResponse
import java.util.UUID

interface InvitationsApi {

    companion object {
        const val URI_BASE = "/api/queuePlans"
    }

    @POST("$URI_BASE/{queuePlanId}/invitations")
    suspend fun createInvitation(
        @Path("queuePlanId") queuePlanId: UUID,
        @Body req: InvitationRequest
    ) : Response<InvitationResponse>


    @GET("$URI_BASE/{queuePlanId}/invitations")
    suspend fun getAllByQueuePlan(@Path("queuePlanId") queuePlanId: UUID) : Response<List<InvitationResponse>>

    @DELETE("$URI_BASE/{queuePlanId}/invitations/{invitationId}")
    suspend fun deleteInvitation(@Path("queuePlanId") queuePlanId: UUID, @Path("invitationId") invitationId: UUID): Response<Unit>
}