package ru.naburnm8.queueapp.queueOperator.queues.queueRules.repository

import okio.IOException
import retrofit2.Response
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.api.QueueRulesApi
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.request.QueueRuleRequest
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.response.QueueRuleResponse
import java.util.UUID

class QueueRulesRepository (
    private val queueRulesApi: QueueRulesApi
) {
    private fun <T> returnResult(response: Response<T>): Result<T> {
        return if(response.isSuccessful) {
            if (response.body() == null) {
                return Result.failure(IllegalStateException("Response body is null"))
            }
            Result.success(response.body()!!)
        } else {
            Result.failure(IOException("Failed to add rule: ${response.code()}"))
        }
    }
    suspend fun addRule(
        queuePlanId: UUID,
        req: QueueRuleRequest
    ) : Result<QueueRuleResponse> {
        val response = queueRulesApi.addRule(queuePlanId, req)
        return returnResult(response)
    }

    suspend fun getRules(
        queuePlanId: UUID
    ) : Result<List<QueueRuleResponse>> {
        val response = queueRulesApi.getRules(queuePlanId)
        return returnResult(response)
    }

    suspend fun updateRule(
        queuePlanId: UUID,
        req: QueueRuleRequest
    ) : Result<QueueRuleResponse> {
        val response = queueRulesApi.updateRule(queuePlanId, req)
        return returnResult(response)
    }

    suspend fun removeRule(
        queuePlanId: UUID,
        ruleId: UUID
    ): Result<Unit> {
        val response = queueRulesApi.removeRule(queuePlanId, ruleId)
        return returnResult(response)
    }

    suspend fun enableRule(
        queuePlanId: UUID,
        ruleId: UUID
    ): Result<Unit> {
        val response = queueRulesApi.enableRule(queuePlanId, ruleId)
        return returnResult(response)
    }

    suspend fun disableRule(
        queuePlanId: UUID,
        ruleId: UUID
    ): Result<Unit> {
        val response = queueRulesApi.disableRule(queuePlanId, ruleId)
        return returnResult(response)
    }


}