package ru.naburnm8.queueapp.queueConsumer.queue.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueConsumer.queue.navigation.QueueNavigation
import ru.naburnm8.queueapp.queueConsumer.queue.viewmodel.QueueViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.ui.queueIdArg
import java.util.UUID
import java.util.UUID.fromString


fun NavGraphBuilder.queueFlow(
    navController: NavHostController
) {
    navigation(
        route = QueueConsumerFlowNavigation.MyQueue.name,
        startDestination = QueueNavigation.StudentQueueMain.name
    ) {


        composable (QueueNavigation.StudentQueueMain.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueConsumerFlowNavigation.MyQueue.name)
            }

            val queuesVm: QueueViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            QueueListScreen(
                modifier = Modifier.fillMaxSize(),
                vm = queuesVm,
            ) {id ->
                queuesVm.loadQueueRules(id) {
                    navController.navigate("${QueueNavigation.StudentQueueView.name}/${id}")
                }
            }
        }

        composable (
            route = "${QueueNavigation.StudentQueueView.name}/{$queueIdArg}",
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
                navController.getBackStackEntry(QueueConsumerFlowNavigation.MyQueue.name)
            }

            val queuesVm: QueueViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            QueueViewScreen(
                modifier = Modifier.fillMaxSize(),
                vm = queuesVm,
                queueId = queueId,
                onNavigateBack = { navController.popBackStack() }
            )

        }
    }
}