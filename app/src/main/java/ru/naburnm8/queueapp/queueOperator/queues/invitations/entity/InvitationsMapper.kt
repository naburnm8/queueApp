package ru.naburnm8.queueapp.queueOperator.queues.invitations.entity

import ru.naburnm8.queueapp.queueOperator.queues.invitations.request.InvitationRequest
import ru.naburnm8.queueapp.queueOperator.queues.invitations.response.InvitationResponse

object InvitationsMapper {
    fun map(response: InvitationResponse): InvitationEntity {
        return InvitationEntity(
            id = response.id,
            queuePlanId = response.queuePlanId,
            enabled = response.enabled,
            code = response.code,
            targetGroup = response.targetGroup,
            targetStudents = response.targetStudents,
            createdAt = response.createdAt,
            expiresAt = response.expiresAt,
            maxUses = response.maxUses,
            usedCount = response.usedCount
        )
    }

    fun toRequest(entity: InvitationEntity): InvitationRequest {
        return InvitationRequest(
            id = entity.id,
            code = entity.code,
            targetGroup = entity.targetGroup,
            targetStudentIds = entity.targetStudents.map { it.id },
            expiresAt = entity.expiresAt,
            enabled = entity.enabled,
            maxUses = entity.maxUses
        )
    }
}