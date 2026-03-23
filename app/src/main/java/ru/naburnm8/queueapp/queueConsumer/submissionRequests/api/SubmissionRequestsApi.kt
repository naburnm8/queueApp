package ru.naburnm8.queueapp.queueConsumer.submissionRequests.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.request.SubmissionRequestRequest
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestResponse
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import java.util.UUID

interface SubmissionRequestsApi {
    companion object {
        const val URI_BASE = "/api/queuePlans"
    }

    @GET("$URI_BASE/requests/my")
    suspend fun getAllMySubmissionRequests(

    ) : Response<List<SubmissionRequestResponse>>

    @GET("$URI_BASE/{queuePlanId}/requests/my")
    suspend fun getMySubmissionRequest(
        @Path("queuePlanId") queuePlanId: UUID
    ): Response<SubmissionRequestResponse>

    @POST("$URI_BASE/{queuePlanId}/requests/my")
    suspend fun createMySubmissionRequest(
        @Path("queuePlanId") queuePlanId: UUID,
        @Body req: SubmissionRequestRequest
    ) : Response<SubmissionRequestResponse>

    @PUT("$URI_BASE/{queuePlanId}/requests/my")
    suspend fun updateMySubmissionRequest(
        @Path("queuePlanId") queuePlanId: UUID,
        @Body req: SubmissionRequestRequest
    ) : Response<SubmissionRequestResponse>

    @DELETE("$URI_BASE/{queuePlanId}/requests/my")
    suspend fun deleteMySubmissionRequest(
        @Path("queuePlanId") queuePlanId: UUID
    ): Response<Unit>

    @GET("$URI_BASE/{queuePlanId}/requests")
    suspend fun getAllSubmissionRequests(
        @Path("queuePlanId") queuePlanId: UUID,
        @Query("status") status: SubmissionStatus? = null
    ) : Response<List<SubmissionRequestResponse>>

    @PUT("$URI_BASE/{queuePlanId}/requests/{requestId}/status")
    suspend fun updateSubmissionRequestStatus(
        @Path("queuePlanId") queuePlanId: UUID,
        @Path("requestId") requestId: UUID,
        @Query("status") status: SubmissionStatus
    ) : Response<Unit>

}