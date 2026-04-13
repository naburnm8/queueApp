package ru.naburnm8.queueapp.fake.api

import retrofit2.Response
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.api.QueueRulesApi
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.request.QueueRuleRequest
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.response.QueueRuleResponse
import java.util.UUID

class FakeQueueRulesApi : QueueRulesApi {

    val callList: MutableList<CallDescription> = mutableListOf()

    companion object {
        enum class CallName {
            GET_RULES
        }
    }

    override suspend fun addRule(
        queuePlanId: UUID,
        req: QueueRuleRequest
    ): Response<QueueRuleResponse> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun getRules(queuePlanId: UUID): Response<List<QueueRuleResponse>> {
        callList.add(
            CallDescription(
                CallName.GET_RULES.toString(),
                queuePlanId.toString()
            )
        )
        return Response.success(
            listOf()
        )
    }

    override suspend fun updateRule(
        queuePlanId: UUID,
        req: QueueRuleRequest
    ): Response<QueueRuleResponse> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun removeRule(
        queuePlanId: UUID,
        ruleId: UUID
    ): Response<Unit> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun enableRule(
        queuePlanId: UUID,
        ruleId: UUID
    ): Response<Unit> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun disableRule(
        queuePlanId: UUID,
        ruleId: UUID
    ): Response<Unit> {
        throw NotImplementedError("not used in viewmodel test case")
    }
}