package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueOperator.queues.navigation.QueuesNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.navigation.QueuePlansEditNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.navigation.QueuePlansNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.navigation.QueueRulesNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesViewmodel

fun NavGraphBuilder.queueRulesFlow(
    navController: NavHostController
) {
    navigation(
        route = QueuePlansEditNavigation.QueuePlansEditRules.name,
        startDestination = QueueRulesNavigation.QueueRulesList.name
    ) {
        composable(QueueRulesNavigation.QueueRulesList.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueuesNavigation.QueuePlans.name)
            }

            val rulesVm: QueueRulesViewmodel = koinViewModel (viewModelStoreOwner = parentEntry)
            val queuePlanVm: QueuePlansViewmodel = koinViewModel (viewModelStoreOwner = parentEntry)

            QueueRuleListScreen(
                modifier = Modifier.fillMaxSize(),
                plansVm = queuePlanVm,
                rulesVm = rulesVm,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = {
                    navController.navigate(QueueRulesNavigation.QueueRulesEdit.name)
                },
                onNavigateToCreate = {
                    navController.navigate(QueueRulesNavigation.QueueRulesCreate.name)
                }
            )
        }

        composable (QueueRulesNavigation.QueueRulesEdit.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueuesNavigation.QueuePlans.name)
            }

            val rulesVm: QueueRulesViewmodel = koinViewModel (viewModelStoreOwner = parentEntry)

            QueueRulesEditScreen(
                modifier = Modifier.fillMaxSize(),
                vm = rulesVm,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable (QueueRulesNavigation.QueueRulesCreate.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueuesNavigation.QueuePlans.name)
            }

            val rulesVm: QueueRulesViewmodel = koinViewModel (viewModelStoreOwner = parentEntry)

            QueueRuleCreateScreen(
                modifier = Modifier.fillMaxSize(),
                vm = rulesVm,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }


    }
}