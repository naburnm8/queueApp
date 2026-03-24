package ru.naburnm8.queueapp.queueOperator.queues.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.serialization.InstantSerializer
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.time.Instant
import java.util.UUID

@Serializable
data class QueueSnapshotResponse (
    @Serializable(with = UUIDSerializer::class)
    val queuePlanId: UUID,
    val version: Long,
    @Serializable(with = InstantSerializer::class)
    val generatedAt: Instant,
    val current: QueueEntryViewResponse?,
    val entries: List<QueueEntryViewResponse>
)