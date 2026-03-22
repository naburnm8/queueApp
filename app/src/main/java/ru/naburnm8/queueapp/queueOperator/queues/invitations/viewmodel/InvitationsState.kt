package ru.naburnm8.queueapp.queueOperator.queues.invitations.viewmodel

import ru.naburnm8.queueapp.queueOperator.queues.invitations.entity.InvitationEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity

sealed class InvitationsState {
    data object Loading : InvitationsState()
    data class Error(val errorMessage: String): InvitationsState()
    data class Main(
        val queuePlan: QueuePlanEntity,
        val invitations: List<InvitationEntity>,
        val distinctGroups: List<String>
    ) : InvitationsState()
}