package ru.naburnm8.queueapp.queueOperator.discipline.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.ActiveDisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity

sealed class DisciplineState {
    object Loading : DisciplineState()
    data class Error(val message: String) : DisciplineState()
    data class Main(
        val disciplines: List<DisciplineEntity>,
        val activeDiscipline: ActiveDisciplineEntity? = null
    ) : DisciplineState()
}