package ru.naburnm8.queueapp.authorization.token

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.tokenDataStore by preferencesDataStore(name = "auth_tokens")

class TokenStorageImpl(
    private val context: Context,
) : TokenStorage {
    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun getAccessToken(): String? {
        return context.tokenDataStore.data.first()[ACCESS_TOKEN]
    }

    override suspend fun getRefreshToken(): String? {
        return context.tokenDataStore.data.first()[REFRESH_TOKEN]
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.tokenDataStore.edit{
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clear() {
        context.tokenDataStore.edit {
            it.remove(ACCESS_TOKEN)
            it.remove(REFRESH_TOKEN)
        }
    }

}