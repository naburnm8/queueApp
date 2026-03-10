package ru.naburnm8.queueapp.navigaton.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.authorization.SessionManager
import ru.naburnm8.queueapp.authorization.entity.Role
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

class NavigationViewmodel(
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<NavigationState>(NavigationState.Unauthorized)

    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.logoutFlow.collect {
                invalidateSession()
            }
        }
        viewModelScope.launch {
            sessionManager.loginFlow.collect {
                when (it) {
                    Role.QCONSUMER -> authorizeAsQueueConsumer()
                    Role.QOPERATOR -> authorizeAsQueueOperator()
                }
            }
        }
    }

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