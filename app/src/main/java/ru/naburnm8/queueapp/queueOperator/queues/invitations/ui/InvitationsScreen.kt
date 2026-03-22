package ru.naburnm8.queueapp.queueOperator.queues.invitations.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueOperator.queues.invitations.entity.InvitationEntity
import ru.naburnm8.queueapp.queueOperator.queues.invitations.viewmodel.InvitationsState
import ru.naburnm8.queueapp.queueOperator.queues.invitations.viewmodel.InvitationsViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansState
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID


@Composable
fun InvitationsScreen(
    modifier: Modifier = Modifier,
    queuePlanVm: QueuePlansViewmodel,
    invitationsVm: InvitationsViewmodel,
    onNavigateBack: () -> Unit
) {
    val invitationsState by invitationsVm.stateFlow.collectAsState()
    val queuePlanState by queuePlanVm.stateFlow.collectAsState()

    when (invitationsState) {
        is InvitationsState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is InvitationsState.Error -> {
            GenericErrorScreen(modifier, (invitationsState as InvitationsState.Error).errorMessage) {
                onNavigateBack()
            }
        }
        is InvitationsState.Main -> {
            if (queuePlanState !is QueuePlansState.Main || (queuePlanState as QueuePlansState.Main).activePlan == null) {
                GenericErrorScreen(modifier, "Queue plan is not loaded") {
                    onNavigateBack()
                }
                return
            }
            InvitationsScreenComponent(
                modifier = modifier,
                invitations = (invitationsState as InvitationsState.Main).invitations,
                distinctGroups = (invitationsState as InvitationsState.Main).distinctGroups,
                queuePlan = (invitationsState as InvitationsState.Main).queuePlan,
                onDelete = {
                    invitationsVm.deleteInvitation((invitationsState as InvitationsState.Main).queuePlan.id!!, it.id)
                },
                onCreate = {
                    invitationsVm.createInvitation((invitationsState as InvitationsState.Main).queuePlan.id!!, it) {
                        onNavigateBack()
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun InvitationsScreenComponentPreview() {
    QueueAppTheme() {
        InvitationsScreenComponent(
            modifier = Modifier.fillMaxSize(),
            invitations = listOf(
                InvitationEntity(
                    id = UUID.randomUUID(),
                    queuePlanId = UUID.randomUUID(),
                    enabled = true,
                    code = "ABC123",
                    targetStudents = emptyList(),
                    createdAt = Instant.now().minusSeconds(3600),
                    expiresAt = Instant.now().plusSeconds(3600000),
                    maxUses = 10,
                    usedCount = 3
                ),
                InvitationEntity(
                    id = UUID.randomUUID(),
                    queuePlanId = UUID.randomUUID(),
                    enabled = false,
                    code = null,
                    targetGroup = "ИУ3-82Б",
                    targetStudents = emptyList(),
                    createdAt = Instant.now().minusSeconds(7200),
                    expiresAt = Instant.now().minusSeconds(3600),
                    maxUses = 5,
                    usedCount = 5
                )
            ),
            distinctGroups = listOf("Group A", "Group B", "Group C"),
            queuePlan = QueuePlanEntity.mock,
            onDelete = { },
            onCreate = { }
        )

    }
}

@Composable
fun InvitationsScreenComponent(
    modifier: Modifier = Modifier,
    invitations: List<InvitationEntity>,
    distinctGroups: List<String>,
    queuePlan: QueuePlanEntity,
    onDelete: (InvitationEntity) -> Unit,
    onCreate: (InvitationEntity) -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${stringResource(R.string.invitations_for)} ${queuePlan.title}",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,

        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
                .heightIn(max = 500.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = invitations,
                key = { it.id }
            ) { invitation ->
                InvitationItem(
                    invitation = invitation,
                    onDelete = { onDelete(invitation) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { showCreateDialog = true }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.create_invitation))
        }
    }

    if (showCreateDialog) {
        CreateInvitationDialog(
            queuePlanId = queuePlan.id!!,
            distinctGroups = distinctGroups,
            onDismiss = { showCreateDialog = false },
            onConfirm = {
                onCreate(it)
                showCreateDialog = false
            }
        )
    }
}


@Composable
private fun InvitationItem(
    modifier: Modifier = Modifier,
    invitation: InvitationEntity,
    onDelete: () -> Unit
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
        DeleteInvitationDialog(
            onDismiss = {
                showDeleteDialog = false
                scope.launch {
                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            }
        )
    }

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
        ) {
            Text(
                text = "${stringResource(R.string.created)}: ${formatInstant(invitation.createdAt)}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${stringResource(R.string.expires)}: ${formatInstant(invitation.expiresAt)}",
                color = if (invitation.expiresAt > Instant.now()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${stringResource(R.string.used_count)}: ${invitation.usedCount}/${invitation.maxUses}",
                color = if(invitation.maxUses > invitation.usedCount) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )

            if (!invitation.code.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(R.string.short_code)}: ${invitation.code}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }

            if (!invitation.targetGroup.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(R.string.group)}: ${invitation.targetGroup}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            val isActive = invitation.expiresAt > Instant.now() && invitation.enabled && invitation.usedCount < invitation.maxUses

            Text(
                text = if (isActive) stringResource(R.string.active) else stringResource(R.string.inactive),
                color = if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                fontSize = 14.sp
            )
        }
    }
}


@Composable
private fun DeleteInvitationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.confirm_request))
        },
        text = {
            Text(
                stringResource(R.string.delete_invitation_affirmation)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.delete_invitation))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateInvitationDialog(
    queuePlanId: UUID,
    distinctGroups: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (InvitationEntity) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var maxUses by remember { mutableStateOf("") }
    var expiresAtText by remember { mutableStateOf("") }

    var maxUsesError by remember { mutableStateOf(false) }
    var expiresAtError by remember { mutableStateOf(false) }

    val hasTarget = code.isNotBlank() || selectedGroup != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.create_invitation))
        },
        text = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.short_code)) },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedGroup ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.group)) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.not_chosen)) },
                            onClick = {
                                selectedGroup = null
                                expanded = false
                            }
                        )

                        distinctGroups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group) },
                                onClick = {
                                    selectedGroup = group
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = expiresAtText,
                    onValueChange = { expiresAtText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("${stringResource(R.string.expires)} (yyyy-MM-dd HH:mm)") },
                    singleLine = true,
                    isError = expiresAtError
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = maxUses,
                    onValueChange = {
                        maxUses = it.filter { ch -> ch.isDigit() }
                        maxUsesError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.max_uses)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = maxUsesError
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (!hasTarget) {
                    Text(
                        text = stringResource(R.string.at_least_one_target),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val parsedMaxUses = maxUses.toIntOrNull()
                    val parsedExpiresAt = parseLocalDateTimeToInstant(expiresAtText)

                    maxUsesError = parsedMaxUses == null || parsedMaxUses <= 0
                    expiresAtError = parsedExpiresAt == null



                    if (!maxUsesError && !expiresAtError && hasTarget) {
                        onConfirm(
                            InvitationEntity(
                                id = UUID(0,0),
                                queuePlanId = queuePlanId,
                                enabled = true,
                                code = code.trim().ifBlank { null },
                                targetGroup = selectedGroup,
                                targetStudents = emptyList(),
                                createdAt = Instant.now(),
                                expiresAt = parsedExpiresAt!!,
                                maxUses = parsedMaxUses!!,
                                usedCount = 0
                            )
                        )
                    }
                }
            ) {
                Text("Создать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

private fun formatInstant(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return instant
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

private fun parseLocalDateTimeToInstant(value: String): Instant? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        LocalDateTime.parse(value, formatter)
            .atZone(ZoneId.systemDefault())
            .toInstant()
    } catch (_: Exception) {
        null
    }
}