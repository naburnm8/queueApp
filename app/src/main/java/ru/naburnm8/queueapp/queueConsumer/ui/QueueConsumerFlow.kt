package ru.naburnm8.queueapp.queueConsumer.ui

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.navigaton.ui.QueueConsumerNavigationBar
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel

@Composable
fun QueueConsumerFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel()
) {
    val state by navigationViewmodel.stateFlow.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {QueueConsumerNavigationBar()}
    ) {
        ip ->
        NavHost(
            navController = navController,
            startDestination = NavigationState.QueueConsumer.MyQueue,
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