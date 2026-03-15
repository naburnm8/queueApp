package ru.naburnm8.queueapp.queueOperator.discipline.ui


import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueOperator.discipline.navigation.DisciplinesNavigation
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

fun NavGraphBuilder.disciplinesFlow (
    navController: NavHostController
) {
    navigation(
        route = QueueOperatorFlowNavigation.Disciplines.name,
        startDestination = DisciplinesNavigation.DisciplinesList.name
    ) {
        composable (DisciplinesNavigation.DisciplinesList.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Disciplines.name)
            }

            val disciplineVm: DisciplineViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            DisciplineListScreen(
                vm = disciplineVm,
                onNavigateToDisciplineCreation = {
                    navController.navigate(DisciplinesNavigation.CreateDiscipline.name)
                },
                onNavigateToActiveDisciplineScreen = {
                    navController.navigate(DisciplinesNavigation.ViewActiveDisciplineSubgraph.name)
                },
                onNavigateToCreate = {
                    navController.navigate(DisciplinesNavigation.CreateDiscipline)
                }
            )
        }

        composable (DisciplinesNavigation.CreateDiscipline.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Disciplines.name)
            }

            val disciplineVm: DisciplineViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            Text("Create")
        }


        activeDisciplineFlow(navController = navController)

    }
}