package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui.input

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.body.GroupBonusRuleBody
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
import ru.naburnm8.queueapp.ui.ImportancePickerComponent
import ru.naburnm8.queueapp.ui.checkNotInBoundaries
import ru.naburnm8.queueapp.ui.filterDoubleInput
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID

@Composable
@Preview
private fun GroupBonusRuleInputComponentPreview() {
    QueueAppTheme() {
        GroupBonusRuleInputComponent(
            modifier = Modifier.fillMaxSize(),
            distinctGroups = listOf("Группа 1", "Группа 2", "Группа 3"),
            queuePlanId = UUID.randomUUID(),
            onConfirm = {}
        )
    }
}


@Composable
fun GroupBonusRuleInputComponent(
    modifier: Modifier = Modifier,
    editingRule: QueueRuleEntity? = null,
    distinctGroups: List<String>,
    queuePlanId: UUID,
    onConfirm: (QueueRuleEntity) -> Unit
) {
    val initialBody = remember(editingRule) {
        editingRule?.payload?.let {
            runCatching {
                Json.decodeFromJsonElement<GroupBonusRuleBody>(it)
            }.getOrNull()
        }
    }

    var selectedGroups by remember {
        mutableStateOf(initialBody?.groups ?: emptyList())
    }
    var bonus by remember {
        mutableFloatStateOf(initialBody?.bonus?.toFloat() ?: 0.5f)
    }
    var enabled by remember {
        mutableStateOf(editingRule?.enabled ?: true)
    }

    var groupsError by remember { mutableStateOf(false) }
    var bonusError by remember { mutableStateOf(false) }

    var sliderActive by remember {mutableStateOf(true)}


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (editingRule == null) stringResource(R.string.create_rule) else stringResource(R.string.edit_rule),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.active),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = enabled,
                onCheckedChange = { enabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.groups),
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            items(
                items = distinctGroups,
                key = { it }
            ) { group ->
                val selected = group in selectedGroups

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {
                            selectedGroups =
                                if (selected) selectedGroups - group else selectedGroups + group
                            groupsError = false
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selected,
                        onCheckedChange = {
                            selectedGroups =
                                if (selected) selectedGroups - group else selectedGroups + group
                            groupsError = false
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = group)
                }
            }
        }

        if (groupsError) {
            Text(
                text = stringResource(R.string.choose_at_least_one_group),
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ImportancePickerComponent(
            value = bonus,
            onValueChange = {bonus = it},
            text = stringResource(R.string.bonus)
        )

        Spacer(modifier = Modifier.height(24.dp))

        SlideToConfirm(
            text = stringResource(R.string.slide_to_confirm),
            enabled = sliderActive,
        ) {
            val parsedBonus = bonus.toDouble()

            groupsError = selectedGroups.isEmpty()

            if (!groupsError) {
                val body = GroupBonusRuleBody(
                    groups = selectedGroups,
                    bonus = parsedBonus
                )
                sliderActive = false
                onConfirm(
                    QueueRuleEntity(
                        id = editingRule?.id,
                        type = RuleType.GROUP_BONUS,
                        enabled = enabled,
                        payload = Json.encodeToJsonElement(body),
                        queuePlanId = queuePlanId
                    )
                )
            }
        }
    }

}
