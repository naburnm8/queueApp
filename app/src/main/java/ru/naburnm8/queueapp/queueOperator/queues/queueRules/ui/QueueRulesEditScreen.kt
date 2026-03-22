package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui

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
fun QueueRulesEditScreen(
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
                editingRule = (state as QueueRulesState.Main).activeRule,
                onNavigateBack = onNavigateBack,
                onConfirm = { rule ->
                    vm.updateRule(rule) {
                        onNavigateBack()
                    }
                }
            )
        }
    }
}