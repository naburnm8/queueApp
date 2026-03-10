package ru.naburnm8.queueapp.authorization.entity

data class RoleEntity(
    val identifier: Role,
    val displayName: String,
    val displayNameRu: String? = null
)
