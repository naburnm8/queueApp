package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.naburnm8.queueapp.R

enum class QueueStatus {
    DRAFT,
    ACTIVE,
    CLOSED,
    EMPTY
}

@Composable
fun QueueStatus.toReadableText(): String {
    return when (this) {
        QueueStatus.DRAFT -> stringResource(R.string.draft)
        QueueStatus.ACTIVE -> stringResource(R.string.active)
        QueueStatus.CLOSED -> stringResource(R.string.closed)
        QueueStatus.EMPTY -> stringResource(R.string.empty)
    }
}