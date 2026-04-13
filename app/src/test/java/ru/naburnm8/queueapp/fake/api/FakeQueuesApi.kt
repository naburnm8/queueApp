package ru.naburnm8.queueapp.fake.api

import retrofit2.Response
import ru.naburnm8.queueapp.queueOperator.queues.api.QueuesApi
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.queueOperator.queues.response.QueueSnapshotResponse
import java.time.Instant
import java.util.UUID


class FakeQueuesApi: QueuesApi {
    companion object {
        enum class CallName {
            VIEW, TAKE_NEXT, TAKE
        }
    }
    val callList: MutableList<CallDescription> = mutableListOf()

    override suspend fun view(
        queuePlanId: UUID,
        force: Boolean
    ): Response<QueueSnapshotResponse> {
        callList.add(
            CallDescription(
                CallName.VIEW.toString(),
                queuePlanId.toString()
            )
        )
        return Response.success(
            QueueSnapshotResponse(
                queuePlanId = UUID(1,1),
                version = 16L,
                generatedAt = Instant.now(),
                current = null,
                entries = listOf(),
                queueStatus = QueueStatus.DRAFT
            )
        )
    }

    override suspend fun takeNext(queuePlanId: UUID): Response<Unit> {
        callList.add(
            CallDescription(
                CallName.TAKE_NEXT.toString(),
                queuePlanId.toString()
            )
        )
        return Response.success(Unit)
    }

    override suspend fun take(
        queuePlanId: UUID,
        requestId: UUID
    ): Response<Unit> {
        callList.add(
            CallDescription(
                CallName.TAKE.toString(),
                queuePlanId.toString()
            )
        )
        return Response.success(Unit)
    }

}