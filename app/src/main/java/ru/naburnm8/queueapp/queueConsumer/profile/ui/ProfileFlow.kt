package ru.naburnm8.queueapp.queueConsumer.profile.ui

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
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueConsumer.profile.navigation.TeacherProfileNavigation

fun NavGraphBuilder.profileFlow(
    navController: NavHostController,
) {
    navigation(
        route = QueueConsumerFlowNavigation.MyProfile.name,
        startDestination = TeacherProfileNavigation.ViewProfileTeacher.name
    ) {
        composable(TeacherProfileNavigation.ViewProfileTeacher.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueConsumerFlowNavigation.MyProfile.name)
            }

            val profileVm: ProfileViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            ProfileScreen(modifier = Modifier.fillMaxSize(), profileVm = profileVm) {
                navController.navigate(TeacherProfileNavigation.EditProfileTeacher.name)
            }
        }

        composable (TeacherProfileNavigation.EditProfileTeacher.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueConsumerFlowNavigation.MyProfile.name)
            }

            val profileVm: ProfileViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            EditProfileScreen(modifier = Modifier.fillMaxSize(), profileVm) {
                navController.popBackStack()
            }
        }
    }

}