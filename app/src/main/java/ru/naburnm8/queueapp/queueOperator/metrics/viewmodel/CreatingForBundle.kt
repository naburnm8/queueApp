package ru.naburnm8.queueapp.queueOperator.metrics.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity

data class CreatingForBundle(
    val discipline: DisciplineEntity,
    val teacher: ProfileEntity,
    val studentsByGroup: Map<String, List<ProfileEntity>>
)
