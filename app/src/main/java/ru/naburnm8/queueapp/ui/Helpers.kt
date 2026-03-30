package ru.naburnm8.queueapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.naburnm8.queueapp.R

fun checkNotInBoundaries(value: String): Boolean {
    val doubleValue = value.toDoubleOrNull() ?: return true
    return doubleValue !in 0.0..1.0
}

fun filterDoubleInput(input: String): String {
    val filtered = input.filter { it.isDigit() || it == '.' || it == ',' }
    val normalized = filtered.replace(',', '.')
    val firstDotIndex = normalized.indexOf('.')

    return if (firstDotIndex == -1) {
        normalized
    } else {
        buildString {
            normalized.forEachIndexed { index, c ->
                if (c != '.' || index == firstDotIndex) {
                    append(c)
                }
            }
        }
    }
}

@Composable
fun Double.toImportanceReadableText(): String {
    return when (this) {
        in 0.0..0.33 -> stringResource(R.string.low)
        in 0.34..0.66 -> stringResource(R.string.medium)
        in 0.67..1.0 -> stringResource(R.string.high)
        else -> ""
    }
}

@Composable
fun Float.toImportanceReadableText(): String {
    return this.toDouble().toImportanceReadableText()
}