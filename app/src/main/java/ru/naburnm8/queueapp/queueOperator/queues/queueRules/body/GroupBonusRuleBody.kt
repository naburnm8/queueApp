package ru.naburnm8.queueapp.queueOperator.queues.queueRules.body

import kotlinx.serialization.Serializable


@Serializable
data class GroupBonusRuleBody(
    val groups: List<String>,
    val bonus: Double
)
