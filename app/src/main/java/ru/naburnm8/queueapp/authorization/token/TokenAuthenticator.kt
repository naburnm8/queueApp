package ru.naburnm8.queueapp.authorization.token

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.naburnm8.queueapp.authorization.SessionManager

class TokenAuthenticator(
    private val tokenStorage: TokenStorage,
    private val tokenRefresher: TokenRefresher,
    private val sessionManager: SessionManager

) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            return null
        }

        val refreshToken = runBlocking { tokenStorage.getRefreshToken() } ?: return null

        return try {
            val tokenPair = tokenRefresher.refreshBlocking(refreshToken)

            runBlocking {
                tokenStorage.saveTokens(
                    accessToken = tokenPair.accessToken,
                    refreshToken = tokenPair.refreshToken
                )
            }

            response.request.newBuilder()
                .header("Authorization", "Bearer ${tokenPair.accessToken}")
                .build()
        } catch (e: Exception) {
            runBlocking {
                tokenStorage.clear()
                sessionManager.closeSession()
            }
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var current = response.priorResponse
        while (current != null) {
            result++
            current = current.priorResponse
        }
        return result
    }
}