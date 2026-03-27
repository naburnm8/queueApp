package ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity

import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import java.util.UUID

data class SubmissionRequestShortEntity(
    val id: UUID,
    val queuePlanId: UUID,
    val studentId: UUID,
    val studentName: String,
    val avatarUrl: String? = null,
    val status: SubmissionStatus,
    val totalMinutes: Int,
) {
    companion object {
        val mock = SubmissionRequestShortEntity(
            id = UUID.randomUUID(),
            queuePlanId = UUID.randomUUID(),
            studentId = UUID.randomUUID(),
            studentName = "Иван Иванов Иванович",
            avatarUrl = null,
            status = SubmissionStatus.PENDING,
            totalMinutes = 15
        )
    }
}
