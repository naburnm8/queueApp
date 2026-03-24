package ru.naburnm8.queueapp.queueOperator.queues.entity

import java.time.Instant
import java.util.UUID

data class QueueSnapshotEntity(
    val queuePlanId: UUID,
    val version: Long,
    val generatedAt: Instant,
    val current: QueueEntryViewEntity?,
    val entries: List<QueueEntryViewEntity>
)
