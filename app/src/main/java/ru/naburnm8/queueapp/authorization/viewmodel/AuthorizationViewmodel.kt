package ru.naburnm8.queueapp.authorization.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class AuthorizationViewmodel : ViewModel() {
    private val _stateFlow = MutableStateFlow<AuthorizationState>(AuthorizationState.Login)

    val stateFlow = _stateFlow.asStateFlow()

    fun proceedToRegistration() {
        _stateFlow.value = AuthorizationState.Register
    }

    fun proceedToLogin() {
        _stateFlow.value = AuthorizationState.Login
    }

    fun tryLogin(email: String, password: String) {


    }

    fun tryRegisterStudent() {

    }
}