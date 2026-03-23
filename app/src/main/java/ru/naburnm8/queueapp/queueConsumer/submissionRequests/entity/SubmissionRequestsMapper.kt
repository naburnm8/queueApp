package ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity

import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestItemResponse
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionRequestResponse

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

    fun map(response: SubmissionRequestResponse): SubmissionRequestEntity {
        return SubmissionRequestEntity(
            id = response.id,
            queuePlanId = response.id,
            studentId = response.studentId,
            status = response.status,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt,
            totalMinutes = response.totalMinutes,
            items = response.items.map {map(it)}
        )
    }
}