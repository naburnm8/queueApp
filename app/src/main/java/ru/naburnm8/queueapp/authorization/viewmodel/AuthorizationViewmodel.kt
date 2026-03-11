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
import ru.naburnm8.queueapp.authorization.navigation.AuthorizationMainNavigation
import ru.naburnm8.queueapp.authorization.repository.AuthorizationRepository
import ru.naburnm8.queueapp.authorization.repository.IntegrationRepository
import ru.naburnm8.queueapp.authorization.request.LoginRequest


class AuthorizationViewmodel (
    private val sessionManager: SessionManager,
    private val repository: AuthorizationRepository,
) : ViewModel() {
    private var _stateFlow = MutableStateFlow<AuthorizationState>(AuthorizationState.Main(
        AuthorizationMainNavigation.LOGIN))

    val stateFlow = _stateFlow.asStateFlow()

    fun changeMainFlowState(state: AuthorizationMainNavigation) {
        _stateFlow.value = AuthorizationState.Main(state)
    }

    fun tryLogin(email: String, password: String) {

        viewModelScope.launch {
            _stateFlow.value = AuthorizationState.Loading
            runCatching {
                val response = repository.login(LoginRequest(email, password))
                if (response.isFailure) {
                    throw IOException(response.exceptionOrNull()?.message ?: "Unknown error")
                } else {
                    val body = response.getOrNull() ?: throw IllegalStateException("Response body is null")
                    sessionManager.initiateSession(body.accessToken, body.refreshToken, body.userRole)
                }
            }.onFailure {
                _stateFlow.value = AuthorizationState.Error(it.message ?: "Unknown error")
            }
            Log.d("AuthorizationViewmodel", "Current state: ${_stateFlow.value}")
        }


    }
}