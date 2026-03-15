package ru.naburnm8.queueapp.queueOperator.profile.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.profile.navigation.ProfileNavigation
import ru.naburnm8.queueapp.profile.ui.EditProfileScreen
import ru.naburnm8.queueapp.profile.ui.ProfileScreen
import ru.naburnm8.queueapp.profile.viewmodel.ProfileViewmodel

import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.queueOperator.profile.navigation.StudentProfileNavigation

fun NavGraphBuilder.profileFlow(
    navController: NavHostController,
) {
    navigation(
        route = QueueOperatorFlowNavigation.Profile.name,
        startDestination = StudentProfileNavigation.ViewProfileStudent.name
    ) {
        composable(StudentProfileNavigation.ViewProfileStudent.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Profile.name)
            }

            val profileVm: ProfileViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            ProfileScreen(modifier = Modifier.fillMaxSize(), profileVm) {
                navController.navigate(StudentProfileNavigation.EditProfileStudent.name)
            }
        }

        composable (StudentProfileNavigation.EditProfileStudent.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Profile.name)
            }

            val profileVm: ProfileViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            EditProfileScreen(modifier = Modifier.fillMaxSize(), profileVm) {
                navController.popBackStack()
            }
        }
    }

}