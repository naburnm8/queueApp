package ru.naburnm8.queueapp.queueConsumer.submissionRequests.ui

import android.util.Log
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel.SubmissionRequestsState
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel.SubmissionRequestsViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericDeleteDialog
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme

@Composable
fun SubmissionRequestsListScreen(
    modifier: Modifier = Modifier,
    vm: SubmissionRequestsViewmodel,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: () -> Unit,
) {
    val state by vm.stateFlow.collectAsState()

    when (state) {
        is SubmissionRequestsState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is SubmissionRequestsState.Error -> {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = (state as SubmissionRequestsState.Error).errorMessage,
            ) {
                vm.loadMyRequests()
            }
        }
        is SubmissionRequestsState.Main -> {
            SubmissionRequestsListComponent(
                modifier = modifier,
                requests = (state as SubmissionRequestsState.Main).requests,
                queuePlans = (state as SubmissionRequestsState.Main).queuePlans,
                onRefresh = { vm.loadMyRequests() },
                onEdit = { request ->
                    vm.switchActiveRequest(request) {
                        onNavigateToEdit()
                    }
                },
                onCreate = { plan ->
                    vm.switchInputContext(plan) {
                        onNavigateToCreate()
                    }
                },
                onDeleteRequest = { request ->
                    vm.deleteRequest(request)
                }
            )
        }
    }
}

@Composable
@Preview
private fun SubmissionRequestsListComponentPreview() {
    QueueAppTheme() {
        SubmissionRequestsListComponent(
            modifier = Modifier.fillMaxSize(),
            requests = listOf(SubmissionRequestEntity.mock),
            queuePlans = listOf(QueuePlanShortEntity.mock),
            onRefresh = {},
            onEdit = {},
            onCreate = {},
            onDeleteRequest = {}
        )
    }
}

@Composable
@Preview
private fun SubmissionRequestsListComponentPreviewDark() {
    QueueAppTheme(darkTheme = true) {
        SubmissionRequestsListComponent(
            modifier = Modifier.fillMaxSize(),
            requests = listOf(SubmissionRequestEntity.mock),
            queuePlans = listOf(QueuePlanShortEntity.mock),
            onRefresh = {},
            onEdit = {},
            onCreate = {},
            onDeleteRequest = {}
        )
    }
}

@Composable
private fun ChooseQueuePlanDialog(
    plans: List<QueuePlanShortEntity>,
    onDismiss: () -> Unit,
    onConfirm: (QueuePlanShortEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.choose_plan))
        },
        text = {
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                plans.forEach { plan ->
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { onConfirm(plan) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.queue),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        Column() {
                            Text(
                                text = plan.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = plan.discipline.name,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
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
private fun SubmissionRequestsListComponent(
    modifier: Modifier = Modifier,
    requests: List<SubmissionRequestEntity>,
    queuePlans: List<QueuePlanShortEntity>,
    onRefresh: () -> Unit,
    onCreate: (QueuePlanShortEntity) -> Unit,
    onEdit: (SubmissionRequestEntity) -> Unit,
    onDeleteRequest: (SubmissionRequestEntity) -> Unit
) {
    var showCreateDialog by remember {mutableStateOf(false)}

    if (showCreateDialog) {
        ChooseQueuePlanDialog(
            plans = queuePlans,
            onDismiss = { showCreateDialog = false },
            onConfirm = {
                showCreateDialog = false
                onCreate(it)
            }
        )
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.my_submission_requests),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onRefresh
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh)
                )
            }

            Button(
                onClick = { showCreateDialog = true },
                enabled = queuePlans.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (requests.isEmpty()) {
            Text(
                text = stringResource(R.string.no_submission_requests),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
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
                    items = requests,
                    key = { it.id ?: "${it.queuePlanId}_${it.createdAt}" }
                ) {request ->
                    val foundPlan = queuePlans.find { it.id == request.queuePlanId }
                    Log.d("SubmissionRequestsList", "Queue plan ids: ${queuePlans.map { it.id }}, request plan id: ${request.queuePlanId}")
                    if (foundPlan != null) {
                        SubmissionRequestItem(
                            plan = foundPlan,
                            request = request,
                            onDeleteRequest = { onDeleteRequest(request) },
                            onEditRequest = { onEdit(request) }
                        )
                    }
                }

            }
        }

    }
}

@Composable
private fun SubmissionRequestItem (
    modifier: Modifier = Modifier,
    plan: QueuePlanShortEntity,
    request: SubmissionRequestEntity,
    onDeleteRequest: () -> Unit,
    onEditRequest: () -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismissState = rememberSwipeToDismissBoxState (
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
            onDismiss = {
                showDeleteDialog = false
                scope.launch {
                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            onConfirm = {
                showDeleteDialog = false
                onDeleteRequest()
            }
        )
    }

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        state = dismissState,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
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
                .clickable {onEditRequest()}
                .padding(12.dp),
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
                        text = plan.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = plan.discipline.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${stringResource(R.string.status)}: ${request.status.toReadableText()}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1.0f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@Composable
private fun SubmissionStatus.toReadableText() : String {
    return when(this) {
        SubmissionStatus.PENDING -> stringResource(R.string.status_pending).toLowerCase(Locale.current)
        SubmissionStatus.ENQUEUED -> stringResource(R.string.status_enqueued).toLowerCase(Locale.current)
        SubmissionStatus.REJECTED -> stringResource(R.string.status_rejected).toLowerCase(Locale.current)
        SubmissionStatus.DEQUEUED -> stringResource(R.string.status_dequeued).toLowerCase(Locale.current)
    }
}