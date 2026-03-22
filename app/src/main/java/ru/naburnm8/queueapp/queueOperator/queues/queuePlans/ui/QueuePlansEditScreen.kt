package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansState
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

@Composable
fun QueuePlansEditScreen(
    modifier: Modifier,
    vm: QueuePlansViewmodel,
    onNavigateBack: () -> Unit,
    onNavigateToInvitations: () -> Unit,
    onNavigateToRules: () -> Unit
) {
    val state by vm.stateFlow.collectAsState()

    when (state) {
        is QueuePlansState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is QueuePlansState.Error -> {
            GenericErrorScreen(modifier, errorMessage = (state as QueuePlansState.Error).errorMessage) {
                onNavigateBack()
            }
        }

        is QueuePlansState.Main -> {
            QueuePlansInputComponent(
                modifier = modifier,
                item = (state as QueuePlansState.Main).activePlan,
                onConfirm = {
                    vm.upsertPlanFromUi(it) {
                        onNavigateBack()
                    }
                },
                toolbarButtons = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                onNavigateToInvitations()
                            }
                        ) {
                            Text(stringResource(R.string.invitations))
                        }
                        Button(
                            onClick = {
                                onNavigateToRules()
                            }
                        ) {
                            Text(stringResource(R.string.rules))
                        }
                    }
                }
            )
        }
    }
}