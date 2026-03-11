package ru.naburnm8.queueapp.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationViewmodel
import ru.naburnm8.queueapp.authorization.viewmodel.RegistrationViewmodel
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.viewmodel.InterViewmodelBridge


val viewmodelModule = module {
    viewModel{
        NavigationViewmodel(get())
    }

    viewModel {
        AuthorizationViewmodel(get(), get(), get())
    }

    viewModel {
        RegistrationViewmodel(get(), get(), get(), get())
    }

    single {
        InterViewmodelBridge()
    }
}