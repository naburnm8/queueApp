package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansState
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

@Composable
fun QueuePlansCreateScreen(
    modifier: Modifier,
    vm: QueuePlansViewmodel,
    onNavigateBack: () -> Unit,
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
                item = null,
                onConfirm = {
                    vm.upsertPlanFromUi(it) {
                        onNavigateBack()
                    }
                }
            )
        }
    }
}