package ru.naburnm8.queueapp.queueOperator.queues.entity

import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import java.util.UUID

data class QueueEntryViewEntity(
    val place: Int,
    val requestId: UUID,
    val studentId: UUID,
    val studentName: String,
    val studentAvatarUrl: String? = null,
    val totalMinutes: Int,
    val priority: Double,
    val status: SubmissionStatus,
)
