package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.queueOperator.queues.navigation.QueuesNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.navigation.QueuePlansNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel

fun NavGraphBuilder.queuePlansFlow(
    navController: NavHostController
) {
    navigation(
        route = QueuesNavigation.QueuePlans.name,
        startDestination = QueuePlansNavigation.QueuePlansList.name
    ) {
        queuePlansEditFlow(navController)

        composable (QueuePlansNavigation.QueuePlansList.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueuesNavigation.QueuePlans.name)
            }

            val queuePlansVm: QueuePlansViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            QueuePlansListScreen(
                modifier = Modifier.fillMaxSize(),
                vm = queuePlansVm,
                onNavigateToCreate = {
                    navController.navigate(QueuePlansNavigation.QueuePlansCreate.name)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = {
                    navController.navigate(QueuePlansNavigation.QueuePlansEdit.name)
                }

            )
        }

        composable (QueuePlansNavigation.QueuePlansCreate.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueuesNavigation.QueuePlans.name)
            }

            val queuePlansVm: QueuePlansViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            QueuePlansCreateScreen(
                modifier = Modifier.fillMaxSize(),
                vm = queuePlansVm,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }

}