package ru.naburnm8.queueapp.authorization.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.naburnm8.queueapp.authorization.api.AuthorizationApi
import ru.naburnm8.queueapp.authorization.entity.Role
import ru.naburnm8.queueapp.authorization.request.LogoutRequest
import ru.naburnm8.queueapp.authorization.token.TokenStorage

class SessionManager(
    private val tokenStorage: TokenStorage,
    private val authApi: AuthorizationApi
) {
    private val _logoutFlow = MutableSharedFlow<Unit>()
    val logoutFlow = _logoutFlow.asSharedFlow()

    private val _loginFlow = MutableSharedFlow<Role>()
    val loginFlow = _loginFlow.asSharedFlow()

    suspend fun logout() {
        val refreshToken = tokenStorage.getRefreshToken() ?: return

        try {
            authApi.logout(LogoutRequest(refreshToken))
        } catch (_: Exception) {

        }

        closeSession()
    }

    suspend fun closeSession() {
        tokenStorage.clear()
        _logoutFlow.emit(Unit)
    }

    suspend fun initiateSession(accessToken: String, refreshToken: String, role: Role) {
        tokenStorage.saveTokens(accessToken, refreshToken)
        _loginFlow.emit(role)
    }

}