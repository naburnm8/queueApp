package ru.naburnm8.queueapp.navigaton.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewmodel : ViewModel() {
    private val _stateFlow = MutableStateFlow<NavigationState>(NavigationState.Unauthorized)

    val stateFlow = _stateFlow.asStateFlow()

    fun changeState(nextState: NavigationState) {
        _stateFlow.value = nextState
    }

    fun invalidateSession() {
        _stateFlow.value = NavigationState.Unauthorized
    }
}