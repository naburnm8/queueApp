package ru.naburnm8.queueapp.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import ru.naburnm8.queueapp.authorization.repository.AuthorizationRepository
import ru.naburnm8.queueapp.authorization.request.LogoutRequest
import ru.naburnm8.queueapp.authorization.session.SessionManager
import ru.naburnm8.queueapp.authorization.session.SessionRepository
import ru.naburnm8.queueapp.authorization.session.SessionState
import ru.naburnm8.queueapp.authorization.token.TokenStorage
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.profile.entity.UpdateProfileEntity
import ru.naburnm8.queueapp.profile.navigation.ProfileNavigation
import ru.naburnm8.queueapp.profile.repository.ProfileRepository

class ProfileViewmodel(
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager,
    private val authorizationRepository: AuthorizationRepository,
    private val sessionRepository: SessionRepository,
    private val tokenStorage: TokenStorage

) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    fun loadMyProfile() {
        viewModelScope.launch {
            _stateFlow.value = ProfileState.Loading
            runCatching {
                val sessionState = sessionRepository.resolveSession()

                when (sessionState) {
                    is SessionState.Student -> {
                        val resultResponse = profileRepository.getMeStudent()
                        if (resultResponse.isFailure) throw IOException("Request failed")

                        val result = resultResponse.getOrNull() ?: throw IllegalStateException("Body is null")
                        val entity = ProfileMapper.map(result)
                        _stateFlow.value = ProfileState.Ready(entity, ProfileNavigation.MAIN)
                    }
                    is SessionState.Teacher -> {
                        val resultResponse = profileRepository.getMeTeacher()
                        if (resultResponse.isFailure) throw IOException("Request failed")

                        val result = resultResponse.getOrNull() ?: throw IllegalStateException("Body is null")
                        val entity = ProfileMapper.map(result)
                        _stateFlow.value = ProfileState.Ready(entity, ProfileNavigation.MAIN)
                    }
                    else -> {
                        throw IllegalStateException("Unauthorized")
                    }
                }
            }.onFailure {
                _stateFlow.value = ProfileState.Error(it.message ?: "")
            }

        }
    }

    fun updateProfile(entity: UpdateProfileEntity) {
        if (_stateFlow.value !is ProfileState.Ready) return
        viewModelScope.launch {
            _stateFlow.value = ProfileState.Loading
            runCatching {
                val updatedResponse = profileRepository.updateMe(ProfileMapper.toRequest(entity))
                if (updatedResponse.isFailure) throw IOException("Request failed")
                val updatedDto =  updatedResponse.getOrNull() ?: throw IllegalStateException("Body is null")
                val updated = ProfileMapper.map(updatedDto)

                val state = _stateFlow.value as ProfileState.Ready

                val newProfile = ProfileEntity(
                    id = state.profile.id,
                    firstName = updated.firstName,
                    lastName = updated.lastName,
                    patronymic = updated.patronymic,
                    telegram = updated.telegram,
                    multifield = updated.multifield,
                    multifieldType = updated.multifieldType,
                    avatarUrl = updated.avatarUrl
                )
                _stateFlow.value = ProfileState.Ready(newProfile, ProfileNavigation.MAIN)
            }.onFailure {
                _stateFlow.value = ProfileState.Error(it.message ?: "Unknown error")
            }
        }

    }

    fun logout() {
        viewModelScope.launch {
            _stateFlow.value = ProfileState.Loading
            runCatching {
                val refreshToken = tokenStorage.getRefreshToken() ?: throw IllegalStateException("Unauthorized")

                val result = authorizationRepository.logout(
                    LogoutRequest(refreshToken)
                )

                if (result.isSuccess) {
                    sessionManager.logout()
                } else {
                    throw IllegalStateException("Failed logout")
                }
            }.onFailure {
                _stateFlow.value = ProfileState.Error(it.message ?: "")
            }
        }
    }

}