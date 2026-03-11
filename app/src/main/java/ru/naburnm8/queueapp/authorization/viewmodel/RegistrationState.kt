package ru.naburnm8.queueapp.authorization.viewmodel

import ru.naburnm8.queueapp.authorization.entity.IntegrationEntity

sealed class RegistrationState {
    data class IntegrationsLoaded(val list: List<IntegrationEntity>) : RegistrationState()
    data object Error : RegistrationState()
    data object Loading : RegistrationState()
}