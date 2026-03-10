package ru.naburnm8.queueapp.authorization.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.authorization.ui.main.AuthorizationMainFlow
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationState
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationViewmodel
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel

@Composable
fun AuthorizationFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel(),
    authorizationViewmodel: AuthorizationViewmodel = koinViewModel(),
) {

    val state = authorizationViewmodel.stateFlow.collectAsState()

    when (state.value) {

        is AuthorizationState.Main -> {
            AuthorizationMainFlow()
        }
        is AuthorizationState.Error -> {

        }
        is AuthorizationState.Loading -> {

        }

    }

}