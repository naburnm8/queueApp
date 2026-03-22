package ru.naburnm8.queueapp.queueOperator.queues.invitations.item

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import java.time.Instant
import java.util.UUID

data class InvitationItem(
    val id: UUID,
    val enabled: Boolean,
    val code: String? = null,
    val targetGroup: String? = null,
    val targetStudents: List<ProfileEntity> = listOf(),
    val createdAt: Instant,
    val expiresAt: Instant,
    val maxUses: Int,
    val usedCount: Int,
)
