package ru.naburnm8.queueapp.navigaton.viewmodel

import kotlinx.serialization.Serializable
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation


sealed class NavigationState {
    data class QueueConsumer (
        val route: QueueConsumerFlowNavigation
    ) : NavigationState ()

    data class QueueOperator (
        val route: QueueOperatorFlowNavigation
    ) : NavigationState()

    data object Unauthorized : NavigationState()

    data object Loading : NavigationState()

}