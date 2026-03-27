package ru.naburnm8.queueapp.queueConsumer.submissionRequests.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class SubmissionRequestShortResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val queuePlanId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val studentId: UUID,
    val studentName: String,
    val avatarUrl: String? = null,
    val status: SubmissionStatus,
    val totalMinutes: Int,
)
