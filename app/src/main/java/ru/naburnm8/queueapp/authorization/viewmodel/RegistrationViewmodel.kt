package ru.naburnm8.queueapp.authorization.viewmodel

import android.util.Log
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
import ru.naburnm8.queueapp.authorization.request.RegisterStudentRequest
import ru.naburnm8.queueapp.viewmodel.BridgeRequest
import ru.naburnm8.queueapp.viewmodel.InterViewmodelBridge
import ru.naburnm8.queueapp.viewmodel.Method
import java.util.UUID

class RegistrationViewmodel(
    private val integrationsRepository: IntegrationRepository,
    private val registrationRepository: RegistrationRepository,
    private val sessionManager: SessionManager,
    private val bridge: InterViewmodelBridge
) : ViewModel() {
    private var _integrationsStateFlow = MutableStateFlow<RegistrationState>(RegistrationState.Loading)

    val integrationsStateFlow = _integrationsStateFlow.asStateFlow()

    fun clearAfterRegistration() {
        viewModelScope.launch {
            sessionManager.closeSession()
        }
    }

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
                Log.e(this::class.java.toString(), "Error fetching integrations: ${it.message}")
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
        viewModelScope.launch {
            runCatching {
                val result = registrationRepository.registerStudent(
                    RegisterStudentRequest(
                        email = req.email,
                        password = req.password,
                        firstName = req.firstName,
                        lastName = req.lastName,
                        patronymic = req.patronymic,
                        academicGroup = req.academicGroup,
                        telegram = req.telegram,
                        avatarUrl = req.avatarUrl
                    )
                )
                if (result.isFailure) throw IOException(result.exceptionOrNull()?.message ?: "Unknown error")
                val body = result.getOrNull() ?: throw IllegalStateException("Response body is null")
                _integrationsStateFlow.value = RegistrationState.RegistrationSuccess
            }.onFailure {
                bridge.sendMessage(
                    BridgeRequest(
                        RegistrationViewmodel::class.java.toString(),
                        AuthorizationViewmodel::class.java.toString(),
                        Method.ERROR.name
                    )
                )
            }
        }
    }

    fun tryRegisterTeacher(req: RegisterTeacherEntity, integration: UUID) {
        if (integration != UUID(0,0)) return tryRegisterTeacherWithIntegration(req, integration)
    }
}