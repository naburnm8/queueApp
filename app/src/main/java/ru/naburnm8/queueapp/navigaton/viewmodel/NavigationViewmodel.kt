package ru.naburnm8.queueapp.navigaton.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.authorization.session.SessionManager
import ru.naburnm8.queueapp.authorization.entity.Role
import ru.naburnm8.queueapp.authorization.session.SessionRepository
import ru.naburnm8.queueapp.authorization.session.SessionState
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.websocket.QueueUpdatesManager

class NavigationViewmodel(
    private val sessionManager: SessionManager,
    private val sessionRepository: SessionRepository,
    private val queueUpdatesManager: QueueUpdatesManager
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<NavigationState>(NavigationState.Loading)

    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.logoutFlow.collect {
                invalidateSession()
                queueUpdatesManager.disconnect()
            }
        }
        viewModelScope.launch {
            sessionManager.loginFlow.collect {
                when (it) {
                    Role.ROLE_QCONSUMER -> authorizeAsQueueConsumer()
                    Role.ROLE_QOPERATOR -> authorizeAsQueueOperator()
                }
                queueUpdatesManager.connect()
            }
        }
        viewModelScope.launch {
            _stateFlow.value = when (sessionRepository.resolveSession()) {
                is SessionState.Unauthorized -> NavigationState.Unauthorized
                is SessionState.Teacher -> NavigationState.QueueOperator(QueueOperatorFlowNavigation.MyQueues)
                is SessionState.Student -> NavigationState.QueueConsumer(QueueConsumerFlowNavigation.MyQueue)
            }
            queueUpdatesManager.connect()
        }
        viewModelScope.launch {
            queueUpdatesManager.sessionExpired.collect {
                sessionManager.refresh()
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