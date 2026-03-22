package ru.naburnm8.queueapp.queueOperator.queues.invitations.entity

import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.queueOperator.queues.invitations.item.InvitationItem
import ru.naburnm8.queueapp.queueOperator.queues.invitations.request.InvitationRequest
import ru.naburnm8.queueapp.queueOperator.queues.invitations.response.InvitationResponse
import java.util.UUID

object InvitationsMapper {
    fun map(response: InvitationResponse): InvitationEntity {
        return InvitationEntity(
            id = response.id,
            queuePlanId = response.queuePlanId,
            enabled = response.enabled,
            code = response.code,
            targetGroup = response.targetGroup,
            targetStudents = response.targetStudents.map { ProfileMapper.map(it) },
            createdAt = response.createdAt,
            expiresAt = response.expiresAt,
            maxUses = response.maxUses,
            usedCount = response.usedCount
        )
    }

    fun toRequest(item: InvitationItem): InvitationRequest {
        return InvitationRequest(
            id = item.id,
            code = item.code,
            targetGroup = item.targetGroup,
            targetStudentIds = item.targetStudents.map { it.id },
            expiresAt = item.expiresAt,
            enabled = item.enabled,
            maxUses = item.maxUses
        )
    }

    fun toRequest(entity: InvitationEntity): InvitationRequest {
        return InvitationRequest(
            id = null,
            code = entity.code,
            targetGroup = entity.targetGroup,
            targetStudentIds = entity.targetStudents.map { it.id },
            expiresAt = entity.expiresAt,
            enabled = entity.enabled,
            maxUses = entity.maxUses
        )
    }
}