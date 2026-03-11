package ru.naburnm8.queueapp.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class InterViewmodelBridge {
    private val _messageFlow = MutableSharedFlow<BridgeRequest>()
    val messageFlow = _messageFlow.asSharedFlow()

    suspend fun sendMessage(request: BridgeRequest) {
        _messageFlow.emit(request)
    }
}