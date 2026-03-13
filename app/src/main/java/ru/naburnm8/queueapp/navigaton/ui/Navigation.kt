package ru.naburnm8.queueapp.navigaton.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.authorization.ui.AuthorizationFlow
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.queueConsumer.ui.QueueConsumerFlow
import ru.naburnm8.queueapp.queueOperator.ui.QueueOperatorFlow
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
private const val TAG = "Navigation"

@Composable
fun Navigation(viewmodel: NavigationViewmodel = koinViewModel()) {

    val state by viewmodel.stateFlow.collectAsState()

    when (state) {
        is NavigationState.QueueConsumer -> {
            Log.d(TAG, "Navigation: QueueConsumer")
            QueueConsumerFlow()
        }
        is NavigationState.QueueOperator -> {
            Log.d(TAG, "Navigation: QueueOperator")
            QueueOperatorFlow()
        }
        is NavigationState.Unauthorized -> {
            Log.d(TAG, "Navigation: Unauthorized")
            AuthorizationFlow()
        }
        is NavigationState.Loading -> {
            Log.d(TAG, "Navigation: Loading")
            GenericLoadingScreen(modifier = Modifier.fillMaxSize())
        }
    }
}