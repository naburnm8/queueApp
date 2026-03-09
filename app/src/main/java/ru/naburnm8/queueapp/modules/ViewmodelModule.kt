package ru.naburnm8.queueapp.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationViewmodel
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel


val viewmodelModule = module {
    viewModel{
        NavigationViewmodel()
    }

    viewModel {
        AuthorizationViewmodel()
    }
}