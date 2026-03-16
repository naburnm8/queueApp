package ru.naburnm8.queueapp.queueOperator.discipline.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueOperator.discipline.navigation.ActiveDisciplineNavigation
import ru.naburnm8.queueapp.queueOperator.discipline.navigation.DisciplinesNavigation
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

fun NavGraphBuilder.activeDisciplineFlow(
    navController: NavHostController
) {
    navigation(
        route = DisciplinesNavigation.ViewActiveDisciplineSubgraph.name,
        startDestination = ActiveDisciplineNavigation.ViewActiveDiscipline.name,
    ) {
        composable (ActiveDisciplineNavigation.ViewActiveDiscipline.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Disciplines.name)
            }

            val disciplineVm: DisciplineViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            ActiveDisciplineViewScreen(
                modifier = Modifier.fillMaxSize(),
                vm = disciplineVm,
                onNavigateToAddOwners = {navController.navigate(ActiveDisciplineNavigation.AddOwnersToDiscipline.name)},
                onNavigateToEdit = {navController.navigate(ActiveDisciplineNavigation.EditDiscipline.name)},
                onNavigateBackToRoot = {navController.popBackStack()}
            )
        }

        composable (ActiveDisciplineNavigation.EditDiscipline.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Disciplines.name)
            }

            val disciplineVm: DisciplineViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            ActiveDisciplineEditScreen(
                modifier = Modifier.fillMaxSize(),
                vm = disciplineVm,
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable (ActiveDisciplineNavigation.AddOwnersToDiscipline.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Disciplines.name)
            }

            val disciplineVm: DisciplineViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            ActiveDisciplineAddOwnerScreen(
                modifier = Modifier.fillMaxSize(),
                vm = disciplineVm,
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }
    }
}