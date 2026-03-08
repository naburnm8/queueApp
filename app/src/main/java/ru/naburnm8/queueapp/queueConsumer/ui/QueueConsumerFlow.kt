package ru.naburnm8.queueapp.queueConsumer.ui

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
import ru.naburnm8.queueapp.navigaton.ui.QueueConsumerNavigationBar
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation

@Composable
fun QueueConsumerFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel()
) {
    val state by navigationViewmodel.stateFlow.collectAsState()

    state as NavigationState.QueueConsumer

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {QueueConsumerNavigationBar()}
    ) {
        ip ->
        NavHost(
            navController = navController,
            startDestination = QueueConsumerFlowNavigation.MyQueue.name,
            modifier = Modifier.padding(ip)
        ) {
            composable(QueueConsumerFlowNavigation.MyQueue.name) {
                Text("My Queue Screen")
            }
            composable(QueueConsumerFlowNavigation.MyRequests.name) {
                Text("My Requests Screen")
            }
            composable(QueueConsumerFlowNavigation.MyProfile.name) {
                Text("My Profile Screen")
            }
        }
    }

    LaunchedEffect(state) {
        val target = (state as NavigationState.QueueConsumer).route.name
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