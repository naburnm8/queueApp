package ru.naburnm8.queueapp.queueOperator.queues.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.queueOperator.queues.navigation.QueuesNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui.queuePlansFlow


fun NavGraphBuilder.queuesFlow(
    navController: NavHostController
) {
    navigation(
        route = QueueOperatorFlowNavigation.MyQueues.name,
        startDestination = QueuesNavigation.QueuesMain.name
    ) {
        composable (QueuesNavigation.QueuesMain.name) {
            Column() {
                Text ("My queues")
                Button(
                    onClick = {navController.navigate(QueuesNavigation.QueuePlans.name)}
                ) {
                    Text("Go to queue plans")
                }
            }

        }

        queuePlansFlow(navController = navController)
    }
}