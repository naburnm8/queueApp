package ru.naburnm8.queueapp.queueOperator.queues.entity

import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import java.time.Instant
import java.util.UUID

data class QueueSnapshotEntity(
    val queuePlanId: UUID,
    val version: Long,
    val generatedAt: Instant,
    val current: QueueEntryViewEntity?,
    val entries: List<QueueEntryViewEntity>,
    val queueStatus: QueueStatus
) {
    companion object {
        val empty = QueueSnapshotEntity(
            queuePlanId = UUID(0, 0),
            version = 0,
            generatedAt = Instant.EPOCH,
            current = null,
            entries = emptyList(),
            queueStatus = QueueStatus.EMPTY
        )
    }
}
