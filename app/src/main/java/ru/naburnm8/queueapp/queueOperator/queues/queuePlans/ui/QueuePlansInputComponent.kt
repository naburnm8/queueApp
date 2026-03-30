package ru.naburnm8.queueapp.queueOperator.queues.queuePlans.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.entity.QueuePlanEntity
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.item.QueuePlanItem
import ru.naburnm8.queueapp.ui.ImportancePickerComponent
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme

@Composable
fun QueuePlansInputComponent(
    modifier: Modifier,
    item: QueuePlanEntity? = null,
    onConfirm: (QueuePlanItem) -> Unit,
    toolbarButtons: @Composable ColumnScope.() -> Unit = {},
) {
    var title by remember { mutableStateOf(item?.title ?: "") }
    var slotDurationMinutes by remember { mutableStateOf(item?.slotDurationMinutes?.toString() ?: "") }

    var useDebts by remember { mutableStateOf(item?.useDebts ?: false) }
    var wDebts by remember { mutableFloatStateOf(item?.wDebts?.toFloat() ?: 0.5f) }

    var useTime by remember { mutableStateOf(item?.useTime ?: false) }
    var wTime by remember { mutableFloatStateOf(item?.wTime?.toFloat() ?: 0.5f) }

    var useAchievements by remember { mutableStateOf(item?.useAchievements ?: false) }
    var wAchievements by remember { mutableFloatStateOf(item?.wAchievements?.toFloat() ?: 0.5f ) }

    var titleError by remember { mutableStateOf(false) }
    var slotDurationError by remember { mutableStateOf(false) }
    var debtsWeightError by remember { mutableStateOf(false) }
    var timeWeightError by remember { mutableStateOf(false) }
    var achievementsWeightError by remember { mutableStateOf(false) }

    var sliderActive by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (item == null) stringResource(R.string.create_plan) else stringResource(R.string.edit_plan),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        toolbarButtons()

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                titleError = false
            },
            label = { Text(stringResource(R.string.plan_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            },
            isError = titleError
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = slotDurationMinutes,
            onValueChange = {
                slotDurationMinutes = it.filter(Char::isDigit)
                slotDurationError = false
            },
            label = { Text(stringResource(R.string.slot_duration_in_minutes)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = slotDurationError
        )

        Spacer(modifier = Modifier.height(16.dp))

        QueuePlanCriterionInput(
            title = stringResource(R.string.use_debts),
            enabled = useDebts,
            weight = wDebts,
            onEnabledChange = {
                useDebts = it
            },
            onWeightChange = {
                wDebts = it
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        QueuePlanCriterionInput(
            title = stringResource(R.string.use_slot_time),
            enabled = useTime,
            weight = wTime,
            onEnabledChange = {
                useTime = it
                timeWeightError = false
            },
            onWeightChange = {
                wTime = it
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        QueuePlanCriterionInput(
            title = stringResource(R.string.use_personal_achievements),
            enabled = useAchievements,
            weight = wAchievements,
            onEnabledChange = {
                useAchievements = it
                achievementsWeightError = false
            },
            onWeightChange = {
                wAchievements = it
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SlideToConfirm(
            text = stringResource(R.string.slide_to_confirm),
            enabled = sliderActive
        ) {
            titleError = title.isBlank()
            slotDurationError = slotDurationMinutes.isBlank()

            if (!titleError && !slotDurationError && !debtsWeightError && !timeWeightError && !achievementsWeightError) {
                val newObject = QueuePlanItem(
                    id = item?.id,
                    title = title,
                    useDebts = useDebts,
                    wDebts = wDebts.toDouble(),
                    useTime = useTime,
                    wTime = wTime.toDouble(),
                    useAchievements = useAchievements,
                    wAchievements = wAchievements.toDouble(),
                    slotDurationMinutes = slotDurationMinutes.toIntOrNull() ?: 0
                )
                onConfirm(newObject)
            } else {
                Log.e("QueuePlansInputComponent", "Validation failed: titleError=$titleError, slotDurationError=$slotDurationError, debtsWeightError=$debtsWeightError, timeWeightError=$timeWeightError, achievementsWeightError=$achievementsWeightError")
            }
        }

    }
}

@Composable
@Preview
fun QueuePlansInputComponentPreview() {
    QueueAppTheme() {
        QueuePlansInputComponent(
            modifier = Modifier.fillMaxWidth(),
            item = null,
            onConfirm = {},
        )
    }
}

@Composable
private fun QueuePlanCriterionInput(
    title: String,
    enabled: Boolean,
    weight: Float,
    onEnabledChange: (Boolean) -> Unit,
    onWeightChange: (Float) -> Unit,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange
                )
            }

            if (enabled) {
                Spacer(modifier = Modifier.height(12.dp))

                ImportancePickerComponent(
                    value = weight,
                    onValueChange = onWeightChange,
                )
            }
        }
    }
}

private fun checkNotInBoundaries(value: String): Boolean {
    val doubleValue = value.toDoubleOrNull() ?: return true
    return doubleValue !in 0.0..1.0
}

private fun filterDoubleInput(input: String): String {
    val filtered = input.filter { it.isDigit() || it == '.' || it == ',' }
    val normalized = filtered.replace(',', '.')
    val firstDotIndex = normalized.indexOf('.')

    return if (firstDotIndex == -1) {
        normalized
    } else {
        buildString {
            normalized.forEachIndexed { index, c ->
                if (c != '.' || index == firstDotIndex) {
                    append(c)
                }
            }
        }
    }
}