package ru.naburnm8.queueapp.queueOperator.discipline.entity

import java.util.UUID

data class WorkTypeEntity(
    val id: UUID? = null,
    val name: String,
    val estimatedTimeMinutes: Int,
) {
    companion object {
        val mock = WorkTypeEntity(
            id = UUID(0,0),
            name = "Лабораторная работа 1",
            estimatedTimeMinutes = 15,
        )
    }
}
