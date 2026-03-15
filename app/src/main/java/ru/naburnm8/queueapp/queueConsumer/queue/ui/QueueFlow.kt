package ru.naburnm8.queueapp.queueConsumer.queue.ui


import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueConsumer.queue.navigation.QueueNavigation

fun NavGraphBuilder.queueFlow(
    navController: NavHostController
) {
    navigation(
        route = QueueConsumerFlowNavigation.MyQueue.name,
        startDestination = QueueNavigation.StudentQueueMain.name
    ) {
        composable (QueueNavigation.StudentQueueMain.name) {
            Text("My queue")
        }
    }
}