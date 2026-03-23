package ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity

import java.util.UUID

data class SubmissionRequestItemEntity (
    val workTypeId: UUID,
    val workTypeName: String,
    val minutesPerOne: Int,
    val quantity: Int,
    val minutesOverride: Int?
)