package ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel

import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity

sealed class SubmissionRequestsState {
    data object Loading : SubmissionRequestsState()
    data class Error(val errorMessage: String) : SubmissionRequestsState()
    data class Main(
        val requests: List<SubmissionRequestEntity>,
        val queuePlans: List<QueuePlanShortEntity>,
        var activeRequest: SubmissionRequestEntity? = null,
        val inputBundle: SubmissionRequestInputBundle? = null
    ) : SubmissionRequestsState()
}