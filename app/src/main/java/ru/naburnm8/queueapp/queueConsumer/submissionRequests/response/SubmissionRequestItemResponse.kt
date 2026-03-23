package ru.naburnm8.queueapp.queueConsumer.submissionRequests.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class SubmissionRequestItemResponse(
    @Serializable(with = UUIDSerializer::class)
    val workTypeId: UUID,
    val workTypeName: String,
    val minutesPerOne: Int,
    val quantity: Int,
    val minutesOverride: Int?
)
