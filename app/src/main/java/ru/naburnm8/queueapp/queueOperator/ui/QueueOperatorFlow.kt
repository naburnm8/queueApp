package ru.naburnm8.queueapp.queueOperator.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.navigaton.ui.QueueOperatorNavigationBar
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.queueOperator.discipline.ui.disciplinesFlow
import ru.naburnm8.queueapp.queueOperator.metrics.ui.studentMetricsFlow
import ru.naburnm8.queueapp.queueOperator.profile.ui.profileFlow
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.queueOperator.queues.ui.queuesFlow

@Composable
fun QueueOperatorFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel()
) {
    val state by navigationViewmodel.stateFlow.collectAsState()

    state as NavigationState.QueueOperator

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            QueueOperatorNavigationBar(
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
            )
        }
    ) { ip ->
        NavHost(
            navController = navController,
            startDestination = QueueOperatorFlowNavigation.MyQueues.name,
            modifier = Modifier.padding(ip)
        ) {
            queuesFlow(navController)

            disciplinesFlow(navController)

            profileFlow(navController)

            studentMetricsFlow(navController)
        }
    }
}