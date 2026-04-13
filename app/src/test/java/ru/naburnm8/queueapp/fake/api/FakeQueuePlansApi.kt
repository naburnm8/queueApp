package ru.naburnm8.queueapp.fake.api

import retrofit2.Response
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.api.QueuePlansApi
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueuePlanRequest
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.response.QueuePlanResponse
import java.time.Instant
import java.util.UUID

//getMyQueuePlans
//getQueuePlanById


class FakeQueuePlansApi : QueuePlansApi{

    companion object {
        enum class CallName {
            GET_MY_QUEUE_PLANS, GET_QUEUE_PLAN_BY_ID, OTHER
        }
    }

    val callList: MutableList<CallDescription> = mutableListOf()

    override suspend fun createQueuePlan(
        disciplineId: UUID,
        req: QueuePlanRequest
    ): Response<QueuePlanResponse> {
        throw NotImplementedError("not used in viewmodel testcase")
    }

    override suspend fun getQueuePlansByDiscipline(disciplineId: UUID): Response<List<QueuePlanResponse>> {
        throw NotImplementedError("not used in viewmodel testcase")
    }

    override suspend fun updateQueuePlan(
        disciplineId: UUID,
        req: QueuePlanRequest
    ): Response<QueuePlanResponse> {
        throw NotImplementedError("not used in viewmodel testcase")
    }

    override suspend fun getMyQueuePlans(): Response<List<QueuePlanResponse>> {
        callList.add(CallDescription(
            CallName.GET_MY_QUEUE_PLANS.toString(),
            ""
        ))
        return Response.success(
            listOf(
                QueuePlanResponse(
                    id = UUID(1,1),
                    disciplineId = UUID(1,1),
                    createdByTeacherId = UUID(1,1),
                    title = "Discipline",
                    status = QueueStatus.DRAFT,
                    useDebts = true,
                    wDebts = 1.0,
                    useTime = true,
                    wTime = 1.0,
                    useAchievements = true,
                    wAchievements = 1.0,
                    createdAt = Instant.now(),
                    slotDurationMinutes = 15
                )
            )
        )
    }

    override suspend fun getQueuePlanById(queuePlanId: UUID): Response<QueuePlanResponse> {
        callList.add(CallDescription(
            CallName.GET_QUEUE_PLAN_BY_ID.toString(),
            queuePlanId.toString()
        ))
        return Response.success(
            QueuePlanResponse(
                id = UUID(1,1),
                disciplineId = UUID(1,1),
                createdByTeacherId = UUID(1,1),
                title = "Discipline",
                status = QueueStatus.DRAFT,
                useDebts = true,
                wDebts = 1.0,
                useTime = true,
                wTime = 1.0,
                useAchievements = true,
                wAchievements = 1.0,
                createdAt = Instant.now(),
                slotDurationMinutes = 15
            )
        )
    }

    override suspend fun activate(queuePlanId: UUID): Response<Unit> {
        throw NotImplementedError("not used in viewmodel testcase")
    }

    override suspend fun close(queuePlanId: UUID): Response<Unit> {
        throw NotImplementedError("not used in viewmodel testcase")
    }

    override suspend fun deletePlan(
        disciplineId: UUID,
        queuePlanId: UUID
    ): Response<Unit> {
        throw NotImplementedError("not used in viewmodel testcase")
    }
}