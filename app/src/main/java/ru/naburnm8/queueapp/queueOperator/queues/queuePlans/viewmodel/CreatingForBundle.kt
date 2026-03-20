package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel

import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity

data class CreatingForBundle(
    val teacher: ProfileEntity,
    val discipline: DisciplineEntity
)
