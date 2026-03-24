package ru.naburnm8.queueapp.queueOperator.queues.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID
@Serializable
data class QueueEntryViewResponse(
    val place: Int,
    @Serializable(with = UUIDSerializer::class)
    val requestId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val studentId: UUID,
    val studentName: String,
    val studentAvatarUrl: String? = null,
    val totalMinutes: Int,
    val priority: Double,
    val status: SubmissionStatus,
)
