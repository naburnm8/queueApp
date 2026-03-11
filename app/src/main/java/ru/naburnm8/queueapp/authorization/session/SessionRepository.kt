package ru.naburnm8.queueapp.authorization.session

import ru.naburnm8.queueapp.authorization.entity.Role
import ru.naburnm8.queueapp.authorization.token.TokenStorage
import ru.naburnm8.queueapp.util.JwtUtil

class SessionRepository (
    private val tokenStorage: TokenStorage
) {
    suspend fun resolveSession(): SessionState {
        val accessToken = tokenStorage.getAccessToken()

        if (accessToken.isNullOrBlank()) {
            return SessionState.Unauthorized
        }

        val roles = JwtUtil.getRoles(accessToken)

        return when {
            Role.ROLE_QOPERATOR.name in roles -> SessionState.Teacher
            Role.ROLE_QCONSUMER.name in roles -> SessionState.Student
            else -> SessionState.Unauthorized
        }
    }
}