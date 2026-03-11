package ru.naburnm8.queueapp.authorization.session

sealed class SessionState {
    data object Unauthorized : SessionState()
    data object Student : SessionState()
    data object Teacher : SessionState()
}