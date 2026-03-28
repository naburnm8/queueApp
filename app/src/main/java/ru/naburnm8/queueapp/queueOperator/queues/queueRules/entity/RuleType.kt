package ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.naburnm8.queueapp.R

enum class RuleType {
    GROUP_BONUS,
    IDENTIFIER_BONUS,
    TIMESTAMP_BONUS,
    CUSTOM,
}

@Composable
fun RuleType.toReadableText(): String {
    return when (this) {
        RuleType.GROUP_BONUS -> stringResource(R.string.group_name_bonus)
        RuleType.IDENTIFIER_BONUS -> stringResource(R.string.identifier_bonus)
        RuleType.TIMESTAMP_BONUS -> stringResource(R.string.timestamp_bonus)
        RuleType.CUSTOM -> stringResource(R.string.custom_bonus)
    }
}