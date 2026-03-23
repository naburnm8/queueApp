package ru.naburnm8.queueapp.queueConsumer.submissionRequests.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.navigation.SubmissionRequestsNavigation
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel.SubmissionRequestsViewmodel
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

fun NavGraphBuilder.submissionRequestsFlow (
    navController: NavHostController
) {
    navigation (
        route = QueueConsumerFlowNavigation.MyRequests.name,
        startDestination = SubmissionRequestsNavigation.SubmissionRequestsList.name
    ) {
        composable (SubmissionRequestsNavigation.SubmissionRequestsList.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueConsumerFlowNavigation.MyRequests.name)
            }

            val vm : SubmissionRequestsViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            SubmissionRequestsListScreen(
                modifier = Modifier.fillMaxSize(),
                vm = vm,
                onNavigateToCreate = {
                    navController.navigate(SubmissionRequestsNavigation.CreateSubmissionRequest.name)
                },
                onNavigateToEdit = {
                    navController.navigate(SubmissionRequestsNavigation.EditSubmissionRequest.name)
                }
            )

        }

        composable (SubmissionRequestsNavigation.CreateSubmissionRequest.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueConsumerFlowNavigation.MyRequests.name)
            }

            val vm : SubmissionRequestsViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            SubmissionRequestCreateScreen(
                modifier = Modifier.fillMaxSize(),
                vm = vm,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )

        }
    }
}