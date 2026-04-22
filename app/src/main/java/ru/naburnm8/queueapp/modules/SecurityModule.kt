package ru.naburnm8.queueapp.modules

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.naburnm8.queueapp.authorization.token.TokenStorage
import ru.naburnm8.queueapp.authorization.token.encrypted.EncryptedTokenStorage

val securityModule = module {

    single<TokenStorage> {
        EncryptedTokenStorage(androidContext())
    }

}