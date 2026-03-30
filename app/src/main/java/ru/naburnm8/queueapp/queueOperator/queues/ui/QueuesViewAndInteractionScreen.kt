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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
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
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestShortEntity
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueEntryViewEntity
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import ru.naburnm8.queueapp.queueOperator.queues.viewmodel.QueuesState
import ru.naburnm8.queueapp.queueOperator.queues.viewmodel.QueuesViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericDeleteDialog
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID


@Composable
fun QueuesViewAndInteractionScreen(
    modifier: Modifier = Modifier,
    queueId: UUID?,
    vm: QueuesViewmodel,
    onNavigateBack: () -> Unit = {},
) {
    if (queueId == null) {
        GenericErrorScreen(
            modifier = modifier,
            errorMessage = "No queue id provided"
        ) {
            onNavigateBack()
        }

        return
    }

    val state by vm.stateFlow.collectAsState()

    when(state) {
        is QueuesState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is QueuesState.Error -> {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = (state as QueuesState.Error).errorMessage
            ) {
                onNavigateBack()
            }
        }
        is QueuesState.Main -> {
            val queue = (state as QueuesState.Main).queues.find { it.queuePlanId == queueId }
            val queuePlan = (state as QueuesState.Main).queuePlans.find { it.id == queueId}
            val requests = (state as QueuesState.Main).submissionRequestsToQueues[queue]
            if (queue == null || queuePlan == null || requests == null) {
                GenericErrorScreen(
                    modifier = modifier,
                    errorMessage = stringResource(R.string.queue_is_empty)
                ) {
                    onNavigateBack()
                }
                return
            }


            QueuesViewAndInteractionComponent(
                modifier = Modifier.fillMaxSize(),
                queue = queue,
                queuePlan = queuePlan,
                requests = requests,
                onTakeNext = {vm.takeNext(queueId)},
                onApproveRequest = {vm.approveRequest(queueId, it)},
                onRejectRequest = {vm.rejectRequest(queueId, it)},
                onTake = {vm.take(queueId, it)},
                onRemove = {vm.remove(queueId, it)}
            )
        }

    }
}

@Composable
@Preview
fun QueuesViewAndInteractionComponentPreview() {
    QueueAppTheme() {
        QueuesViewAndInteractionComponent(
            modifier = Modifier.fillMaxSize(),
            queue = QueueSnapshotEntity.mockActive,
            queuePlan = QueuePlanEntity.mock,
            requests = listOf(SubmissionRequestShortEntity.mock),
            onTakeNext = {},
            onApproveRequest = {},
            onRejectRequest = {},
            onTake = {},
            onRemove = {}
        )
    }
}


@Composable
fun QueuesViewAndInteractionComponent(
    modifier: Modifier = Modifier,
    queue: QueueSnapshotEntity,
    queuePlan: QueuePlanEntity,
    requests: List<SubmissionRequestShortEntity>,
    onTakeNext: () -> Unit,
    onTake: (UUID) -> Unit,
    onRemove: (UUID) -> Unit,
    onApproveRequest: (UUID) -> Unit,
    onRejectRequest: (UUID) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = queuePlan.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onTakeNext() },
            enabled = queue.entries.isNotEmpty() && queue.queueStatus == QueueStatus.ACTIVE
        ) {
            Text(text = stringResource(R.string.take_next))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${stringResource(R.string.current_queue_head)}:",
            color = MaterialTheme.colorScheme.onBackground,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            if (queue.current != null) {
                QueueEntryItem(
                    entry = queue.current,
                    onRemove = {},
                    onEnqueue = {onTakeNext()},
                    removeDisabled = true,
                    queueStatus = queue.queueStatus
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.no_queue_head),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${stringResource(R.string.queue)}:",
            color = MaterialTheme.colorScheme.onBackground,
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            if (queue.entries.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.queue_is_empty),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(
                    items = queue.entries,
                    key = {it.requestId}
                ) {
                    QueueEntryItem(
                        entry = it,
                        onRemove = {onRemove(it.requestId)},
                        onEnqueue = {onTake(it.requestId)},
                        queueStatus = queue.queueStatus
                    )
                }
            }
        }

        if (requests.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            SubmissionRequestsListComponent(
                requests = requests,
                onApprove = onApproveRequest,
                onReject = onRejectRequest,
                queueStatus = queue.queueStatus
            )
        }
    }
}


@Composable
fun SubmissionRequestsListComponent(
    modifier: Modifier = Modifier,
    requests: List<SubmissionRequestShortEntity>,
    onApprove: (UUID) -> Unit,
    onReject: (UUID) -> Unit,
    queueStatus: QueueStatus
) {

    var expanded by remember {mutableStateOf(false)}

    val interactionsEnabled = queueStatus == QueueStatus.ACTIVE

    Column (
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.pending_requests),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        if (expanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(4.dp)
                    .heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items (
                    items = requests,
                    key = {it.id}
                ) {
                    SubmissionRequestListItem(
                        request = it,
                        onApprove = {onApprove(it.id)},
                        onReject = {onReject(it.id)},
                        interactionsEnabled = interactionsEnabled
                    )
                }
            }
        }
    }


}

@Composable
fun SubmissionRequestListItem(
    modifier: Modifier = Modifier,
    request: SubmissionRequestShortEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    interactionsEnabled: Boolean = true
) {
    var showRejectDialog by remember{mutableStateOf(false)}
    val scope = rememberCoroutineScope ()
    val rejectState = rememberSwipeToDismissBoxState (
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    showRejectDialog = true
                    false
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onApprove()
                    false
                }
                else -> {
                    false
                }
            }
        }
    )

    if (showRejectDialog) {
        GenericDeleteDialog(
            onDismiss = {
                showRejectDialog = false
                scope.launch {
                    rejectState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            onConfirm = {
                showRejectDialog = false
                onReject()
            }
        )
    }

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        state = rejectState,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        },
        enableDismissFromStartToEnd = interactionsEnabled,
        enableDismissFromEndToStart = interactionsEnabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.submission_request),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column() {
                    Text(
                        text = request.studentName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.timer),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text (
                            text = "${request.totalMinutes} ${stringResource(R.string.minutes_short)}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

            }
        }

    }
}

@Composable
fun QueueEntryItem(
    modifier: Modifier = Modifier,
    entry: QueueEntryViewEntity,
    onRemove: () -> Unit,
    onEnqueue: () -> Unit,
    queueStatus: QueueStatus,
    removeDisabled: Boolean = false,
) {
    var showRemoveDialog by remember {mutableStateOf(false)}
    var showEnqueueDialog by remember {mutableStateOf(false)}

    val scope = rememberCoroutineScope()

    val removeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showRemoveDialog = true
                false
            } else {
                false
            }
        }
    )

    val interactionsEnabled = queueStatus == QueueStatus.ACTIVE

    if (showRemoveDialog) {
        GenericDeleteDialog(
            onDismiss = {
                showRemoveDialog = false
                scope.launch {
                    removeState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            onConfirm = {
                showRemoveDialog = false
                onRemove()
            },
            text = stringResource(R.string.remove_from_queue_affirmation)
        )
    }

    if (showEnqueueDialog) {
        GenericDeleteDialog(
            onDismiss = {
                showEnqueueDialog = false
            },
            onConfirm = {
                showEnqueueDialog = false
                onEnqueue()
            },
            text = stringResource(R.string.take_into_queue_affirmation)
        )
    }

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        state = removeState,
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
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = !removeDisabled && interactionsEnabled,
        gesturesEnabled = !removeDisabled && interactionsEnabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    if (!removeDisabled && interactionsEnabled) {
                        showEnqueueDialog = true
                    }
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.8f)
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column () {
                    Text (
                        text = entry.studentName,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.timer),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text (
                            text = "${entry.totalMinutes} ${stringResource(R.string.minutes_short)}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Column (
                modifier = Modifier.weight(0.1f)
            ) {
                if (entry.place != -1) {
                    Text (
                        text = entry.place.toString(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text (
                        text = entry.priority.toString(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}