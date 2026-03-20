package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel

@Composable
fun QueuePlansCreateScreen(
    modifier: Modifier,
    vm: QueuePlansViewmodel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    val state by vm.stateFlow.collectAsState()
}