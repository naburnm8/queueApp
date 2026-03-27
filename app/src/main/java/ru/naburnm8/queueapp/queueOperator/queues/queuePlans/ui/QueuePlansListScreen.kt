package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.toReadableText
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansState
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun QueuePlansListScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: () -> Unit,
    vm: QueuePlansViewmodel
) {

    val state by vm.stateFlow.collectAsState()

    when (state) {
        is QueuePlansState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is QueuePlansState.Error -> {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = (state as QueuePlansState.Error).errorMessage) {
                onNavigateBack()
            }
        }
        is QueuePlansState.Main -> {
            QueuePlansListComponent(
                modifier = modifier,
                plansOfDisciplines = (state as QueuePlansState.Main).plansOfDisciplines,
                onDeletePlan = { plan, discipline ->
                    vm.deleteQueuePlan(plan.id ?: throw IllegalStateException("Plan id must be non-null"), discipline.id)
                },
                onEditPlan = {plan, discipline ->
                    vm.switchBundle(discipline, plan) {
                        onNavigateToEdit()
                    }
                },
                onCreatePlan = {
                    vm.switchBundle(it) {
                        onNavigateToCreate()
                    }
                }
            )
        }
    }

}

@Composable
private fun QueuePlansListComponent(
    modifier: Modifier = Modifier,
    plansOfDisciplines: Map<DisciplineEntity, List<QueuePlanEntity>>,
    onDeletePlan: (QueuePlanEntity, DisciplineEntity) -> Unit,
    onEditPlan: (QueuePlanEntity, DisciplineEntity) -> Unit,
    onCreatePlan: (DisciplineEntity) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.queue_plans),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                items = plansOfDisciplines.entries.toList(),
                key = { (discipline, _) -> discipline.id }
            ) { (discipline, plans) ->
                QueuePlansGroupItem(
                    discipline = discipline,
                    plans = plans,
                    onDeletePlan = {onDeletePlan(it, discipline) },
                    onEditPlan = {onEditPlan(it, discipline) },
                    onCreate = {onCreatePlan(discipline) }
                )
            }
        }
    }
}

@Composable
private fun QueuePlansGroupItem(
    modifier: Modifier = Modifier,
    discipline: DisciplineEntity,
    plans: List<QueuePlanEntity>,
    onDeletePlan: (QueuePlanEntity) -> Unit,
    onEditPlan: (QueuePlanEntity) -> Unit,
    onCreate: () -> Unit
) {
    var expanded by rememberSaveable(discipline.id, discipline.name) {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = discipline.name,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = plans.size.toString(),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onCreate() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
            ) {
                items(
                    items = plans,
                    key = { it.id ?: "${it.title}_${it.createdAt}" }
                ) { plan ->
                    QueuePlanItem(
                        plan = plan,
                        onDeletePlan = { onDeletePlan(plan) },
                        onEditPlan = { onEditPlan(plan) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QueuePlanItem(
    modifier: Modifier = Modifier,
    plan: QueuePlanEntity,
    onDeletePlan: () -> Unit,
    onEditPlan: () -> Unit
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
                Text("${stringResource(R.string.delete_queue_plan)} \"${plan.title}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeletePlan()
                    }
                ) {
                    Text(stringResource(R.string.delete_queue_plan))
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
                .clickable { onEditPlan() }
                .padding(12.dp),
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
                    text = plan.title,
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
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = formatPlanCreatedAt(plan.createdAt),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${stringResource(R.string.status)}: ${plan.status.toReadableText()}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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


private fun formatPlanCreatedAt(createdAt: java.time.Instant): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return formatter.format(
        createdAt.atZone(ZoneId.systemDefault()).toLocalDateTime()
    )
}
