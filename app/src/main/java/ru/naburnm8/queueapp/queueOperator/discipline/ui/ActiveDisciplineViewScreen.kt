package ru.naburnm8.queueapp.queueOperator.discipline.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.queueOperator.discipline.entity.ActiveDisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.WorkTypeEntity
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineState
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import java.util.UUID


@Composable
fun ActiveDisciplineViewScreen(
    modifier: Modifier = Modifier,
    vm: DisciplineViewmodel,
    onNavigateToEdit: () -> Unit,
    onNavigateToAddOwners: () -> Unit,
) {

    val state = vm.stateFlow.collectAsState()

    when (state.value) {
        is DisciplineState.Loading -> {
            GenericLoadingScreen(modifier = modifier.fillMaxSize())
        }

        is DisciplineState.Error -> {
            GenericErrorScreen(modifier = modifier, errorMessage = (state.value as DisciplineState.Error).message) {
                vm.loadDisciplines()
            }
        }

        is DisciplineState.Main -> {
            ActiveDisciplineViewComponent(
                modifier = modifier,
                onEditClick = {
                    onNavigateToEdit()
                },
                onAddOwnersClick = {
                    onNavigateToAddOwners()
                },
                activeDiscipline = (state.value as DisciplineState.Main).activeDiscipline,
                onNewWorkTypeSubmit = {
                    vm.addWorkTypes(listOf(it))
                },
                onDeleteWorkTypeSubmit = {
                    vm.deleteWorkTypes(listOf(it.id ?: UUID(0, 0)))
                }
            )
        }

    }
}

@Composable
private fun ActiveDisciplineViewComponent(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onAddOwnersClick: () -> Unit,
    activeDiscipline: ActiveDisciplineEntity?,
    onNewWorkTypeSubmit: (WorkTypeEntity) -> Unit,
    onDeleteWorkTypeSubmit: (WorkTypeEntity) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        if (activeDiscipline == null) {
            Text("no active discipline")
            return@Column
        }
        Text(
            text = "active: " + activeDiscipline.discipline.name,
        )
        Text(
            text = "work types: " + activeDiscipline.workTypes.size,
        )
        Text(
            text = "owners: " + activeDiscipline.owners.size,
        )

        Button(
            onClick = {onEditClick()}
        ) {
            Text("to edit")
        }

        Button(
            onClick = {onAddOwnersClick()}
        ) {
            Text("to add owners")
        }

        Button(
            onClick = {onNewWorkTypeSubmit(
                WorkTypeEntity(
                    name = "Лаба 1",
                    estimatedTimeMinutes = 15
                )
            )}
        ) {
            Text("add worktype")
        }


    }
}