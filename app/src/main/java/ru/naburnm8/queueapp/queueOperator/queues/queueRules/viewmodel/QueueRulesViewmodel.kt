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
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleMapper
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
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

    fun updateRule(queueRule: QueueRuleEntity, onSuccess: () -> Unit) {
        val currentState = _stateFlow.value
        if (currentState !is QueueRulesState.Main) {
            return
        }
        _stateFlow.value = QueueRulesState.Loading
        viewModelScope.launch {
            runCatching {
                queueRulesRepository.updateRule(queueRule.queuePlanId, QueueRuleMapper.toRequest(queueRule)).getOrThrow()
                loadRulesInner(currentState.queuePlan, onSuccess)
            }.onFailure {
                _stateFlow.value = QueueRulesState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun switchActiveInputContext(queueRule: QueueRuleEntity, onSuccess: () -> Unit) {
        val currentState = _stateFlow.value
        if (currentState !is QueueRulesState.Main) {
            return
        }
        _stateFlow.value = QueueRulesState.Loading
        viewModelScope.launch {
            currentState.activeRule = queueRule
            switchInputContextInner(currentState, queueRule.type, onSuccess)
        }
    }

    fun switchInputContext(type: RuleType, onSuccess: () -> Unit) {
        val currentState = _stateFlow.value
        if (currentState !is QueueRulesState.Main) {
            return
        }
        _stateFlow.value = QueueRulesState.Loading
        viewModelScope.launch {
            switchInputContextInner(currentState, type, onSuccess)
        }
    }

    fun deleteRule(queueRule: QueueRuleEntity, onSuccess: () -> Unit = {}) {
        val currentState = _stateFlow.value
        if (currentState !is QueueRulesState.Main) {
            return
        }
        _stateFlow.value = QueueRulesState.Loading
        viewModelScope.launch {
            runCatching {
                queueRulesRepository.removeRule(queueRule.queuePlanId, queueRule.id!!).getOrThrow()
                loadRulesInner(currentState.queuePlan, onSuccess)
            }.onFailure {
                _stateFlow.value = QueueRulesState.Error(it.message ?: "Unknown error")
            }
        }
    }

    private suspend fun switchInputContextInner(currentState: QueueRulesState.Main, type: RuleType, onSuccess: () -> Unit) {
        runCatching {
            when (type) {
                RuleType.GROUP_BONUS -> {
                    val groups = profileRepository.getAllDistinctGroups().getOrThrow()
                    _stateFlow.value = QueueRulesState.Main.GroupBonus(
                        currentState.queuePlan,
                        currentState.queueRules,
                        currentState.activeRule,
                        groups
                    )
                }

                RuleType.TIMESTAMP_BONUS -> {
                    _stateFlow.value = QueueRulesState.Main.OtherType(
                        currentState.queuePlan,
                        currentState.queueRules,
                        currentState.activeRule,
                    )
                }

                RuleType.IDENTIFIER_BONUS -> {
                    val groups = profileRepository.getAllDistinctGroups().getOrThrow()
                    val studentsToGroups = mutableMapOf<String, List<ProfileEntity>>()

                    for (group in groups) {
                        val students = profileRepository.getAllOrByGroupStudents(group).getOrThrow()
                        studentsToGroups[group] = students.map { ProfileMapper.map(it) }
                    }

                    _stateFlow.value = QueueRulesState.Main.IdentifierBonus(
                        currentState.queuePlan,
                        currentState.queueRules,
                        currentState.activeRule,
                        studentsToGroups
                    )
                }

                RuleType.CUSTOM -> {
                    throw NotImplementedError("Custom rule type is not implemented yet")
                }
            }
            onSuccess()
        }.onFailure {
            _stateFlow.value = QueueRulesState.Error(it.message ?: "Unknown error")
        }
    }

    fun createRule(queueRule: QueueRuleEntity, onSuccess: () -> Unit) {
        val previousState = _stateFlow.value
        if (previousState !is QueueRulesState.Main) {
            return
        }
        _stateFlow.value = QueueRulesState.Loading
        viewModelScope.launch {
            runCatching {
                queueRulesRepository.addRule(queueRule.queuePlanId, QueueRuleMapper.toRequest(queueRule)).getOrThrow()
                loadRulesInner(previousState.queuePlan, onSuccess)
            }.onFailure {
                _stateFlow.value = QueueRulesState.Error(it.message ?: "Unknown error")
            }
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