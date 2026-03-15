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
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineState
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen


@Composable
fun DisciplineListScreen(
    modifier: Modifier = Modifier,
    vm: DisciplineViewmodel,
    onNavigateToActiveDisciplineScreen: () -> Unit,
    onNavigateToDisciplineCreation: () -> Unit,
    onNavigateToCreate: () -> Unit,
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
            DisciplineListComponent(
                modifier = modifier,
                disciplines = (state.value as DisciplineState.Main).disciplines,
                onDisciplineClick = {
                    vm.switchActiveDiscipline(it)
                    onNavigateToActiveDisciplineScreen()
                },
                onDeleteDisciplineClick = {
                    vm.deleteDisciplines(listOf(it.id))
                },
                onCreateDisciplineClick = {
                    onNavigateToDisciplineCreation()
                }
            )
        }
    }
}

@Composable
private fun DisciplineListComponent(
    modifier: Modifier = Modifier,
    disciplines: List<DisciplineEntity>,
    onDisciplineClick: (DisciplineEntity) -> Unit,
    onDeleteDisciplineClick: (DisciplineEntity) -> Unit,
    onCreateDisciplineClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = disciplines.size.toString() + " дисциплин",
        )
        Button (
            onClick = {onDisciplineClick(disciplines[0])}
        ) {
            Text("active view")
        }
        Button (
            onClick = {onCreateDisciplineClick()}
        ) {
            Text("create")
        }
        Button(
            onClick = {onDeleteDisciplineClick(disciplines[0])}
        ) {
            Text("delete")
        }

    }


}