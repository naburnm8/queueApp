package ru.naburnm8.queueapp.queueConsumer.submissionRequests.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class SubmissionRequestRequest(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID?,
    val items: List<SubmissionRequestItemRequest>,
    val inviteCode: String?
)
