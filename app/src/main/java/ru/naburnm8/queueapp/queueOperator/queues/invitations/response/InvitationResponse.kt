package ru.naburnm8.queueapp.queueOperator.queues.invitations.response

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.profile.response.StudentResponse
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.serialization.InstantSerializer
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.time.Instant
import java.util.UUID

@Serializable
data class InvitationResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val queuePlanId: UUID,
    val enabled: Boolean,
    val code: String? = null,
    val targetGroup: String? = null,
    val targetStudents: List<StudentResponse> = listOf(),

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,

    @Serializable(with = InstantSerializer::class)
    val expiresAt: Instant,
    val maxUses: Int,
    val usedCount: Int,
)
