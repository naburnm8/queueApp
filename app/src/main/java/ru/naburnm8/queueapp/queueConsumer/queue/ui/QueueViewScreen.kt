package ru.naburnm8.queueapp.queueConsumer.queue.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.queue.viewmodel.QueueState
import ru.naburnm8.queueapp.queueConsumer.queue.viewmodel.QueueViewmodel
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueEntryViewEntity
import ru.naburnm8.queueapp.queueOperator.queues.entity.QueueSnapshotEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.toReadableText
import ru.naburnm8.queueapp.ui.screen.GenericDeleteDialog
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import ru.naburnm8.queueapp.ui.toImportanceReadableText
import java.util.UUID
import kotlin.math.max

@Composable
fun QueueViewScreen(
    modifier: Modifier = Modifier,
    vm: QueueViewmodel,
    queueId: UUID?,
    onNavigateBack: () -> Unit,
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
            val queue = (state as QueueState.Main).queues.find { it.queuePlanId == queueId }
            val queuePlan = (state as QueueState.Main).queuePlans.find { it.id == queueId }
            val request = (state as QueueState.Main).myRequestByQueue[queue]
            if (queue == null || queuePlan == null || request == null) {
                GenericErrorScreen(
                    modifier = modifier,
                    errorMessage = stringResource(R.string.queue_is_empty)
                ) {
                    onNavigateBack()
                }
                return
            }

            QueueViewComponent(
                modifier = modifier,
                queue = queue,
                queuePlan = queuePlan,
                request = request,
                queueRules = (state as QueueState.Main).currentQueueRules,
                onLeaveClick = {
                    vm.leaveQueue(queueId) {
                        onNavigateBack()
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun QueueViewComponentPreview(){
    QueueAppTheme() {
        QueueViewComponent(
            modifier = Modifier.fillMaxSize(),
            queue = QueueSnapshotEntity.mockActive,
            queuePlan = QueuePlanShortEntity.mock,
            request = SubmissionRequestEntity.mock,
            queueRules = QueueRuleEntity.mocks,
            onLeaveClick = {}
        )
    }
}

@Composable
fun QueueViewComponent(
    modifier: Modifier = Modifier,
    queue: QueueSnapshotEntity,
    queuePlan: QueuePlanShortEntity,
    request: SubmissionRequestEntity,
    queueRules: List<QueueRuleEntity>?,
    onLeaveClick: () -> Unit,
) {

    var showLeaveDialog by remember { mutableStateOf(false) }

    if (showLeaveDialog) {
        GenericDeleteDialog(
            onDismiss = { showLeaveDialog = false},
            onConfirm = {
                showLeaveDialog = false
                onLeaveClick()
            },
            text = stringResource(R.string.leave_queue_affirmation),
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
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
            onClick = {showLeaveDialog = true},
            enabled = queue.entries.isNotEmpty()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = stringResource(R.string.leave_queue)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${stringResource(R.string.current_queue_head)}:",
            color = MaterialTheme.colorScheme.onBackground,
        )

        val isMeOnHead = queue.current != null && queue.current.studentId == request.studentId

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
                    isMe = isMeOnHead
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
                        isMe = it.studentId == request.studentId
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        QueueInformationComponent(
            queuePlan = queuePlan,
            rules = queueRules
        )
    }
}
@Composable
fun QueueInformationComponent(
    modifier: Modifier = Modifier,
    queuePlan: QueuePlanShortEntity,
    rules: List<QueueRuleEntity>?,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text (
            text = stringResource(R.string.queue_info),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                painterResource(R.drawable.teacher),
                null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.width(8.dp))
            val teacher = queuePlan.teacher
            Text (
                text = "${teacher.lastName} ${teacher.firstName} ${teacher.patronymic ?: ""}",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (queuePlan.useTime) {
            ParameterItem(
                iconResourceId = R.drawable.timer,
                parameterName = stringResource(R.string.slot_time).toLowerCase(Locale.current),
                parameterWeight = queuePlan.wTime
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (queuePlan.useDebts) {
            ParameterItem(
                iconResourceId = R.drawable.submission_request,
                parameterName = stringResource(R.string.debts).toLowerCase(Locale.current),
                parameterWeight = queuePlan.wDebts
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (queuePlan.useAchievements) {
            ParameterItem(
                iconResourceId = R.drawable.star,
                parameterName = stringResource(R.string.achievements).toLowerCase(Locale.current),
                parameterWeight = queuePlan.wAchievements
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (!rules.isNullOrEmpty()) {
            Text (
                text = "${stringResource(R.string.rules)}:",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            rules.forEach {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Build,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text (
                        text = it.type.toReadableText(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))


            }
        }

    }
}

@Composable
private fun ParameterItem(
    modifier: Modifier = Modifier,
    iconResourceId: Int,
    parameterName: String,
    parameterWeight: Double
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(iconResourceId),
            null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text (
            text = "${stringResource(R.string.uses)} $parameterName, ${stringResource(R.string.importance).toLowerCase(Locale.current)}: ${parameterWeight.toImportanceReadableText().toLowerCase(Locale.current)}",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Composable
fun QueueEntryItem(
    modifier: Modifier = Modifier,
    entry: QueueEntryViewEntity,
    isMe: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
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
                    text = if (!isMe) entry.studentName else stringResource(R.string.you),
                    color = if (!isMe) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error,
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