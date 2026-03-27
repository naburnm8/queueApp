package ru.naburnm8.queueapp.queueOperator.queues.viewmodel

import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestShortEntity
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity

sealed class QueuesState {
    data object Loading : QueuesState()
    data class Error(val errorMessage: String) : QueuesState()

    data class Main(
        val queues: List<QueueSnapshotEntity>,
        val queuePlans: List<QueuePlanEntity>,
        val submissionRequestsToQueues: Map<QueueSnapshotEntity, List<SubmissionRequestShortEntity>>
    ) : QueuesState()
}