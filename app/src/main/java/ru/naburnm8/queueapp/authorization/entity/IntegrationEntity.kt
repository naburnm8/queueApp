package ru.naburnm8.queueapp.authorization.entity

import java.util.UUID

data class IntegrationEntity(
    val id: UUID,
    val name: String,
    val payload: String
)
