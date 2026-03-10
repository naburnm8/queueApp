package ru.naburnm8.queueapp.authorization.token

import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse

interface TokenRefresher {
    fun refreshBlocking(refreshToken: String): AuthorizationResponse
}