package ru.naburnm8.queueapp.authorization

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.naburnm8.queueapp.authorization.token.TokenStorage

class AuthorizationInterceptor(private val tokenStorage: TokenStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val path = original.url.encodedPath

        val isAuthRequest = path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register") || path.startsWith("/api/auth/refresh/") || path.startsWith("/api/auth/logout/")

        if (isAuthRequest) {
            return chain.proceed(original)
        }

        val accessToken = runBlocking { tokenStorage.getAccessToken() }

        val request = if (!accessToken.isNullOrBlank()) {
            original.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            original
        }

        return chain.proceed(request)
    }


}