package ru.naburnm8.queueapp.authorization.ui.main

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.authorization.navigation.AuthorizationMainNavigation
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationState
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationViewmodel

@Composable
fun AuthorizationMainFlow(
    modifier: Modifier = Modifier,
    vm: AuthorizationViewmodel = koinViewModel(),
){
    val state by vm.stateFlow.collectAsState()

    state as AuthorizationState.Main

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthorizationMainNavigation.LOGIN.name,
        modifier = modifier
    ) {
        composable(
            AuthorizationMainNavigation.LOGIN.name,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            LoginScreen(
                modifier = modifier.fillMaxSize(),
                onLoginClick = {email, pass -> vm.tryLogin(email, pass)},
                onRegistrationClick = {
                    vm.changeMainFlowState(AuthorizationMainNavigation.REGISTRATION_CHOICE)
                    navController.navigate(AuthorizationMainNavigation.REGISTRATION_CHOICE.name)
                }
            )
        }
        composable (
            AuthorizationMainNavigation.REGISTRATION_CHOICE.name,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            RegistrationChoiceScreen(
                modifier = modifier.fillMaxSize(),
                onStudentRegistrationClick = {
                    vm.changeMainFlowState(AuthorizationMainNavigation.STUDENT_REGISTRATION)
                    navController.navigate(AuthorizationMainNavigation.STUDENT_REGISTRATION.name)
                },
                onTeacherRegistrationClick = {
                    vm.changeMainFlowState(AuthorizationMainNavigation.TEACHER_REGISTRATION)
                    navController.navigate(AuthorizationMainNavigation.TEACHER_REGISTRATION.name)
                }
            )
        }
        composable (
            AuthorizationMainNavigation.STUDENT_REGISTRATION.name,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {

        }
        composable (
            AuthorizationMainNavigation.TEACHER_REGISTRATION.name,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {

        }
    }


}