package ru.naburnm8.queueapp.queueConsumer.queue.viewmodel

import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity

sealed class QueueState {
    data object Loading : QueueState()
    data class Error(val errorMessage: String) : QueueState()

    data class Main(
        val queues: List<QueueSnapshotEntity>,
        val queuePlans: List<QueuePlanShortEntity>,
        val myRequestByQueue: Map<QueueSnapshotEntity, SubmissionRequestEntity>,
        val currentQueueRules: List<QueueRuleEntity>? = null
    ) : QueueState()
}