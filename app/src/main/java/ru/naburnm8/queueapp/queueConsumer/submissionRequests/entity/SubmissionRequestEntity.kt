package ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity

import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import java.time.Instant
import java.util.UUID

data class SubmissionRequestEntity(
    val id: UUID,
    val queuePlanId: UUID,
    val studentId: UUID,
    val status: SubmissionStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val totalMinutes: Int,
    val items: List<SubmissionRequestItemEntity>
) {
    fun countOverallTime(): Int {
        return items.sumOf {it.minutesPerOne * it.quantity}
    }

    companion object {
        val mock = SubmissionRequestEntity(
            id = UUID(0,0),
            queuePlanId = UUID(0,0),
            studentId = UUID(0,0),
            status = SubmissionStatus.PENDING,
            createdAt = Instant.now().minusSeconds(9000),
            updatedAt = Instant.now().minusSeconds(3000),
            totalMinutes = 15,
            items = listOf(SubmissionRequestItemEntity(
                workTypeId = UUID(0,0),
                workTypeName = "Лаб 1",
                minutesPerOne = 15,
                quantity = 1,
                minutesOverride = null,
            ))
        )
    }
}
