package ru.naburnm8.queueapp.authorization.viewmodel

sealed class AuthorizationState {
    data object Login : AuthorizationState()
    data object Register : AuthorizationState()
    data object Loading : AuthorizationState()
    data class Error(val message: String) : AuthorizationState()

}