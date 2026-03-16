package ru.naburnm8.queueapp.queueOperator.discipline.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMultifieldType
import ru.naburnm8.queueapp.queueOperator.discipline.entity.ActiveDisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.WorkTypeEntity
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineState
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID


@Composable
fun ActiveDisciplineViewScreen(
    modifier: Modifier = Modifier,
    vm: DisciplineViewmodel,
    onNavigateToEdit: () -> Unit,
    onNavigateToAddOwners: () -> Unit,
    onNavigateBackToRoot: () -> Unit
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
                },
                onNavigateBackToRoot = {
                    onNavigateBackToRoot()
                },
                onEditWorkTypeSubmit = {
                    vm.updateWorkTypes(listOf(it))
                },
                onLeaveClick = {
                    vm.leaveDiscipline {
                        onNavigateBackToRoot()
                    }
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
    onNavigateBackToRoot: () -> Unit,
    onEditWorkTypeSubmit: (WorkTypeEntity) -> Unit,
    onLeaveClick: () -> Unit
) {

    var dialogShown by remember { mutableStateOf(false) }

    if (dialogShown) {
        ConfirmLeaveDialog(
            onDismiss = {dialogShown = false},
            onConfirm = {
                dialogShown = false
                onLeaveClick()
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (activeDiscipline == null) {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = stringResource(R.string.something_went_wrong)
            ) {
                onNavigateBackToRoot()
            }
            return@Column
        }

        Text(
            text = activeDiscipline.discipline.name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onEditClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }

            Button(
                {
                    dialogShown = true
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.leave)
                )
            }
        }




        Spacer(modifier = Modifier.height(24.dp))

        OwnersComponent(
            owners = activeDiscipline.owners
        ) {
            onAddOwnersClick()
        }

        Spacer(modifier = Modifier.height(24.dp))

        WorkTypesComponent(
            workTypes = activeDiscipline.workTypes,
            onNewWorkTypeSubmit = { onNewWorkTypeSubmit(it) },
            onDeleteWorkTypeSubmit = { onDeleteWorkTypeSubmit(it) },
            onEditWorkTypeSubmit = { onEditWorkTypeSubmit(it) }
        )
    }
}

@Composable
private fun ConfirmLeaveDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.confirm_request))
        },
        text = {
            Text(stringResource(R.string.leave_discipline_affirmation))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.leave))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
private fun EditWorkTypeDialog(
    workType: WorkTypeEntity,
    onDismiss: () -> Unit,
    onConfirm: (WorkTypeEntity) -> Unit
) {
    var name by remember(workType) { mutableStateOf(workType.name) }
    var timeMinutes by remember(workType) { mutableStateOf(workType.estimatedTimeMinutes.toString()) }

    var nameError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.edit_work_type))
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true,
                    isError = nameError
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = timeMinutes,
                    onValueChange = {
                        timeMinutes = it.filter { ch -> ch.isDigit() }
                        timeError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.time_in_minutes)) },
                    singleLine = true,
                    isError = timeError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trimmedName = name.trim()
                    val parsedTime = timeMinutes.toIntOrNull()

                    nameError = trimmedName.isBlank()
                    timeError = parsedTime == null || parsedTime <= 0

                    if (!nameError && !timeError) {
                        onConfirm(
                            workType.copy(
                                name = trimmedName,
                                estimatedTimeMinutes = parsedTime!!
                            )
                        )
                    }
                }
            ) {
                Text(stringResource(R.string.submit))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun WorkTypesComponent(
    modifier: Modifier = Modifier,
    workTypes: List<WorkTypeEntity>,
    onNewWorkTypeSubmit: (WorkTypeEntity) -> Unit,
    onDeleteWorkTypeSubmit: (WorkTypeEntity) -> Unit,
    onEditWorkTypeSubmit: (WorkTypeEntity) -> Unit,
) {
    var showAddDialog by remember { mutableStateOf(false) }

    var editingWorkType by remember {mutableStateOf<WorkTypeEntity?>(null)}

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.work_types),
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = workTypes,
                key = { it.id ?: "${it.name}_${it.estimatedTimeMinutes}" }
            ) { workType ->
                WorkTypeItem(
                    workType = workType,
                    onDeleteConfirmed = {
                        onDeleteWorkTypeSubmit(workType)
                    },
                    onClick = {
                        editingWorkType = workType
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { showAddDialog = true }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.add_work_type))
        }
    }

    editingWorkType?.let { workType ->
        EditWorkTypeDialog(
            workType = workType,
            onDismiss = {
                editingWorkType = null
            },
            onConfirm = { updatedWorkType ->
                onEditWorkTypeSubmit(updatedWorkType)
                editingWorkType = null
            }
        )
    }

    if (showAddDialog) {
        AddWorkTypeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, timeMinutes ->
                onNewWorkTypeSubmit(
                    WorkTypeEntity(
                        id = null,
                        name = name,
                        estimatedTimeMinutes = timeMinutes
                    )
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddWorkTypeDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, timeMinutes: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var timeMinutes by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.add_work_type))
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true,
                    isError = nameError
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = timeMinutes,
                    onValueChange = {
                        timeMinutes = it.filter { ch -> ch.isDigit() }
                        timeError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.time_in_minutes)) },
                    singleLine = true,
                    isError = timeError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trimmedName = name.trim()
                    val parsedTime = timeMinutes.toIntOrNull()

                    nameError = trimmedName.isBlank()
                    timeError = parsedTime == null || parsedTime <= 0

                    if (!nameError && !timeError) {
                        onConfirm(trimmedName, parsedTime!!)
                    }
                }
            ) {
                Text(stringResource(R.string.submit))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
private fun WorkTypeItem(
    modifier: Modifier = Modifier,
    workType: WorkTypeEntity,
    onDeleteConfirmed: () -> Unit,
    onClick: () -> Unit = {}
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
                scope.launch {
                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            title = {
                Text(stringResource(R.string.confirm_request))
            },
            text = {
                Text("${stringResource(R.string.deletion_affirmation)} \"${workType.name}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteConfirmed()
                    }
                ) {
                    Text(stringResource(R.string.delete_work_type))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        scope.launch {
                            dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                        }
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
                    contentDescription = null,
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
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = workType.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${workType.estimatedTimeMinutes} ${stringResource(R.string.minutes_short)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}


@Composable
private fun OwnerItem(
    modifier: Modifier = Modifier,
    owner: ProfileEntity
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.height(48.dp).width(48.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${owner.lastName} ${owner.firstName} ${owner.patronymic ?: ""}",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.height(16.dp).width(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.width(4.dp))
                Text (
                    text = owner.multifield,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.width(8.dp))
                if (owner.telegram != null) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp).width(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(4.dp))
                    Text (
                        text = "@${owner.telegram}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }

}


@Composable
private fun OwnersComponent(
    modifier: Modifier = Modifier,
    owners: List<ProfileEntity>,
    onAddOwnersClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.owners),
            textAlign = TextAlign.Left,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 150.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(owners) {
                OwnerItem(owner = it)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onAddOwnersClick() }) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.add_owners))
            }
        }
    }
}

@Composable
@Preview
private fun ActiveDisciplineViewComponentPreview() {
    QueueAppTheme() {
        ActiveDisciplineViewComponent(
            modifier = Modifier.fillMaxSize(),
            onEditClick = {},
            onAddOwnersClick = {},
            activeDiscipline = ActiveDisciplineEntity(
                discipline = DisciplineEntity(
                    name = "Технология программирования",
                    personalAchievementsScoreLimit = 100,
                ),
                workTypes = listOf(
                    WorkTypeEntity(
                        id = UUID(0, 0),
                        name = "Лабораторная работа",
                        estimatedTimeMinutes = 15
                    ),
                    WorkTypeEntity(
                        id = UUID(0, 1),
                        name = "Практическая работа",
                        estimatedTimeMinutes = 20
                    )
                ),
                owners = listOf(
                    ProfileEntity(
                        id = UUID(0, 0),
                        firstName = "Артем",
                        lastName = "Линт",
                        patronymic = "Дмитриевич",
                        multifield = "ИУ3",
                        multifieldType = ProfileMultifieldType.DEPARTMENT,
                        telegram = "naburnm8",
                        avatarUrl = ""
                    ),
                    ProfileEntity(
                        id = UUID(0, 0),
                        firstName = "Артем",
                        lastName = "Линт",
                        patronymic = "Дмитриевич",
                        multifield = "ИУ3",
                        multifieldType = ProfileMultifieldType.DEPARTMENT,
                        telegram = "naburnm8",
                        avatarUrl = ""
                    )
                )
            ),
            onNewWorkTypeSubmit = {},
            onDeleteWorkTypeSubmit = {},
            onNavigateBackToRoot = {},
            onEditWorkTypeSubmit = {},
            onLeaveClick = {}
        )
    }
}