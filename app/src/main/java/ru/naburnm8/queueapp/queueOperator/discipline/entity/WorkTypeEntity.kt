package ru.naburnm8.queueapp.queueOperator.discipline.entity

import java.util.UUID

data class WorkTypeEntity(
    val id: UUID? = null,
    val name: String,
    val estimatedTimeMinutes: Int,
)
