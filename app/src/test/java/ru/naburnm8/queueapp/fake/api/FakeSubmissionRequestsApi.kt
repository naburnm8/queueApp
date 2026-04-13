package ru.naburnm8.queueapp.fake.api

import retrofit2.Response
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.api.SubmissionRequestsApi
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.request.SubmissionRequestRequest
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestResponse
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestShortResponse
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import java.time.Instant
import java.util.UUID

class FakeSubmissionRequestsApi : SubmissionRequestsApi {

    companion object {
        enum class CallName {
            GET_ALL_SHORT, GET_ALL_MY, GET_MY
        }

    }

    val callList: MutableList<CallDescription> = mutableListOf()

    override suspend fun leaveQueue(queuePlanId: UUID): Response<Unit> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun getAllMySubmissionRequests(): Response<List<SubmissionRequestResponse>> {
        callList.add(
            CallDescription(
                CallName.GET_ALL_MY.toString(),
                ""
            )
        )
        return Response.success(
            listOf(
                SubmissionRequestResponse(
                    id = UUID(1,1),
                    queuePlanId = UUID(1,1),
                    studentId = UUID(1,2),
                    status = SubmissionStatus.ENQUEUED,
                    createdAt = Instant.now().minusSeconds(3600),
                    updatedAt = Instant.now(),
                    totalMinutes = 0,
                    items = listOf()
                )
            )
        )
    }

    override suspend fun getMySubmissionRequest(queuePlanId: UUID): Response<SubmissionRequestResponse> {
        callList.add(
            CallDescription(
                CallName.GET_MY.toString(),
                queuePlanId.toString()
            )
        )
        return Response.success(
            SubmissionRequestResponse(
                id = UUID(1,1),
                queuePlanId = UUID(1,1),
                studentId = UUID(1,2),
                status = SubmissionStatus.ENQUEUED,
                createdAt = Instant.now().minusSeconds(3600),
                updatedAt = Instant.now(),
                totalMinutes = 0,
                items = listOf()
            )
        )
    }

    override suspend fun createMySubmissionRequest(
        queuePlanId: UUID,
        req: SubmissionRequestRequest
    ): Response<SubmissionRequestResponse> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun updateMySubmissionRequest(
        queuePlanId: UUID,
        req: SubmissionRequestRequest
    ): Response<SubmissionRequestResponse> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun deleteMySubmissionRequest(queuePlanId: UUID): Response<Unit> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun getAllSubmissionRequests(
        queuePlanId: UUID,
        status: SubmissionStatus?
    ): Response<List<SubmissionRequestResponse>> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun updateSubmissionRequestStatus(
        queuePlanId: UUID,
        requestId: UUID,
        status: SubmissionStatus
    ): Response<Unit> {
        throw NotImplementedError("not used in viewmodel test case")
    }

    override suspend fun getAllSubmissionRequestsShort(queuePlanId: UUID): Response<List<SubmissionRequestShortResponse>> {
        callList.add(
            CallDescription(
                CallName.GET_ALL_SHORT.toString(),
                queuePlanId.toString()
            )
        )
        return Response.success(
            listOf()
        )
    }
}