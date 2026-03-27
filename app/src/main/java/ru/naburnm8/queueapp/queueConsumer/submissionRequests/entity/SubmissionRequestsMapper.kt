package ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity

import ru.naburnm8.queueapp.queueConsumer.submissionRequests.request.SubmissionRequestItemRequest
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.request.SubmissionRequestRequest
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestItemResponse
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestResponse
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestShortResponse

object SubmissionRequestsMapper {
    fun map(response: SubmissionRequestItemResponse): SubmissionRequestItemEntity {
        return SubmissionRequestItemEntity(
            workTypeId = response.workTypeId,
            workTypeName = response.workTypeName,
            minutesPerOne = response.minutesPerOne,
            quantity = response.quantity,
            minutesOverride = response.minutesOverride
        )
    }

    fun map(response: SubmissionRequestShortResponse): SubmissionRequestShortEntity {
        return SubmissionRequestShortEntity(
            id = response.id,
            queuePlanId = response.queuePlanId,
            studentId = response.studentId,
            studentName = response.studentName,
            avatarUrl = response.avatarUrl,
            status = response.status,
            totalMinutes = response.totalMinutes
        )
    }

    fun map(response: SubmissionRequestResponse): SubmissionRequestEntity {
        return SubmissionRequestEntity(
            id = response.id,
            queuePlanId = response.queuePlanId,
            studentId = response.studentId,
            status = response.status,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt,
            totalMinutes = response.totalMinutes,
            items = response.items.map {map(it)}
        )
    }

    fun toRequest(entity: SubmissionRequestEntity, inviteCode: String? = null): SubmissionRequestRequest {
        return SubmissionRequestRequest(
            id = entity.id,
            inviteCode = inviteCode,
            items = entity.items.map {
                SubmissionRequestItemRequest(
                    workTypeId = it.workTypeId,
                    quantity = it.quantity,
                    minutesOverride = it.minutesOverride
                )
            }
        )
    }
}