package ru.naburnm8.queueapp.queueOperator.queues.invitations.request

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.serialization.InstantSerializer
import ru.naburnm8.queueapp.serialization.UUIDSerializer
import java.time.Instant
import java.util.UUID

@Serializable
data class InvitationRequest(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val code: String? = null,
    val targetGroup: String? = null,
    val targetStudentIds: List<@Serializable(with = UUIDSerializer::class) UUID>? = null,
    @Serializable(with = InstantSerializer::class)
    val expiresAt: Instant = Instant.MAX,
    val enabled: Boolean = true,
    val maxUses: Int = Int.MAX_VALUE,
)
