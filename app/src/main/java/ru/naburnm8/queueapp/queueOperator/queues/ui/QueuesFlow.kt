package ru.naburnm8.queueapp.queueOperator.queues.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.queueOperator.queues.navigation.QueuesNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui.queuePlansFlow
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.viewmodel.QueuesViewmodel
import java.util.UUID

const val queueIdArg = "queueId"

fun NavGraphBuilder.queuesFlow(
    navController: NavHostController
) {
    navigation(
        route = QueueOperatorFlowNavigation.MyQueues.name,
        startDestination = QueuesNavigation.QueuesMain.name
    ) {
        composable (QueuesNavigation.QueuesMain.name) {

            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.MyQueues.name)
            }

            val queuesVm: QueuesViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            QueuesScreen(
                modifier = Modifier.fillMaxSize(),
                vm = queuesVm,
                onNavigateToQueuePlans = {navController.navigate(QueuesNavigation.QueuePlans.name)},
                onNavigateToQueue = {id ->
                    navController.navigate("${QueuesNavigation.QueueViewAndInteraction.name}/${id}")
                }
            )

        }

        composable (
            route = "${QueuesNavigation.QueueViewAndInteraction}/{$queueIdArg}",
            arguments = listOf(
                navArgument(queueIdArg) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val queueId = backStackEntry
                .arguments
                ?.getString(queueIdArg)
                .let(UUID::fromString)

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.MyQueues.name)
            }

            val queuesVm: QueuesViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            QueuesViewAndInteractionScreen(
                modifier = Modifier.fillMaxSize(),
                queueId = queueId,
                vm = queuesVm,
                onNavigateBack = { navController.popBackStack() }
            )

        }

        queuePlansFlow(navController = navController)
    }
}