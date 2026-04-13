package ru.naburnm8.queueapp.fake

import ru.naburnm8.queueapp.authorization.token.TokenStorage

class FakeTokenStorage(
    private var access: String?,
    private var refresh: String?,
) : TokenStorage {
    override suspend fun getAccessToken(): String? {
        return access
    }

    override suspend fun getRefreshToken(): String? {
        return refresh
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        access = accessToken
        refresh = refreshToken
    }

    override suspend fun clear() {
        access = null
        refresh = null
    }

}