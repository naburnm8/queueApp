package ru.naburnm8.queueapp.profile.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.navigation.ProfileNavigation

sealed class ProfileState {
    data class Error(val message: String) : ProfileState()
    data object Loading : ProfileState()
    data class Ready(val profile: ProfileEntity, val route: ProfileNavigation) : ProfileState()
}