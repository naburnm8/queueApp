package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui.input.QueueRuleInputComponent
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesState
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

@Composable
fun QueueRuleCreateScreen(
    modifier: Modifier = Modifier,
    vm: QueueRulesViewmodel,
    onNavigateBack: () -> Unit,
) {
    val state by vm.stateFlow.collectAsState()

    when (state) {
        is QueueRulesState.Loading -> {
            GenericLoadingScreen(modifier)
        }

        is QueueRulesState.Error -> {
            GenericErrorScreen(
                modifier,
                errorMessage = (state as QueueRulesState.Error).errorMessage,
            ) {
                onNavigateBack()
            }
        }

        is QueueRulesState.Main -> {
            QueueRuleInputComponent(
                modifier = modifier,
                state = state as QueueRulesState.Main,
                onNavigateBack = onNavigateBack,
                onConfirm = { rule ->
                    vm.createRule(rule) {
                        Log.d("QueueRuleCreateScreen", "On navigate back called")
                        onNavigateBack()
                    }
                }
            )
        }
    }
}