package ru.naburnm8.queueapp.queueOperator.queues.ui

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
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.toReadableText
import ru.naburnm8.queueapp.queueOperator.queues.viewmodel.QueuesState
import ru.naburnm8.queueapp.queueOperator.queues.viewmodel.QueuesViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID

@Composable
fun QueuesScreen (
    modifier: Modifier = Modifier,
    vm: QueuesViewmodel,
    onNavigateToQueuePlans: () -> Unit,
    onNavigateToQueue: (UUID) -> Unit,
) {
    val state by vm.stateFlow.collectAsState()

    when (state) {
        is QueuesState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is QueuesState.Error -> {
            GenericErrorScreen(
                modifier,
                (state as QueuesState.Error).errorMessage
            ) {
                vm.loadQueues()
            }
        }
        is QueuesState.Main -> {
            QueuesComponent(
                modifier = modifier,
                queues = (state as QueuesState.Main).queues,
                queuePlans = (state as QueuesState.Main).queuePlans,
                onStatusToggle = {
                    vm.toggleStatus(it)
                },
                onQueueClick = {
                    onNavigateToQueue(it)
                },
                onQueuePlansClick = {
                    onNavigateToQueuePlans()
                },
                onRefresh = {
                    vm.loadQueues()
                }
            )
        }
    }
}

@Composable
@Preview
fun QueuesListComponentPreview(){
    QueueAppTheme() {
        QueuesComponent(
            modifier = Modifier.fillMaxSize(),
            queues = listOf(QueueSnapshotEntity.mockActive),
            queuePlans = listOf(QueuePlanEntity.mock),
            onStatusToggle = {},
            onQueueClick = {},
            onRefresh = {},
            onQueuePlansClick = {}
        )
    }
}


@Composable
fun QueuesComponent(
    modifier: Modifier = Modifier,
    queues: List<QueueSnapshotEntity>,
    queuePlans: List<QueuePlanEntity>,
    onStatusToggle: (UUID) -> Unit,
    onQueueClick: (UUID) -> Unit,
    onRefresh: () -> Unit,
    onQueuePlansClick: () -> Unit,
) {
    val activeQueues = queues.filter { it.queueStatus == QueueStatus.ACTIVE }
    val draftQueues = queues.filter { it.queueStatus == QueueStatus.DRAFT }
    val closedQueues = queues.filter { it.queueStatus == QueueStatus.CLOSED }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.my_queues),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onRefresh
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Button(
                onClick = onQueuePlansClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.queue),
                    contentDescription = stringResource(R.string.queue_plans),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (activeQueues.isNotEmpty()) {
            QueueListComponent(
                queues = activeQueues,
                queuePlans = queuePlans,
                onQueueClick = { onQueueClick(it) },
                onStatusToggle = {onStatusToggle(it)}
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (draftQueues.isNotEmpty()) {
            QueueListComponent(
                queues = draftQueues,
                queuePlans = queuePlans,
                onQueueClick = {onQueueClick(it)},
                onStatusToggle = {onStatusToggle(it)}
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (closedQueues.isNotEmpty()) {
            QueueListComponent(
                queues = closedQueues,
                queuePlans = queuePlans,
                onQueueClick = {onQueueClick(it)},
                onStatusToggle = {onStatusToggle(it)}
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (activeQueues.isEmpty() && draftQueues.isEmpty() && closedQueues.isEmpty()) {
            Text(
                text = stringResource(R.string.you_have_no_queues)
            )
        }

    }
}


@Composable
fun QueueListComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    queues: List<QueueSnapshotEntity>,
    queuePlans: List<QueuePlanEntity>,
    onQueueClick: (UUID) -> Unit,
    onStatusToggle: (UUID) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            
    ) {
        Text(
            text = title ?: "${queues[0].queueStatus.toReadableText()}: ",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            items(
                items = queues,
                key = {it.queuePlanId}
            ) {queue ->
                QueueListItem(
                    queuePlan = queuePlans.find {it.id == queue.queuePlanId},
                    queue = queue,
                    onQueueClick = {onQueueClick(queue.queuePlanId)},
                    onStatusToggle = {onStatusToggle(queue.queuePlanId)}
                )
            }
        }
    }

}

@Composable
fun QueueListItem(
    modifier: Modifier = Modifier,
    queuePlan: QueuePlanEntity?,
    queue: QueueSnapshotEntity,
    onStatusToggle: () -> Unit,
    onQueueClick: () -> Unit
) {
    var showToggleDialog by remember { mutableStateOf(false)}

    val scope = rememberCoroutineScope()

    val nextStatus = when (queue.queueStatus) {
        QueueStatus.DRAFT -> QueueStatus.ACTIVE
        QueueStatus.ACTIVE -> QueueStatus.CLOSED
        QueueStatus.CLOSED -> QueueStatus.ACTIVE
        QueueStatus.EMPTY -> QueueStatus.EMPTY
    }

    val toggleState = rememberSwipeToDismissBoxState (
        confirmValueChange = {value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showToggleDialog = true
                false
            } else {
                false
            }
        }
    )

    if (showToggleDialog) {
        AlertDialog(
            onDismissRequest = {
                showToggleDialog = false
                scope.launch {
                    toggleState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            title = {
                Text(stringResource(R.string.confirm_request))
            },
            text = {
                Text("${stringResource(R.string.change_status_affirmation)} ${stringResource(R.string.from)} ${queue.queueStatus.toReadableText()} ${stringResource(R.string.to)} ${nextStatus.toReadableText()}")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showToggleDialog = false
                        onStatusToggle()
                    }
                ) {
                    Text(stringResource(R.string.change_status))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showToggleDialog = false
                        scope.launch {
                            toggleState.snapTo(SwipeToDismissBoxValue.Settled)
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
        state = toggleState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = when(nextStatus) {
                        QueueStatus.DRAFT -> Icons.Default.Warning
                        QueueStatus.EMPTY -> Icons.Default.Warning
                        QueueStatus.ACTIVE -> Icons.Default.PlayArrow
                        QueueStatus.CLOSED -> Icons.Default.Lock
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    if (queue.queueStatus != QueueStatus.DRAFT) {
                        onQueueClick()
                    }
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.queue),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
               modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = queuePlan?.title ?: queue.queuePlanId.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.status)}: ${queue.queueStatus.toReadableText()},",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${stringResource(R.string.enqueued)}: ${queue.entries.size}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
