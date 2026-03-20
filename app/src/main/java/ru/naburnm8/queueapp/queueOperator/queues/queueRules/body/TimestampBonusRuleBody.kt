package ru.naburnm8.queueapp.queueOperator.queues.queueRules.body

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class TimestampBonusRuleBody(
    @Serializable(with = OffsetDateTimeSerializer::class)
    val begin: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val end: OffsetDateTime,
    val bonus: Double
)
