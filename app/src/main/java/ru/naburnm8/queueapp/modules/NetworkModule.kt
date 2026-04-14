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
import ru.naburnm8.queueapp.BuildConfig
import ru.naburnm8.queueapp.authorization.api.AuthorizationApi
import ru.naburnm8.queueapp.authorization.api.RefreshApi
import ru.naburnm8.queueapp.authorization.api.IntegrationApi
import ru.naburnm8.queueapp.authorization.AuthorizationInterceptor
import ru.naburnm8.queueapp.authorization.session.SessionManager
import ru.naburnm8.queueapp.authorization.api.RegistrationApi
import ru.naburnm8.queueapp.authorization.token.TokenAuthenticator
import ru.naburnm8.queueapp.authorization.token.TokenRefresher
import ru.naburnm8.queueapp.authorization.token.TokenRefresherImpl
import ru.naburnm8.queueapp.authorization.token.TokenStorage
import ru.naburnm8.queueapp.authorization.token.TokenStorageImpl
import ru.naburnm8.queueapp.profile.api.ProfileApi
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.api.QueuePlansShortApi
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.api.SubmissionRequestsApi
import ru.naburnm8.queueapp.queueOperator.discipline.api.DisciplineApi
import ru.naburnm8.queueapp.queueOperator.metrics.api.StudentMetricsApi
import ru.naburnm8.queueapp.queueOperator.queues.api.QueuesApi
import ru.naburnm8.queueapp.queueOperator.queues.invitations.api.InvitationsApi
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.api.QueuePlansApi
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.api.QueueRulesApi
import ru.naburnm8.queueapp.websocket.QueueUpdatesManager
import java.util.concurrent.TimeUnit

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

val networkModule = module {
    single(named("api_base_url")) { BuildConfig.API_BASE_URL }
    single(named("api_socket")) { BuildConfig.API_SOCKET }

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
            .baseUrl(get<String>(named("api_base_url")))
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
            get()
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
            .baseUrl(get<String>(named("api_base_url")))
            .client(get<OkHttpClient>(named("main_okhttp")))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<ProfileApi> {
        get<Retrofit>(named("main_retrofit")).create(ProfileApi::class.java)
    }

    single<DisciplineApi> {
        get<Retrofit>(named("main_retrofit")).create(DisciplineApi::class.java)
    }

    single<StudentMetricsApi> {
        get<Retrofit>(named("main_retrofit")).create(StudentMetricsApi::class.java)
    }

    single<QueueRulesApi> {
        get<Retrofit>(named("main_retrofit")).create(QueueRulesApi::class.java)
    }

    single<QueuePlansApi> {
        get<Retrofit>(named("main_retrofit")).create(QueuePlansApi::class.java)
    }

    single<InvitationsApi> {
        get<Retrofit>(named("main_retrofit")).create(InvitationsApi::class.java)
    }

    single <SubmissionRequestsApi> {
        get<Retrofit>(named("main_retrofit")).create(SubmissionRequestsApi::class.java)
    }

    single <QueuePlansShortApi> {
        get<Retrofit>(named("main_retrofit")).create(QueuePlansShortApi::class.java)
    }

    single <QueuesApi> {
        get<Retrofit>(named("main_retrofit")).create(QueuesApi::class.java)
    }

    single {
        QueueUpdatesManager(
            get<String>(named("api_socket")),
            get(),
        )
    }

}