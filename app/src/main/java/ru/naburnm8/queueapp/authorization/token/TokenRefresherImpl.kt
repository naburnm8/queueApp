package ru.naburnm8.queueapp.authorization.token

import android.util.Log
import ru.naburnm8.queueapp.authorization.api.RefreshApi
import ru.naburnm8.queueapp.authorization.request.RefreshRequest
import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse

class TokenRefresherImpl (
    private val refreshApi: RefreshApi
) : TokenRefresher {
    override fun refreshBlocking(refreshToken: String): AuthorizationResponse {
        Log.d(TAG, "Attempting to refresh token")

        val response = refreshApi.refreshBlocking(RefreshRequest(refreshToken)).execute()

        if (!response.isSuccessful) {
            Log.e(TAG, "Token refresh failed with code ${response.code()} and message ${response.message()}")
            throw IllegalStateException("Refreshing token failed with code ${response.code()} and message ${response.message()}")
        }

        Log.d(TAG, "Token refresh successful")
        return response.body() ?: throw IllegalStateException("Response body is null")
    }

    companion object {
        private const val TAG = "TokenRefresherImpl"
    }
}