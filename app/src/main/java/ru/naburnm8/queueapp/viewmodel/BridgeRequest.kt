package ru.naburnm8.queueapp.viewmodel

data class BridgeRequest(
    val senderId: String,
    val recipientId: String,
    val method: String,
)
