package ru.naburnm8.queueapp.queueConsumer.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.navigaton.ui.QueueConsumerNavigationBar
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueConsumer.profile.ui.profileFlow
import ru.naburnm8.queueapp.queueConsumer.queue.ui.queueFlow
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.ui.submissionRequestsFlow

@Composable
fun QueueConsumerFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel()
) {
    val state by navigationViewmodel.stateFlow.collectAsState()

    state as NavigationState.QueueConsumer

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {QueueConsumerNavigationBar(
            currentDestination = currentDestination,
            onTabClick = {route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }

            }
        )}
    ) {
        ip ->
        NavHost(
            navController = navController,
            startDestination = QueueConsumerFlowNavigation.MyQueue.name,
            modifier = Modifier.padding(ip)
        ) {
            queueFlow(navController)

            submissionRequestsFlow(navController)

            profileFlow(navController)
        }
    }
}