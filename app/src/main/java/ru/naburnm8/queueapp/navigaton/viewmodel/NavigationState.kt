package ru.naburnm8.queueapp.navigaton.viewmodel

sealed class NavigationState {
    sealed class QueueConsumer : NavigationState () {
        data object MyQueue : QueueConsumer()
        data object MyRequests : QueueConsumer()
        data object MyProfile : QueueConsumer()
    }

    sealed class QueueOperator : NavigationState() {
        data object MyQueues : QueueOperator()
        data object MySettings : QueueOperator()
        data object MyProfile : QueueOperator()
    }

    data object Unauthorized : NavigationState()

}