package ru.naburnm8.queueapp.queueOperator.queues.entity

import ru.naburnm8.queueapp.queueOperator.queues.response.QueueEntryViewResponse
import ru.naburnm8.queueapp.queueOperator.queues.response.QueueSnapshotResponse

object QueueMapper {
    fun map(response: QueueSnapshotResponse): QueueSnapshotEntity {
        return QueueSnapshotEntity(
            queuePlanId = response.queuePlanId,
            version = response.version,
            generatedAt = response.generatedAt,
            current = if (response.current != null) map(response.current) else null,
            entries = response.entries.map { map(it) }
        )
    }

    fun map(response: QueueEntryViewResponse): QueueEntryViewEntity {
        return QueueEntryViewEntity(
            place = response.place,
            requestId = response.requestId,
            studentId = response.studentId,
            studentName = response.studentName,
            studentAvatarUrl = response.studentAvatarUrl,
            totalMinutes = response.totalMinutes,
            priority = response.priority,
            status = response.status
        )
    }

}