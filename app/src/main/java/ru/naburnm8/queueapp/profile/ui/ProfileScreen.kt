package ru.naburnm8.queueapp.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.profile.navigation.ProfileNavigation
import ru.naburnm8.queueapp.profile.viewmodel.ProfileState
import ru.naburnm8.queueapp.profile.viewmodel.ProfileViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileVm: ProfileViewmodel,
    onEditClick: () -> Unit,
) {
    val state = profileVm.stateFlow.collectAsState()

    when (state.value) {
        is ProfileState.Error -> {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = (state.value as ProfileState.Error).message
            ) {
                    profileVm.resetScreen()
            }
        }
        is ProfileState.Loading -> {
            GenericLoadingScreen(
                modifier = modifier
            )


        }
        is ProfileState.Ready -> {
            Column(
                modifier = modifier.padding(16.dp)
            ) {
                ProfileCard(
                    modifier = Modifier,
                    profile = (state.value as ProfileState.Ready).profile,
                    onEditClick = {
                        profileVm.changeLoadedRoute(ProfileNavigation.EDIT)
                        onEditClick()
                    },
                    onLogoutClick = {
                        profileVm.logout()
                    }
                )
            }

        }
    }


}