package ru.naburnm8.queueapp.queueOperator.queues.entity

import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import java.util.UUID

data class QueueEntryViewEntity(
    val place: Int,
    val requestId: UUID,
    val studentId: UUID,
    val studentName: String,
    val studentAvatarUrl: String? = null,
    val totalMinutes: Int,
    val priority: Double,
    val status: SubmissionStatus,
) {
    companion object {
        val mock = QueueEntryViewEntity(
            place = 0,
            requestId = UUID(1, 1),
            studentId = UUID(2, 2),
            studentName = "Шлянтяев Семён Владимирович",
            studentAvatarUrl = null,
            totalMinutes = 15,
            priority = 0.5,
            status = SubmissionStatus.ENQUEUED
        )

        val mockList = generateMocks(10)
        val mockHead = QueueEntryViewEntity(
            place = -1,
            requestId = UUID(2, 3),
            studentId = UUID(5, 2),
            studentName = "Цапков Михаил Андреевич",
            studentAvatarUrl = null,
            totalMinutes = 15,
            priority = 0.5,
            status = SubmissionStatus.DEQUEUED
        )
        
        private fun generateMocks(n: Int): List<QueueEntryViewEntity> {
            val mocks = (1..n).map {
                QueueEntryViewEntity(
                    place = it,
                    requestId = UUID(it.toLong(), it.toLong()),
                    studentId = UUID((it + 1).toLong(), (it + 1).toLong()),
                    studentName = "Студент $it",
                    studentAvatarUrl = null,
                    totalMinutes = 15,
                    priority = 0.5,
                    status = SubmissionStatus.ENQUEUED
                )
            }
            return mocks
        }
    }
}
