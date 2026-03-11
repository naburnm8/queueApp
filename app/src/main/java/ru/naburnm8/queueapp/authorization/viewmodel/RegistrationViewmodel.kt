package ru.naburnm8.queueapp.authorization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import ru.naburnm8.queueapp.authorization.SessionManager
import ru.naburnm8.queueapp.authorization.entity.IntegrationEntity
import ru.naburnm8.queueapp.authorization.entity.RegisterStudentEntity
import ru.naburnm8.queueapp.authorization.entity.RegisterTeacherEntity
import ru.naburnm8.queueapp.authorization.repository.IntegrationRepository
import ru.naburnm8.queueapp.authorization.repository.RegistrationRepository
import java.util.UUID

class RegistrationViewmodel(
    private val integrationsRepository: IntegrationRepository,
    private val registrationRepository: RegistrationRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private var _integrationsStateFlow = MutableStateFlow<RegistrationState>(RegistrationState.Loading)

    val integrationsStateFlow = _integrationsStateFlow.asStateFlow()

    fun fetchAllIntegrations() {
        viewModelScope.launch {
            _integrationsStateFlow.value = RegistrationState.Loading

            runCatching {
                val fetched = integrationsRepository.getAllIntegrations()
                if (fetched.isFailure) throw IOException("Failed to fetch integrations")
                val mappedList = fetched.getOrNull()?.map {
                    IntegrationEntity(
                        id = it.id,
                        name = it.name,
                        payload = it.payload
                    )
                } ?: throw IOException("Body is null")
                _integrationsStateFlow.value = RegistrationState.IntegrationsLoaded(
                        mappedList
                    )
            }.onFailure {
                _integrationsStateFlow.value = RegistrationState.Error
            }


        }
    }

    private fun tryRegisterTeacherWithIntegration(req: RegisterTeacherEntity, integration: UUID) {

    }

    private fun tryRegisterStudentWithIntegration(req: RegisterStudentEntity, integration: UUID) {

    }

    fun tryRegisterStudent(req: RegisterStudentEntity, integration: UUID) {
        if (integration != UUID(0,0)) return tryRegisterStudentWithIntegration(req, integration)
    }

    fun tryRegisterTeacher(req: RegisterTeacherEntity, integration: UUID) {
        if (integration != UUID(0,0)) return tryRegisterTeacherWithIntegration(req, integration)
    }
}