package ru.naburnm8.queueapp.queueOperator.queues.ui

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.queueOperator.queues.navigation.QueuesNavigation


fun NavGraphBuilder.queuesFlow(
    navController: NavHostController
) {
    navigation(
        route = QueueOperatorFlowNavigation.MyQueues.name,
        startDestination = QueuesNavigation.QueuesMain.name
    ) {
        composable (QueuesNavigation.QueuesMain.name) {
            Text ("My queues")
        }
    }
}