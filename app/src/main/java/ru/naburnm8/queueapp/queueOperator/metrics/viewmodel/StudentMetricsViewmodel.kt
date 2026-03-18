package ru.naburnm8.queueapp.queueOperator.metrics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplinesMapper
import ru.naburnm8.queueapp.queueOperator.discipline.repository.TeacherDisciplineRepository
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplinesResponse
import ru.naburnm8.queueapp.queueOperator.metrics.entity.StudentMetricEntity
import ru.naburnm8.queueapp.queueOperator.metrics.entity.StudentMetricMapper
import ru.naburnm8.queueapp.queueOperator.metrics.repository.StudentMetricsRepository
import kotlin.runCatching

class StudentMetricsViewmodel(
    private val studentMetricsRepository: StudentMetricsRepository,
    private val disciplinesRepository: TeacherDisciplineRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private var _stateFlow = MutableStateFlow<StudentMetricsState>(StudentMetricsState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        loadMetrics()
    }

    fun editMetric(req: StudentMetricEntity, onSuccess: () -> Unit = {}) {
        if (req.id == null) {
            return
        }
        _stateFlow.value = StudentMetricsState.Loading
        viewModelScope.launch {
            runCatching {
                studentMetricsRepository.updateStudentMetrics(
                    req.discipline.id,
                    req.student.id,
                    StudentMetricMapper.toRequest(req)
                )
                loadMetricsInner { onSuccess() }
            }.onFailure {
                _stateFlow.value = StudentMetricsState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun deleteMetric(req: StudentMetricEntity, onSuccess: () -> Unit = {}) {
        if (req.id == null) {
            return
        }
        _stateFlow.value = StudentMetricsState.Loading
        viewModelScope.launch {
            runCatching {
                studentMetricsRepository.deleteMetrics(req.id)
                loadMetricsInner { onSuccess() }
            }.onFailure {
                _stateFlow.value = StudentMetricsState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun createMetric(req: StudentMetricEntity, onSuccess: () -> Unit = {}) {
        _stateFlow.value = StudentMetricsState.Loading
        viewModelScope.launch {
            runCatching {
                studentMetricsRepository.createStudentMetrics(
                    req.discipline.id,
                    req.student.id,
                    StudentMetricMapper.toRequest(req)
                ).getOrNull() ?: throw IllegalStateException("body is null")
                loadMetricsInner { onSuccess() }
            }.onFailure {
                _stateFlow.value = StudentMetricsState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun loadMetrics(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _stateFlow.value = StudentMetricsState.Loading
            loadMetricsInner(onSuccess)
        }
    }
    private suspend fun switchCreatingForInner(discipline: DisciplineEntity, previousState: StudentMetricsState.Main) {
        _stateFlow.value = StudentMetricsState.Loading
        runCatching {
            val me = profileRepository.getMeTeacher().getOrNull() ?: throw IllegalStateException("body is null")
            val groups = profileRepository.getAllDistinctGroups().getOrNull() ?: throw IllegalStateException("body is null")
            val newMap = mutableMapOf<String, List<ProfileEntity>>()

            for (group in groups) {
                val students = profileRepository.getAllOrByGroupStudents(group).getOrNull() ?: throw IllegalStateException("body is null")
                newMap[group] = students.map { ProfileMapper.map(it) }
            }

            val newState = StudentMetricsState.Main(
                metricToDiscipline = previousState.metricToDiscipline,
                activeMetric = previousState.activeMetric,
                creatingFor = CreatingForBundle(
                    discipline = discipline,
                    teacher = ProfileMapper.map(me),
                    studentsByGroup = newMap
                )
            )
            _stateFlow.value = newState
        }.onFailure {
            _stateFlow.value = StudentMetricsState.Error(it.message ?: "Unknown error")
        }
    }
    fun switchCreatingFor(discipline: DisciplineEntity) {
        val previousState = _stateFlow.value
        if (previousState !is StudentMetricsState.Main) {
            return
        }
        viewModelScope.launch {
            switchCreatingForInner(discipline, previousState)
        }
    }

    private fun putActiveMetric(previousState: StudentMetricsState.Main, metric: StudentMetricEntity) {
        viewModelScope.launch {
            switchCreatingForInner(metric.discipline, previousState)
            val currentState = _stateFlow.value
            val newState = StudentMetricsState.Main(
                metricToDiscipline = previousState.metricToDiscipline,
                activeMetric = metric,
                creatingFor = (currentState as StudentMetricsState.Main).creatingFor
            )
            _stateFlow.value = newState
        }

    }

    fun switchActiveMetric(metric: StudentMetricEntity) {
        val currentState = _stateFlow.value
        if (currentState is StudentMetricsState.Main) {
            putActiveMetric(currentState, metric)
        }
    }
    private suspend fun getMyDisciplines(): List<DisciplineEntity> {
        val result = disciplinesRepository.getMyDisciplines().getOrNull() ?: throw IllegalStateException("body is null")
        return DisciplinesMapper.map(result)
    }


    private suspend fun loadMetricsInner(onSuccess: () -> Unit = {}) {
        val newMap = mutableMapOf<DisciplineEntity, List<StudentMetricEntity>>()
        runCatching {
            val disciplines = getMyDisciplines()

            for (discipline in disciplines) {
                val metricsResult =
                    studentMetricsRepository.metricsByDiscipline(discipline.id).getOrNull()
                        ?: emptyList()
                newMap[discipline] = metricsResult.map {
                    StudentMetricMapper.map(it)
                }
            }

            _stateFlow.value = StudentMetricsState.Main(newMap)
            onSuccess()

        }.onFailure {
            _stateFlow.value = StudentMetricsState.Error(it.message ?: "Unknown error")
        }
    }



}