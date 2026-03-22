package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesState
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericDeleteDialog
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import java.util.UUID

@Composable
fun QueueRuleListScreen(
    modifier: Modifier,
    plansVm: QueuePlansViewmodel,
    rulesVm: QueueRulesViewmodel,
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: () -> Unit
) {

    val rulesState by rulesVm.stateFlow.collectAsState()

    when (rulesState) {
        is QueueRulesState.Loading -> {
            GenericLoadingScreen(modifier)
        }

        is QueueRulesState.Error -> {
            GenericErrorScreen(
                modifier,
                errorMessage = (rulesState as QueueRulesState.Error).errorMessage
            ) {
                onNavigateBack()
            }
        }

        is QueueRulesState.Main -> {
            QueueRuleListComponent(
                modifier = modifier,
                queuePlan = (rulesState as QueueRulesState.Main).queuePlan,
                rules = (rulesState as QueueRulesState.Main).queueRules,
                onDelete = { rule ->
                    rulesVm.deleteRule(rule)
                },
                onEdit = { rule ->
                    rulesVm.switchActiveInputContext(rule) {
                        onNavigateToEdit()
                    }
                },
                onCreate = { type ->
                    rulesVm.switchInputContext(type) {
                        onNavigateToCreate()
                    }
                }
            )
        }
    }
}

@Composable
fun QueueRuleListComponent(
    modifier: Modifier = Modifier,
    queuePlan: QueuePlanEntity,
    rules: List<QueueRuleEntity>,
    onDelete: (QueueRuleEntity) -> Unit,
    onEdit: (QueueRuleEntity) -> Unit,
    onCreate: (RuleType) -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${stringResource(R.string.rules_of_plan)} \"${queuePlan.title}\"",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (rules.isEmpty()) {
            Text(
                text = stringResource(R.string.no_rules_yet),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        } else {
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
                    items = rules,
                    key = { it.id ?: "${it.type}_${it.queuePlanId}" }
                ) { rule ->
                    QueueRuleItem(
                        rule = rule,
                        onDelete = { onDelete(rule) },
                        onEdit = { onEdit(rule) }
                    )
                }
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
            Text(stringResource(R.string.create_rule))
        }
    }

    if (showCreateDialog) {
        SelectRuleTypeDialog(
            onDismiss = { showCreateDialog = false },
            onTypeSelected = { type ->
                showCreateDialog = false
                onCreate(type)
            }
        )
    }
}

@Composable
private fun QueueRuleItem(
    modifier: Modifier = Modifier,
    rule: QueueRuleEntity,
    onDelete: () -> Unit,
    onEdit: () -> Unit
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
        GenericDeleteDialog(
            text = stringResource(R.string.confirm_rule_deletion),
            onDismiss = { showDeleteDialog = false },
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onEdit() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = rule.type.toReadableText(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (rule.enabled) {
                            Icons.Default.Check
                        } else {
                            Icons.Default.Close
                        },
                        contentDescription = null,
                        tint = if (rule.enabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = if (rule.enabled) stringResource(R.string.active) else stringResource(R.string.inactive),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
private fun SelectRuleTypeDialog(
    onDismiss: () -> Unit,
    onTypeSelected: (RuleType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.choose_rule_type))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RuleType.entries.forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onTypeSelected(type) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = type.toReadableText(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
private fun RuleType.toReadableText(): String {
    return when (this) {
        RuleType.GROUP_BONUS -> stringResource(R.string.group_name_bonus)
        RuleType.IDENTIFIER_BONUS -> stringResource(R.string.identifier_bonus)
        RuleType.TIMESTAMP_BONUS -> stringResource(R.string.timestamp_bonus)
        RuleType.CUSTOM -> stringResource(R.string.custom_rule)
    }
}