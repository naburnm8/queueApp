package ru.naburnm8.queueapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.naburnm8.queueapp.authorization.token.TokenStorage
import ru.naburnm8.queueapp.authorization.token.TokenStorageImpl
import ru.naburnm8.queueapp.authorization.token.encrypted.EncryptedTokenStorage


@RunWith(AndroidJUnit4::class)
class TokenStorageTest {

    private lateinit var context: Context
    private lateinit var tokenStorage: TokenStorage

    private lateinit var encryptedTokenStorage: TokenStorage

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        tokenStorage = TokenStorageImpl(context)
        encryptedTokenStorage = EncryptedTokenStorage(context)
    }

    @Test
    fun saveAndRetrieveTokens() = runBlocking {
        tokenStorage.clear()

        tokenStorage.saveTokens(
            accessToken = "access token test",
            refreshToken = "refresh token test"
        )

        val access = tokenStorage.getAccessToken()
        val refresh = tokenStorage.getRefreshToken()

        assertEquals("access token test", access)
        assertEquals("refresh token test", refresh)
    }

    @Test
    fun saveAndRetrieveTokensEncrypted() = runBlocking {
        encryptedTokenStorage.clear()

        encryptedTokenStorage.saveTokens(
            accessToken = "access token test",
            refreshToken = "refresh token test"
        )

        val access = encryptedTokenStorage.getAccessToken()
        val refresh = encryptedTokenStorage.getRefreshToken()

        assertEquals("access token test", access)
        assertEquals("refresh token test", refresh)
    }

    @After
    fun teardown() = runBlocking {
        tokenStorage.clear()
        encryptedTokenStorage.clear()
    }
}