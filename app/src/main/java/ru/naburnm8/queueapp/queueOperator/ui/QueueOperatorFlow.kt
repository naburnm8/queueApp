package ru.naburnm8.queueapp.queueOperator.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.navigaton.ui.QueueOperatorNavigationBar
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel

@Composable
fun QueueOperatorFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel()
) {
    val state by navigationViewmodel.stateFlow.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {QueueOperatorNavigationBar()}
    ) {
            ip ->
        NavHost(
            navController = navController,
            startDestination = NavigationState.QueueOperator.MyQueues,
            modifier = Modifier.padding(ip)
        ) {

        }
    }

    LaunchedEffect(state) {
        val target = state
        val current = navController.currentDestination

        if (current != target) {
            navController.navigate(target) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }
    }
}