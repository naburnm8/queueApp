package ru.naburnm8.queueapp.websocket

import java.util.UUID

data class QueueUpdateEvent(
    val queueId: UUID,
    val version: Long,
)
