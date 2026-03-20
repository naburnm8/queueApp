package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplinesMapper
import ru.naburnm8.queueapp.queueOperator.discipline.repository.TeacherDisciplineRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlansMapper
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.item.QueuePlanItem
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.repository.QueuePlansRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import java.time.Instant
import java.util.UUID

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

    fun loadQueuePlans(onSuccess: () -> Unit = {}) {
        _stateFlow.value = QueuePlansState.Loading
        viewModelScope.launch {
            loadQueuePlansInner(onSuccess)
        }
    }

    fun switchBundle(discipline: DisciplineEntity, activePlan: QueuePlanEntity? = null, onSuccess: () -> Unit = {}) {
        if (_stateFlow.value !is QueuePlansState.Main) return
        val previousState = _stateFlow.value as QueuePlansState.Main
        _stateFlow.value = QueuePlansState.Loading
        viewModelScope.launch {
            runCatching {
                val me = profileRepository.getMeTeacher().getOrThrow()
                _stateFlow.value = QueuePlansState.Main(
                    plansOfDisciplines = previousState.plansOfDisciplines,
                    activePlan = activePlan,
                    activePlanBundle = ActiveQueuePlanBundle(
                        teacher = ProfileMapper.map(me),
                        discipline = discipline
                    )
                )
                onSuccess()
            }.onFailure {
                _stateFlow.value = QueuePlansState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun upsertPlanFromUi(item: QueuePlanItem, onSuccess: () -> Unit = {}) {
        if (_stateFlow.value !is QueuePlansState.Main) {
            Log.e("QueuePlansViewmodel", "Cannot upsert plan, state is not Main")
            return
        }
        val previousState = _stateFlow.value as QueuePlansState.Main
        _stateFlow.value = QueuePlansState.Loading
        viewModelScope.launch {
            runCatching {
                val newObject = QueuePlanEntity(
                    id = item.id,
                    createdByTeacherId = previousState.activePlanBundle?.teacher?.id ?: throw IllegalStateException("No active plan bundle"),
                    title = item.title,
                    slotDurationMinutes = item.slotDurationMinutes,
                    useDebts = item.useDebts,
                    wDebts = item.wDebts,
                    useTime = item.useTime,
                    wTime = item.wTime,
                    useAchievements = item.useAchievements,
                    wAchievements = item.wAchievements,
                    status = QueueStatus.DRAFT,
                    createdAt = Instant.now()
                )

                if (item.id == null) {
                    queuePlansRepository.createQueuePlan(previousState.activePlanBundle.discipline.id, QueuePlansMapper.toRequest(newObject)).getOrThrow()
                } else {
                    queuePlansRepository.updateQueuePlan(previousState.activePlanBundle.discipline.id, QueuePlansMapper.toRequest(newObject)).getOrThrow()
                }
                loadQueuePlansInner(onSuccess)
            }.onFailure {
                _stateFlow.value = QueuePlansState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun deleteQueuePlan(queuePlanId: UUID, disciplineId: UUID, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            runCatching {
                queuePlansRepository.deletePlan(disciplineId, queuePlanId).getOrThrow()
                loadQueuePlansInner(onSuccess)
            }.onFailure {
                _stateFlow.value = QueuePlansState.Error(it.message ?: "Unknown error")
            }
        }
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



}