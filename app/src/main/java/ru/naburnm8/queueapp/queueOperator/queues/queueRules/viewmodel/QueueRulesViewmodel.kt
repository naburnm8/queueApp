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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleMapper
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.repository.QueueRulesRepository
import java.util.UUID

class QueueRulesViewmodel (
    private val queueRulesRepository: QueueRulesRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<QueueRulesState>(QueueRulesState.Loading)

    val stateFlow = _stateFlow.asStateFlow()



    fun loadRules(queuePlan: QueuePlanEntity, onSuccess: () -> Unit) {
        _stateFlow.value = QueueRulesState.Loading
        viewModelScope.launch {
            loadRulesInner(queuePlan, onSuccess)
        }
    }

    private suspend fun loadRulesInner(queuePlan: QueuePlanEntity, onSuccess: () -> Unit) {
        runCatching {
            val result = queueRulesRepository.getRules(queuePlan.id!!)
            val rules = result.getOrThrow()
            _stateFlow.value = QueueRulesState.Main(
                queuePlan,
                rules.map { QueueRuleMapper.map(it)}
            )
            onSuccess()
        }.onFailure {
            _stateFlow.value = QueueRulesState.Error(it.message ?: "Unknown error")
        }
    }

    private inline fun <reified T> serializeBody(
        body: @Serializable T
    ): JsonElement {
        return Json.encodeToJsonElement(body)
    }

}