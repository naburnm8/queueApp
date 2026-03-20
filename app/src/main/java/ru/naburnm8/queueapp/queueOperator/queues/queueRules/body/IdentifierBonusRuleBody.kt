package ru.naburnm8.queueapp.queueOperator.queues.queueRules.body

import kotlinx.serialization.Serializable

@Serializable
data class IdentifierBonusRuleBody(
    val field: IdentifierField,
    val values: List<String>,
    val bonus: Double
)
