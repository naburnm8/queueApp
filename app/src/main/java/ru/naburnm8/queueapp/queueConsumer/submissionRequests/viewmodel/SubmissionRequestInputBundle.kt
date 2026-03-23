package ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.WorkTypeEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity

data class SubmissionRequestInputBundle(
    val queuePlan: QueuePlanShortEntity,
    val workTypes: List<WorkTypeEntity>,
    val me: ProfileEntity
)
