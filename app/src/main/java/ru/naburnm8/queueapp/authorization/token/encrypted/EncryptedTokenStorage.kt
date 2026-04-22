package ru.naburnm8.queueapp.authorization.token.encrypted

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import ru.naburnm8.queueapp.authorization.token.TokenStorage

private val Context.tokenDataStore by preferencesDataStore(name = "auth_tokens_encrypted")


class EncryptedTokenStorage (
    private val context: Context
) : TokenStorage {

    private val encrypter = TokenEncrypter(context)

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }


    override suspend fun getAccessToken(): String? {
        val encrypted = context.tokenDataStore.data.first()[ACCESS_TOKEN] ?: return null
        return runCatching { encrypter.decrypt(encrypted) }.getOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        val encrypted = context.tokenDataStore.data.first()[REFRESH_TOKEN] ?: return null
        return runCatching { encrypter.decrypt(encrypted) }.getOrNull()
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.tokenDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = encrypter.encrypt(accessToken)
            prefs[REFRESH_TOKEN] = encrypter.encrypt(refreshToken)
        }
    }

    override suspend fun clear() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
        }
    }

}