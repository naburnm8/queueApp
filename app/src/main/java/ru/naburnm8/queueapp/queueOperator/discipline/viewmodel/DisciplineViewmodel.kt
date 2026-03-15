package ru.naburnm8.queueapp.queueOperator.discipline.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMapper
import ru.naburnm8.queueapp.queueOperator.discipline.entity.ActiveDisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplinesMapper
import ru.naburnm8.queueapp.queueOperator.discipline.entity.WorkTypeEntity
import ru.naburnm8.queueapp.queueOperator.discipline.repository.TeacherDisciplineRepository
import ru.naburnm8.queueapp.queueOperator.discipline.request.AddOwnersRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.AddWorkTypesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.UpdateDisciplinesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.UpdateWorkTypesRequest
import ru.naburnm8.queueapp.request.DeleteRequest
import java.util.UUID

class DisciplineViewmodel (
    private val repository: TeacherDisciplineRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<DisciplineState>(DisciplineState.Loading)

    val stateFlow = _stateFlow.asStateFlow()

    init {
        loadDisciplines()
    }

    private suspend fun loadDisciplinesInner() {
        val previousState = _stateFlow.value
        _stateFlow.value = DisciplineState.Loading
        runCatching {
            val result = repository.getMyDisciplines()
            if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
            val disciplines = result.getOrNull() ?: throw IOException("Body is null")

            if (previousState is DisciplineState.Main) {
                _stateFlow.value = DisciplineState.Main(DisciplinesMapper.map(disciplines), previousState.activeDiscipline)
                return@runCatching
            }

            _stateFlow.value = DisciplineState.Main(DisciplinesMapper.map(disciplines))
        }.onFailure {
            _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
        }
    }

    fun loadDisciplines() {
        viewModelScope.launch {
            loadDisciplinesInner()
        }
    }

    fun addOwners(disciplineId: UUID, ownerIds: List<UUID>) {
        viewModelScope.launch {
            runCatching {
                val result = repository.addOwners(disciplineId, AddOwnersRequest(ownerIds))
                if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
                loadDisciplinesInner()
            }.onFailure {
                _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun createDiscipline(req: DisciplineEntity) {
        viewModelScope.launch {
            runCatching {
                val result = repository.createDiscipline(DisciplinesMapper.toCreateRequest(req))
                if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
                loadDisciplinesInner()
            }.onFailure {
                _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
            }
        }
    }

    private suspend fun loadWorkTypes(disciplineId: UUID): List<WorkTypeEntity> {
        runCatching {
            val result = repository.getWorkTypesById(disciplineId)
            if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
            val workTypes = result.getOrNull() ?: throw IOException("Body is null")
            return DisciplinesMapper.map(workTypes)
        }.onFailure {
            throw(it)
        }
        return listOf() // Unreachable, but compiler requires it
    }

    private suspend fun loadOwners(disciplineId: UUID): List<ProfileEntity> {
        runCatching {
            val result = repository.getOwners(disciplineId)
            if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
            val owners = result.getOrNull() ?: throw IOException("Body is null")
            return owners.map {ProfileMapper.map(it)}
        }.onFailure {
            throw(it)
        }
        return listOf() // Unreachable, but compiler requires it
    }

    private suspend fun switchActiveDisciplineInner(discipline: DisciplineEntity) {
        val previousState = _stateFlow.value
        if (previousState !is DisciplineState.Main) return
        _stateFlow.value = DisciplineState.Loading
        runCatching {
            val workTypes = loadWorkTypes(discipline.id)
            val owners = loadOwners(discipline.id)
            _stateFlow.value = DisciplineState.Main(
                disciplines = previousState.disciplines,
                activeDiscipline = ActiveDisciplineEntity(
                    discipline = discipline,
                    workTypes = workTypes,
                    owners = owners
                )
            )
        }.onFailure {
            _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
        }
    }

    fun switchActiveDiscipline(discipline: DisciplineEntity) {
        viewModelScope.launch {
            switchActiveDisciplineInner(discipline)
        }
    }

    fun addWorkTypes(workTypes: List<WorkTypeEntity>, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val previousState = _stateFlow.value
            if (previousState !is DisciplineState.Main) return@launch
            if (previousState.activeDiscipline == null) return@launch
            _stateFlow.value = DisciplineState.Loading
            runCatching {
                val result = repository.addWorkTypes(
                    AddWorkTypesRequest(
                        disciplineId = previousState.activeDiscipline.discipline.id,
                        workTypes = workTypes.map { DisciplinesMapper.toDto(it) }
                    )
                )
                if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
                switchActiveDisciplineInner(previousState.activeDiscipline.discipline)
                onSuccess()
            }.onFailure {
                _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun deleteDisciplines(disciplineIds: List<UUID>, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val previousState = _stateFlow.value
            if (previousState !is DisciplineState.Main) return@launch
            _stateFlow.value = DisciplineState.Loading
            runCatching {
                val result = repository.deleteDisciplines(DeleteRequest(disciplineIds))
                if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
                loadDisciplinesInner()
                onSuccess()
            }.onFailure {
                _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun deleteWorkTypes(workTypeIds: List<UUID>, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val previousState = _stateFlow.value
            if (previousState !is DisciplineState.Main) return@launch
            if (previousState.activeDiscipline == null) return@launch
            _stateFlow.value = DisciplineState.Loading
            runCatching {
                val result = repository.deleteWorkTypes(DeleteRequest(workTypeIds))
                if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
                switchActiveDisciplineInner(previousState.activeDiscipline.discipline)
                onSuccess()
            }.onFailure {
                _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun updateDiscipline(newDiscipline: DisciplineEntity) {
        viewModelScope.launch {
            val previousState = _stateFlow.value
            if (previousState !is DisciplineState.Main) return@launch
            if (previousState.activeDiscipline == null) return@launch
            _stateFlow.value = DisciplineState.Loading
            runCatching {
                val result = repository.updateDisciplines(
                    UpdateDisciplinesRequest(
                        listOf(DisciplinesMapper.toDto(newDiscipline))
                    )
                )
                if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
                switchActiveDiscipline(newDiscipline)
                loadDisciplinesInner()
            }.onFailure {
                _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun updateWorkTypes(updated: List<WorkTypeEntity>) {
        viewModelScope.launch {
            val previousState = _stateFlow.value
            if (previousState !is DisciplineState.Main) return@launch
            if (previousState.activeDiscipline == null) return@launch
            _stateFlow.value = DisciplineState.Loading
            runCatching {
                val result = repository.updateWorkTypes(
                    UpdateWorkTypesRequest(updated.map { DisciplinesMapper.toDto(it) })
                )
                if (result.isFailure) throw result.exceptionOrNull() ?: IOException("Unknown error")
                switchActiveDisciplineInner(previousState.activeDiscipline.discipline)
            }.onFailure {
                _stateFlow.value = DisciplineState.Error(it.message ?: "Unknown error")
            }
        }
    }


}