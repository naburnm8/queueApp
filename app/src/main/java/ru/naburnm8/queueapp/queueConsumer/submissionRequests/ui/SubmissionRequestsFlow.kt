package ru.naburnm8.queueapp.queueConsumer.submissionRequests.ui

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.navigation.SubmissionRequestsNavigation

fun NavGraphBuilder.submissionRequestsFlow (
    navController: NavHostController
) {
    navigation (
        route = QueueConsumerFlowNavigation.MyRequests.name,
        startDestination = SubmissionRequestsNavigation.SubmissionRequestsList.name
    ) {
        composable (SubmissionRequestsNavigation.SubmissionRequestsList.name) {
            Text("Submission requests")
        }
    }
}