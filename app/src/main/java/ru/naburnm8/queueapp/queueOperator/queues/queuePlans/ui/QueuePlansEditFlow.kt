package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueOperator.queues.invitations.ui.InvitationsScreen
import ru.naburnm8.queueapp.queueOperator.queues.invitations.viewmodel.InvitationsViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.navigation.QueuesNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.navigation.QueuePlansEditNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.navigation.QueuePlansNavigation
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansState
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel

fun NavGraphBuilder.queuePlansEditFlow(
    navController: NavHostController
) {
    navigation (
        route = QueuePlansNavigation.QueuePlansEdit.name,
        startDestination = QueuePlansEditNavigation.QueuePlansEditParamsAndDetails.name
    ) {
        composable (QueuePlansEditNavigation.QueuePlansEditParamsAndDetails.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueuesNavigation.QueuePlans.name)
            }

            val queuePlansVm: QueuePlansViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)
            val invitationsViewmodel: InvitationsViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            val queuePlansState = queuePlansVm.stateFlow.collectAsState().value

            QueuePlansEditScreen(
                modifier = Modifier.fillMaxSize(),
                vm = queuePlansVm,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToRules = {
                    navController.navigate(QueuePlansEditNavigation.QueuePlansEditRules.name)
                },
                onNavigateToInvitations = {
                    if (queuePlansState is QueuePlansState.Main && queuePlansState.activePlan != null) {
                        invitationsViewmodel.loadInvitations(queuePlansState.activePlan) {
                            navController.navigate(QueuePlansEditNavigation.QueuePlansEditInvitations.name)
                        }
                    }
                }

            )
        }

        composable (QueuePlansEditNavigation.QueuePlansEditInvitations.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueuesNavigation.QueuePlans.name)
            }

            val queuePlansVm: QueuePlansViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)
            val invitationsViewmodel: InvitationsViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            InvitationsScreen(
                modifier = Modifier.fillMaxSize(),
                queuePlansVm,
                invitationsViewmodel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable (QueuePlansEditNavigation.QueuePlansEditRules.name) {

        }
    }

}