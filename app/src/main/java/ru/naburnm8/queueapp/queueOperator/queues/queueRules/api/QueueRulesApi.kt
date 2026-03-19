package ru.naburnm8.queueapp.queueOperator.queues.queueRules.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.request.QueueRuleRequest
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.response.QueueRuleResponse
import java.util.UUID

interface QueueRulesApi {
    companion object {
        const val URI_BASE = "/api/queuePlans"
    }

    @POST("$URI_BASE/{queuePlanId}/rules")
    suspend fun addRule(@Path("queuePlanId") queuePlanId: UUID, @Body req: QueueRuleRequest): Response<QueueRuleResponse>

    @GET("$URI_BASE/{queuePlanId}/rules")
    suspend fun getRules(@Path("queuePlanId") queuePlanId: UUID): Response<List<QueueRuleResponse>>

    @PUT("$URI_BASE/{queuePlanId}/rules")
    suspend fun updateRule(@Path("queuePlanId") queuePlanId: UUID, @Body req: QueueRuleRequest): Response<QueueRuleResponse>

    @DELETE("$URI_BASE/{queuePlanId}/rules/{ruleId}")
    suspend fun removeRule(
        @Path("queuePlanId") queuePlanId: UUID,
        @Path("ruleId") ruleId: UUID,
    ): Response<Unit>

    @POST("$URI_BASE/{queuePlanId}/rules/{ruleId}/enable")
    suspend fun enableRule(
        @Path("queuePlanId") queuePlanId: UUID,
        @Path("ruleId") ruleId: UUID,
    ): Response<Unit>

    @POST("$URI_BASE/{queuePlanId}/rules/{ruleId}/disable")
    suspend fun disableRule(
        @Path("queuePlanId") queuePlanId: UUID,
        @Path("ruleId") ruleId: UUID,
    ): Response<Unit>





}