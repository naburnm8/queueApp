package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplinesMapper
import ru.naburnm8.queueapp.queueOperator.discipline.repository.TeacherDisciplineRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlansMapper
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.repository.QueuePlansRepository

class QueuePlansViewmodel (
    private val profileRepository: ProfileRepository,
    private val disciplineRepository: TeacherDisciplineRepository,
    private val queuePlansRepository: QueuePlansRepository
) : ViewModel() {

    private var _stateFlow = MutableStateFlow<QueuePlansState>(QueuePlansState.Loading)

    val stateFlow = _stateFlow.asStateFlow()

    init {
        loadQueuePlans()
    }

    private suspend fun loadQueuePlansInner(onSuccess: () -> Unit = {}) {
        runCatching {
            val myDisciplines = disciplineRepository
                .getMyDisciplines()
                .getOrThrow()
                .disciplines
                .map {
                    DisciplinesMapper.map(it)
                }
            val newMap = mutableMapOf<DisciplineEntity, List<QueuePlanEntity>>()
            val myQueuePlans = queuePlansRepository
                .getMyQueuePlans()
                .getOrThrow()


            for (discipline in myDisciplines) {
                val queuePlans = myQueuePlans
                    .filter {it.disciplineId == discipline.id}
                    .map { QueuePlansMapper.map(it) }
                newMap[discipline] = queuePlans
            }

            _stateFlow.value = QueuePlansState.Main(newMap)
            onSuccess()
        }.onFailure {
            _stateFlow.value = QueuePlansState.Error(it.message ?: "Unknown error")
        }
    }

    fun loadQueuePlans(onSuccess: () -> Unit = {}) {
        _stateFlow.value = QueuePlansState.Loading
        viewModelScope.launch {
            loadQueuePlansInner(onSuccess)
        }
    }

}