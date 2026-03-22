package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesState
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

@Composable
fun QueueRuleListScreen(
    modifier: Modifier,
    plansVm: QueuePlansViewmodel,
    rulesVm: QueueRulesViewmodel,
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: () -> Unit
) {

    val rulesState by rulesVm.stateFlow.collectAsState()

    when (rulesState) {
        is QueueRulesState.Loading -> {
            GenericLoadingScreen(modifier)
        }

        is QueueRulesState.Error -> {
            GenericErrorScreen(
                modifier,
                errorMessage = (rulesState as QueueRulesState.Error).errorMessage
            ) {
                onNavigateBack()
            }
        }

        is QueueRulesState.Main -> {

        }
    }
}

@Composable
fun QueueRuleListComponent(
    modifier: Modifier = Modifier,
    rules: List<QueueRuleEntity>,
    onDelete: (QueueRuleEntity) -> Unit,
    onEdit: (QueueRuleEntity) -> Unit,
    onCreate: (RuleType) -> Unit
) {

}