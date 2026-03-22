package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.body.TimestampBonusRuleBody
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
import ru.naburnm8.queueapp.ui.checkNotInBoundaries
import ru.naburnm8.queueapp.ui.filterDoubleInput
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.UUID

@Composable
@Preview
private fun TimestampBonusRuleInputComponentPreview() {
    QueueAppTheme() {
        TimestampBonusRuleInputComponent(
            modifier = Modifier.fillMaxSize(),
            queuePlanId = UUID.randomUUID(),
            onConfirm = {}
        )
    }
}


@Composable
fun TimestampBonusRuleInputComponent(
    modifier: Modifier = Modifier,
    editingRule: QueueRuleEntity? = null,
    queuePlanId: UUID,
    onConfirm: (QueueRuleEntity) -> Unit
) {
    val initialBody = remember(editingRule) {
        editingRule?.payload?.let {
            runCatching {
                Json.decodeFromJsonElement<TimestampBonusRuleBody>(it)
            }.getOrNull()
        }
    }

    var begin by remember {
        mutableStateOf(initialBody?.begin)
    }
    var end by remember {
        mutableStateOf(initialBody?.end)
    }
    var bonus by remember {
        mutableStateOf(initialBody?.bonus?.toString() ?: "")
    }
    var enabled by remember {
        mutableStateOf(editingRule?.enabled ?: true)
    }

    var beginError by remember { mutableStateOf(false) }
    var endError by remember { mutableStateOf(false) }
    var bonusError by remember { mutableStateOf(false) }

    var showBeginDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var pendingDateSelectionForBegin by remember { mutableStateOf<LocalDate?>(null) }
    var pendingDateSelectionForEnd by remember { mutableStateOf<LocalDate?>(null) }

    var showBeginTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    
    var sliderActive by remember {mutableStateOf(true)}

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (editingRule == null) {
                stringResource(R.string.create_rule)
            } else {
                stringResource(R.string.edit_rule)
            },
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
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

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = begin?.let(::formatOffsetDateTimeForUi) ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(stringResource(R.string.start_interval)) },
                isError = beginError
            )

            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showBeginDatePicker = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = end?.let(::formatOffsetDateTimeForUi) ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(stringResource(R.string.end_interval)) },
                isError = endError
            )

            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showEndDatePicker = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bonus,
            onValueChange = {
                bonus = filterDoubleInput(it)
                bonusError = checkNotInBoundaries(bonus)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.bonus)) },
            singleLine = true,
            isError = bonusError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(24.dp))

        SlideToConfirm(
            text = stringResource(R.string.slide_to_confirm),
            enabled = sliderActive
        ) {
            val parsedBonus = bonus.toDoubleOrNull()

            beginError = begin == null
            endError = end == null || (begin != null && end != null && end!! <= begin!!)
            bonusError = parsedBonus == null || parsedBonus !in 0.0..1.0

            if (!beginError && !endError && !bonusError) {
                val body = TimestampBonusRuleBody(
                    begin = begin!!,
                    end = end!!,
                    bonus = parsedBonus!!
                )

                sliderActive = false

                onConfirm(
                    QueueRuleEntity(
                        id = editingRule?.id,
                        type = RuleType.TIMESTAMP_BONUS,
                        enabled = enabled,
                        payload = Json.encodeToJsonElement(body),
                        queuePlanId = queuePlanId
                    )
                )
            }
        }
    }

    if (showBeginDatePicker) {
        LocalDatePickerDialog(
            initialDate = begin?.toLocalDate(),
            onDismiss = { showBeginDatePicker = false },
            onConfirm = { pickedDate ->
                pendingDateSelectionForBegin = pickedDate
                showBeginDatePicker = false
                showBeginTimePicker = true
            }
        )
    }

    if (showEndDatePicker) {
        LocalDatePickerDialog(
            initialDate = end?.toLocalDate(),
            onDismiss = { showEndDatePicker = false },
            onConfirm = { pickedDate ->
                pendingDateSelectionForEnd = pickedDate
                showEndDatePicker = false
                showEndTimePicker = true
            }
        )
    }

    if (showBeginTimePicker && pendingDateSelectionForBegin != null) {
        LocalTimePickerDialog(
            initialTime = begin?.toLocalTime(),
            onDismiss = {
                showBeginTimePicker = false
                pendingDateSelectionForBegin = null
            },
            onConfirm = { pickedTime ->
                begin = combineToOffsetDateTime(
                    date = pendingDateSelectionForBegin!!,
                    time = pickedTime
                )
                beginError = false
                showBeginTimePicker = false
                pendingDateSelectionForBegin = null
            }
        )
    }

    if (showEndTimePicker && pendingDateSelectionForEnd != null) {
        LocalTimePickerDialog(
            initialTime = end?.toLocalTime(),
            onDismiss = {
                showEndTimePicker = false
                pendingDateSelectionForEnd = null
            },
            onConfirm = { pickedTime ->
                end = combineToOffsetDateTime(
                    date = pendingDateSelectionForEnd!!,
                    time = pickedTime
                )
                endError = false
                showEndTimePicker = false
                pendingDateSelectionForEnd = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalTimePickerDialog(
    initialTime: LocalTime?,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialTime?.hour ?: 12,
        initialMinute = initialTime?.minute ?: 0,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.pick_time))
        },
        text = {
            TimePicker(state = state)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(LocalTime.of(state.hour, state.minute))
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalDatePickerDialog(
    initialDate: LocalDate?,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit
) {
    val initialMillis = initialDate
        ?.atStartOfDay(ZoneId.systemDefault())
        ?.toInstant()
        ?.toEpochMilli()

    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        val localDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onConfirm(localDate)
                    }
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = state)
    }
}

private fun combineToOffsetDateTime(
    date: LocalDate,
    time: LocalTime
): OffsetDateTime {
    return date
        .atTime(time)
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime()
}

private fun formatOffsetDateTimeForUi(value: OffsetDateTime): String {
    val date = value.toLocalDate()
    val time = value.toLocalTime().withSecond(0).withNano(0)
    return "$date $time"
}