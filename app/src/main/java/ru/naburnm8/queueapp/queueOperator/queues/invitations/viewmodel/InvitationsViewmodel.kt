package ru.naburnm8.queueapp.queueOperator.queues.invitations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueOperator.queues.invitations.entity.InvitationEntity
import ru.naburnm8.queueapp.queueOperator.queues.invitations.entity.InvitationsMapper
import ru.naburnm8.queueapp.queueOperator.queues.invitations.item.InvitationItem
import ru.naburnm8.queueapp.queueOperator.queues.invitations.repository.InvitationsRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import java.util.UUID

class InvitationsViewmodel (
    private val invitationsRepository: InvitationsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<InvitationsState>(InvitationsState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    private suspend fun loadInvitationsInner(queuePlan: QueuePlanEntity, onSuccess: () -> Unit = {}) {
        if (queuePlan.id == null) {
            return
        }
        runCatching {
            val invitations = invitationsRepository.getAllByQueuePlan(queuePlan.id).getOrThrow()
            val groups = profileRepository.getAllDistinctGroups().getOrThrow()
            _stateFlow.value = InvitationsState.Main(
                queuePlan,
                invitations.map { InvitationsMapper.map(it)},
                groups
            )
            onSuccess()
        }.onFailure {
            _stateFlow.value = InvitationsState.Error(it.message ?: "Unknown error")
        }
    }

    fun loadInvitations(queuePlan: QueuePlanEntity, onSuccess: () -> Unit = {}) {
        _stateFlow.value = InvitationsState.Loading
        viewModelScope.launch {
            loadInvitationsInner(queuePlan, onSuccess)
        }
    }

    fun deleteInvitation(queuePlanId: UUID, invitationId: UUID, onSuccess: () -> Unit = {}) {
        val previousState = _stateFlow.value
        if (previousState !is InvitationsState.Main) {
            return
        }
        _stateFlow.value = InvitationsState.Loading
        viewModelScope.launch {
            runCatching {
                invitationsRepository.deleteInvitation(queuePlanId, invitationId).getOrThrow()
                loadInvitationsInner(previousState.queuePlan, onSuccess)
            }.onFailure {
                _stateFlow.value = InvitationsState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun createInvitation(queuePlanId: UUID, new: InvitationEntity, onSuccess: () -> Unit = {}) {
        val previousState = _stateFlow.value
        if (previousState !is InvitationsState.Main) {
            return
        }
        _stateFlow.value = InvitationsState.Loading
        viewModelScope.launch {
            runCatching {
                invitationsRepository.createInvitation(queuePlanId, InvitationsMapper.toRequest(new)).getOrThrow()
                loadInvitationsInner(previousState.queuePlan, onSuccess)
            }.onFailure {
                _stateFlow.value = InvitationsState.Error(it.message ?: "Unknown error")
            }
        }
    }


}