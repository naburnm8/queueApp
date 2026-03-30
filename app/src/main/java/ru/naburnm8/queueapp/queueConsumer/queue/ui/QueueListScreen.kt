package ru.naburnm8.queueapp.queueConsumer.queue.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.queue.viewmodel.QueueState
import ru.naburnm8.queueapp.queueConsumer.queue.viewmodel.QueueViewmodel
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.toReadableText
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.toReadableText
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import java.util.UUID

@Composable
fun QueueListScreen(
    modifier: Modifier = Modifier,
    vm: QueueViewmodel,
    onNavigateToQueue: (UUID) -> Unit,
) {
    val state by vm.stateFlow.collectAsState()

    when (state) {
        is QueueState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is QueueState.Error -> {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = (state as QueueState.Error).errorMessage
            ) {
                vm.loadQueues()
            }

        }

        is QueueState.Main -> {
            QueueListComponent(
                modifier = modifier,
                queues = (state as QueueState.Main).queues,
                queuePlans = (state as QueueState.Main).queuePlans,
                requests = (state as QueueState.Main).myRequestByQueue,
                onQueueClick = { onNavigateToQueue(it) },
                onRefresh = { vm.loadQueues() }
            )
        }
    }
}

@Composable
fun QueueListComponent(
    modifier: Modifier = Modifier,
    queues: List<QueueSnapshotEntity>,
    queuePlans: List<QueuePlanShortEntity>,
    requests: Map<QueueSnapshotEntity, SubmissionRequestEntity>,
    onQueueClick: (UUID) -> Unit,
    onRefresh: () -> Unit,
) {
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

        Button(
            onClick = onRefresh
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.refresh),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (queues.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.you_have_no_queues),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items (
                    items = queues,
                    key = { it.queuePlanId }
                ) {queue ->
                    val queuePlan = queuePlans.find {it.id == queue.queuePlanId}
                    val request = requests[queue]
                    QueueListItem(
                        queuePlan = queuePlan,
                        queue = queue,
                        request = request,
                        onQueueClick = { onQueueClick(queue.queuePlanId) }
                    )
                }
            }

        }
    }

}

@Composable
fun QueueListItem(
    modifier: Modifier = Modifier,
    queuePlan: QueuePlanShortEntity?,
    queue: QueueSnapshotEntity,
    request: SubmissionRequestEntity?,
    onQueueClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                if (request != null) {
                    if (request.status != SubmissionStatus.REJECTED && request.status != SubmissionStatus.PENDING) {
                        onQueueClick()
                    }
                }
            }

            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if ((queue.current != null) && (request != null) && (queue.current.requestId == request.id)) {
                    Text(
                        text = stringResource(R.string.you_are_on_queue_head),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    var currentPlace = queue.entries.find {it.requestId == request?.id}?.place ?: "-"

                    Text(
                        text = "${stringResource(R.string.status)}: ${request?.status?.toReadableText()}, ",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (currentPlace == 0) {
                        currentPlace = stringResource(R.string.next).toLowerCase(Locale.current)
                    }

                    Text(
                        text = "${stringResource(R.string.place).toLowerCase(Locale.current)}: $currentPlace",
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