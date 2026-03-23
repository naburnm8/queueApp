package ru.naburnm8.queueapp.queueConsumer.submissionRequests.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class SubmissionRequestItemRequest(
    @Serializable(with = UUIDSerializer::class)
    val workTypeId: UUID,
    val quantity: Int = 1,
    val minutesOverride: Int? = null,
)
