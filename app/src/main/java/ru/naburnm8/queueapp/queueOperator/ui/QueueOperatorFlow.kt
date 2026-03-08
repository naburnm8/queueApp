package ru.naburnm8.queueapp.queueOperator.ui

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
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.navigaton.ui.QueueOperatorNavigationBar
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

@Composable
fun QueueOperatorFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel()
) {
    val state by navigationViewmodel.stateFlow.collectAsState()

    state as NavigationState.QueueOperator

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {QueueOperatorNavigationBar()}
    ) { ip ->
        NavHost(
            navController = navController,
            startDestination = QueueOperatorFlowNavigation.MyQueues.name,
            modifier = Modifier.padding(ip)
        ) {
            composable(QueueOperatorFlowNavigation.MyQueues.name) {
                Text("My Queues Screen")
            }
            composable(QueueOperatorFlowNavigation.Settings.name) {
                Text("Settings Screen")
            }
            composable(QueueOperatorFlowNavigation.Profile.name) {
                Text("Profile Screen")
            }
        }
    }

    LaunchedEffect(state) {
        val target = (state as NavigationState.QueueOperator).route.name
        val current = navController.currentDestination?.route

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