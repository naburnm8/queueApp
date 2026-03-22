package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui.input

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.viewmodel.QueueRulesState

@Composable
fun QueueRuleInputComponent(
    modifier: Modifier = Modifier,
    state: QueueRulesState.Main,
    editingRule: QueueRuleEntity? = null,
    onNavigateBack: () -> Unit,
    onConfirm: (QueueRuleEntity) -> Unit
) {
    when(state) {
        is QueueRulesState.Main.OtherType -> {
            if (state.queuePlan.id == null) {
                return
            }

            TimestampBonusRuleInputComponent(
                modifier = modifier,
                editingRule = editingRule,
                queuePlanId = state.queuePlan.id,
                onConfirm = onConfirm
            )
        }

        is QueueRulesState.Main.GroupBonus -> {
            if (state.queuePlan.id == null) {
                onNavigateBack()
                return
            }

            GroupBonusRuleInputComponent(
                modifier = modifier,
                editingRule = editingRule,
                distinctGroups = state.distinctGroups,
                queuePlanId = state.queuePlan.id,
                onConfirm = onConfirm
            )
        }

        is QueueRulesState.Main.IdentifierBonus -> {
            if (state.queuePlan.id == null) {
                onNavigateBack()
                return
            }

            IdentifierBonusRuleInputComponent(
                modifier = modifier,
                editingRule = editingRule,
                queuePlanId = state.queuePlan.id,
                onConfirm = onConfirm,
                studentsByGroup = state.studentsToGroups,
            )
        }

        else -> {

        }
    }
}