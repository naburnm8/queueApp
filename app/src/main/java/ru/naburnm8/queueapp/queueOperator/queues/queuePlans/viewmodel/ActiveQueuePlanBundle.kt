package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.queues.invitations.entity.InvitationEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity

data class ActiveQueuePlanBundle(
    val activePlan: QueuePlanEntity,
    val teacher: ProfileEntity,
    val discipline: DisciplineEntity,
    val invitations: List<InvitationEntity>
)
