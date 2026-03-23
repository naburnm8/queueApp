package ru.naburnm8.queueapp.queueConsumer.submissionRequests.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.serialization.InstantSerializer
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.time.Instant
import java.util.UUID

@Serializable
data class SubmissionRequestResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val queuePlanId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val studentId: UUID,
    val status: SubmissionStatus,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant,
    val totalMinutes: Int,
    val items: List<SubmissionRequestItemResponse>
)
