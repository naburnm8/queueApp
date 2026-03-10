package ru.naburnm8.queueapp.authorization.viewmodel

import ru.naburnm8.queueapp.authorization.navigation.AuthorizationMainNavigation

sealed class AuthorizationState {
    data class Main (val route: AuthorizationMainNavigation) : AuthorizationState()
    data object Loading : AuthorizationState()
    data class Error(val message: String) : AuthorizationState()

}