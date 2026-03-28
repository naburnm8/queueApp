package ru.naburnm8.queueapp.queueConsumer.submissionRequests.response

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import ru.naburnm8.queueapp.R

enum class SubmissionStatus {
    PENDING,
    ENQUEUED,
    REJECTED,
    DEQUEUED
}

@Composable
fun SubmissionStatus.toReadableText() : String {
    return when(this) {
        SubmissionStatus.PENDING -> stringResource(R.string.status_pending).toLowerCase(Locale.current)
        SubmissionStatus.ENQUEUED -> stringResource(R.string.status_enqueued).toLowerCase(Locale.current)
        SubmissionStatus.REJECTED -> stringResource(R.string.status_rejected).toLowerCase(Locale.current)
        SubmissionStatus.DEQUEUED -> stringResource(R.string.status_dequeued).toLowerCase(Locale.current)
    }
}