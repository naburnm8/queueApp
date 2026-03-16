package ru.naburnm8.queueapp.queueOperator.discipline.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineState
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID


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
                },
                onRefresh = {
                    vm.loadDisciplines()
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
    onRefresh: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.your_disciplines),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (disciplines.isEmpty()) {
            Text(
                text = stringResource(R.string.your_disciplines_empty),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
                    .padding(16.dp),

            ) {
                items(items = disciplines) { item ->
                    DisciplineListItem(
                        modifier = Modifier.padding(8.dp),
                        discipline = item,
                        onDisciplineClick = {onDisciplineClick(item)},
                        onDeleteClick = {onDeleteDisciplineClick(item)},
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {onCreateDisciplineClick()}
        ) {
            Row() {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_discipline)
                )
            }
        }


    }
}

@Composable
private fun DisciplineListItem(
    modifier: Modifier = Modifier,
    discipline: DisciplineEntity,
    onDisciplineClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
                false
            } else {
                false
            }
        }
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                scope.launch { dismissState.reset() }
            },
            title = {
                Text(stringResource(R.string.confirm_request))
            },
            text = {
                Text("${stringResource(R.string.deletion_affirmation)} \"${discipline.name}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text(stringResource(R.string.delete_discipline))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        scope.launch { dismissState.reset() }
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp)),
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_discipline),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { onDisciplineClick() }
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = discipline.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
@Preview
private fun DisciplineListComponentPreview() {
    QueueAppTheme() {
        DisciplineListComponent(
            disciplines = listOf(
                DisciplineEntity(
                    id = UUID(1,0),
                    name = "Технология программирования",
                    personalAchievementsScoreLimit = 100
                ),
                DisciplineEntity(
                    id = UUID(2,0),
                    name = "Инфокоммуникационные системы и сети",
                    personalAchievementsScoreLimit = 100
                )
            ),
            onDisciplineClick = {},
            onDeleteDisciplineClick = {},
            onCreateDisciplineClick = {},
            onRefresh = {}
        )
    }
}

@Composable
@Preview
private fun DisciplineListComponentPreviewDark() {
    QueueAppTheme(darkTheme = true) {
        DisciplineListComponent(
            disciplines = listOf(
                DisciplineEntity(
                    id = UUID(1,0),
                    name = "Технология программирования",
                    personalAchievementsScoreLimit = 100
                ),
                DisciplineEntity(
                    id = UUID(2,0),
                    name = "Инфокоммуникационные системы и сети",
                    personalAchievementsScoreLimit = 100
                )
            ),
            onDisciplineClick = {},
            onDeleteDisciplineClick = {},
            onCreateDisciplineClick = {},
            onRefresh = {}
        )
    }
}