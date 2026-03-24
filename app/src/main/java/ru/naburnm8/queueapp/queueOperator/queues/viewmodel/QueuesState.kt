package ru.naburnm8.queueapp.queueOperator.queues.viewmodel

import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity

sealed class QueuesState {
    data object Loading : QueuesState()
    data class Error(val errorMessage: String) : QueuesState()

    data class Main(
        val queues: List<QueueSnapshotEntity>
    ) : QueuesState()
}