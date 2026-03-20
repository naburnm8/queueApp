package ru.naburnm8.queueapp.queueOperator.queues.invitations.entity

import ru.naburnm8.queueapp.profile.response.StudentResponse
import java.time.Instant
import java.util.UUID

data class InvitationEntity(
    val id: UUID,
    val queuePlanId: UUID,
    val enabled: Boolean,
    val code: String? = null,
    val targetGroup: String? = null,
    val targetStudents: List<StudentResponse> = listOf(),
    val createdAt: Instant,
    val expiresAt: Instant,
    val maxUses: Int,
    val usedCount: Int,
)
