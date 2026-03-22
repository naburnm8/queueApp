package ru.naburnm8.queueapp.ui

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