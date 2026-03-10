package ru.naburnm8.queueapp.authorization.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.authorization.navigation.AuthorizationMainNavigation
import ru.naburnm8.queueapp.authorization.ui.main.AuthorizationMainFlow
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationState
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationViewmodel
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

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
            GenericErrorScreen(
                modifier = Modifier.fillMaxSize(),
                errorMessage = (state.value as AuthorizationState.Error).message
            ) {
                authorizationViewmodel.changeMainFlowState(AuthorizationMainNavigation.LOGIN)
            }
        }
        is AuthorizationState.Loading -> {
            GenericLoadingScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }

    }

}