package ru.naburnm8.queueapp.modules

import org.koin.dsl.module
import ru.naburnm8.queueapp.authorization.repository.AuthorizationRepository
import ru.naburnm8.queueapp.authorization.repository.IntegrationRepository

val repositoryModule = module {
    single {
        AuthorizationRepository(get())
    }

    single {
        IntegrationRepository(get())
    }
}