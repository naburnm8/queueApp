package ru.naburnm8.queueapp.modules

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.naburnm8.queueapp.authorization.api.AuthorizationApi
import ru.naburnm8.queueapp.authorization.api.RefreshApi
import ru.naburnm8.queueapp.authorization.api.IntegrationApi
import ru.naburnm8.queueapp.authorization.AuthorizationInterceptor
import ru.naburnm8.queueapp.authorization.SessionManager
import ru.naburnm8.queueapp.authorization.api.RegistrationApi
import ru.naburnm8.queueapp.authorization.token.TokenAuthenticator
import ru.naburnm8.queueapp.authorization.token.TokenRefresher
import ru.naburnm8.queueapp.authorization.token.TokenRefresherImpl
import ru.naburnm8.queueapp.authorization.token.TokenStorage
import ru.naburnm8.queueapp.authorization.token.TokenStorageImpl
import java.util.concurrent.TimeUnit

private const val DEBUG_URL = "http://10.0.2.2:8081/"

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

val networkModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<TokenStorage> {
        TokenStorageImpl(androidContext())
    }

    single {
        AuthorizationInterceptor(get())
    }

    single(named("auth_okhttp")) {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named("auth_retrofit")) {
        Retrofit.Builder()
            .baseUrl(DEBUG_URL)
            .client(get<OkHttpClient>(named("auth_okhttp")))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<AuthorizationApi> {
        get<Retrofit>(named("auth_retrofit")).create(AuthorizationApi::class.java)
    }

    single<RefreshApi> {
        get<Retrofit>(named("auth_retrofit")).create(RefreshApi::class.java)
    }

    single<IntegrationApi> {
        get<Retrofit>(named("auth_retrofit")).create(IntegrationApi::class.java)
    }

    single<RegistrationApi> {
        get<Retrofit>(named("auth_retrofit")).create(RegistrationApi::class.java)
    }

    single<TokenRefresher> {
        TokenRefresherImpl(get(),)
    }

    single {
        SessionManager(
            tokenStorage = get(),
            authApi = get(),
        )
    }

    single {
        TokenAuthenticator(
            tokenStorage = get(),
            tokenRefresher = get(),
            sessionManager = get(),
        )
    }

    single(named("main_okhttp")) {
        OkHttpClient.Builder()
            .addInterceptor(get< AuthorizationInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single(named("main_retrofit")) {
        Retrofit.Builder()
            .baseUrl(DEBUG_URL)
            .client(get<OkHttpClient>(named("main_okhttp")))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

}