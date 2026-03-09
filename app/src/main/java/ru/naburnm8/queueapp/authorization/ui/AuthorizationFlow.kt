package ru.naburnm8.queueapp.authorization.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse
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

        is AuthorizationState.Login -> {

        }
        is AuthorizationState.Register -> {

        }
        is AuthorizationState.Error -> {

        }
        is AuthorizationState.Loading -> {

        }

    }

}