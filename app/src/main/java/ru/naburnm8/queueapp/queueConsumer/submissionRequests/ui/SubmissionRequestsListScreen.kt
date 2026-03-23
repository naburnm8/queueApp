package ru.naburnm8.queueapp.queueConsumer.submissionRequests.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel.SubmissionRequestsState
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel.SubmissionRequestsViewmodel
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
            queuePlans = listOf(),
            onRefresh = {},
            onEdit = {},
            onCreate = {}
        )
    }
}

@Composable
private fun SubmissionRequestsListComponent(
    modifier: Modifier = Modifier,
    requests: List<SubmissionRequestEntity>,
    queuePlans: List<QueuePlanShortEntity>,
    onRefresh: () -> Unit,
    onCreate: (QueuePlanShortEntity) -> Unit,
    onEdit: (SubmissionRequestEntity) -> Unit,
) {

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

        Button(
            onClick = onRefresh
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.refresh)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

}