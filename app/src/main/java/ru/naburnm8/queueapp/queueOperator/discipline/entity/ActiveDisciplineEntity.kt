package ru.naburnm8.queueapp.queueOperator.discipline.entity

import ru.naburnm8.queueapp.profile.entity.ProfileEntity

data class ActiveDisciplineEntity(
    val discipline: DisciplineEntity,
    val owners: List<ProfileEntity>,
    val workTypes: List<WorkTypeEntity>
)
