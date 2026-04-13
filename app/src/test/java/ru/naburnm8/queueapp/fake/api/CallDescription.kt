package ru.naburnm8.queueapp.fake.api

data class CallDescription(
    val method: String,
    val args: String,
) {
    override fun toString(): String {
        if (args.isBlank()) {
            return method
        }
        return "$method:$args"
    }
}
