package ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleMapper
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.repository.QueueRulesRepository
import java.util.UUID

class QueueRulesViewmodel (
    private val queueRulesRepository: QueueRulesRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<QueueRulesState>(QueueRulesState.Loading)

    val stateFlow = _stateFlow.asStateFlow()



    fun loadRules(queuePlanId: UUID, onSuccess: () -> Unit) {
        _stateFlow.value = QueueRulesState.Loading
        viewModelScope.launch {
            loadRulesInner(queuePlanId, onSuccess)
        }
    }

    private suspend fun loadRulesInner(queuePlanId: UUID, onSuccess: () -> Unit) {
        runCatching {
            val result = queueRulesRepository.getRules(queuePlanId)
            val rules = result.getOrThrow()
            _stateFlow.value = QueueRulesState.Main(
                queuePlanId,
                rules.map { QueueRuleMapper.map(it)}
            )
            onSuccess()
        }.onFailure {
            _stateFlow.value = QueueRulesState.Error(it.message ?: "Unknown error")
        }
    }

    private inline fun <reified T> serializeBody(
        body: @Serializable T
    ): String {
        return Json.encodeToString(body)
    }

}