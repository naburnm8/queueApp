package ru.naburnm8.queueapp.navigaton.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

class NavigationViewmodel : ViewModel() {
    private val _stateFlow = MutableStateFlow<NavigationState>(NavigationState.Unauthorized)

    val stateFlow = _stateFlow.asStateFlow()

    fun changeState(nextState: NavigationState) {
        _stateFlow.value = nextState
    }

    fun authorizeAsQueueConsumer() {
        _stateFlow.value = NavigationState.QueueConsumer(QueueConsumerFlowNavigation.MyQueue)
    }

    fun authorizeAsQueueOperator() {
        _stateFlow.value = NavigationState.QueueOperator(QueueOperatorFlowNavigation.MyQueues)
    }


    fun invalidateSession() {
        _stateFlow.value = NavigationState.Unauthorized
    }
}