package ru.naburnm8.queueapp.queueConsumer.submissionRequests.ui

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.entity.QueuePlanShortEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.entity.SubmissionRequestItemEntity
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.response.SubmissionStatus
import ru.naburnm8.queueapp.queueOperator.discipline.entity.WorkTypeEntity
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.time.Instant
import java.util.UUID

@Composable
@Preview
fun SubmissionRequestsInputComponentPreview() {
    QueueAppTheme() {
        SubmissionRequestsInputComponent(
            modifier = Modifier.fillMaxSize(),
            editing = null,
            workTypes = listOf(WorkTypeEntity.mock),
            queuePlanShort = QueuePlanShortEntity.mock,
            me = ProfileEntity.studentMock,
        ) { _, _ ->

        }
    }
}


@Composable
fun SubmissionRequestsInputComponent(
    modifier: Modifier = Modifier,
    editing: SubmissionRequestEntity? = null,
    workTypes: List<WorkTypeEntity>,
    queuePlanShort: QueuePlanShortEntity,
    me: ProfileEntity,
    onSubmit: (SubmissionRequestEntity, String) -> Unit
) {
    var sliderActive by remember { mutableStateOf(true) }
    var sliderResetKey by remember { mutableIntStateOf(0) }

    val initialItems = remember(editing) {
        editing?.items?.associateBy { it.workTypeId } ?: emptyMap()
    }

    val selectedMap = remember {
        mutableStateMapOf<UUID, Boolean>().apply {
            workTypes.forEach { workType ->
                this[workType.id!!] = initialItems.containsKey(workType.id)
            }
        }
    }

    val quantityMap = remember {
        mutableStateMapOf<UUID, String>().apply {
            workTypes.forEach { workType ->
                val initialQuantity = initialItems[workType.id]?.quantity?.toString() ?: ""
                this[workType.id!!] = initialQuantity
            }
        }
    }

    var itemsError by remember { mutableStateOf(false) }

    val selectedItemsPreview = workTypes.mapNotNull { workType ->
        val id = workType.id ?: return@mapNotNull null
        val selected = selectedMap[id] == true
        val quantity = quantityMap[id]?.toIntOrNull()

        if (selected && quantity != null && quantity > 0) {
            SubmissionRequestItemEntity(
                workTypeId = id,
                workTypeName = workType.name,
                minutesPerOne = workType.estimatedTimeMinutes,
                quantity = quantity,
                minutesOverride = initialItems[id]?.minutesOverride
            )
        } else {
            null
        }
    }

    val totalMinutes = selectedItemsPreview.sumOf { item ->
        (item.minutesOverride ?: item.minutesPerOne) * item.quantity
    }

    var shortCode by remember { mutableStateOf("") }

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
            text = if (editing == null) stringResource(R.string.create_request) else stringResource(R.string.edit_request),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = queuePlanShort.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = queuePlanShort.discipline.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.work_types),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = workTypes,
                key = { it.id ?: "${it.name}_${it.estimatedTimeMinutes}" }
            ) { workType ->
                val workTypeId = workType.id ?: return@items
                val selected = selectedMap[workTypeId] == true
                val quantity = quantityMap[workTypeId] ?: ""

                SubmissionWorkTypeItem(
                    workType = workType,
                    selected = selected,
                    quantity = quantity,
                    onSelectedChange = { checked ->
                        selectedMap[workTypeId] = checked
                        if (!checked) {
                            quantityMap[workTypeId] = ""
                        }
                        itemsError = false
                    },
                    onQuantityChange = {
                        quantityMap[workTypeId] = it.filter(Char::isDigit)
                        itemsError = false
                    }
                )
            }
        }

        if (itemsError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.at_least_one_target),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "${stringResource(R.string.total_minutes)}: $totalMinutes",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${stringResource(R.string.chosen_positions)}: ${selectedItemsPreview.size}",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (editing == null) {
            OutlinedTextField(
                value = shortCode,
                onValueChange = {shortCode = it},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.short_code)) },
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SlideToConfirm(
            text = stringResource(R.string.slide_to_confirm),
            enabled = sliderActive,
            resetKey = sliderResetKey
        ) {
            val items = workTypes.mapNotNull { workType ->
                val workTypeId = workType.id ?: return@mapNotNull null
                val selected = selectedMap[workTypeId] == true
                val quantity = quantityMap[workTypeId]?.toIntOrNull()

                if (selected && quantity != null && quantity > 0) {
                    SubmissionRequestItemEntity(
                        workTypeId = workTypeId,
                        workTypeName = workType.name,
                        minutesPerOne = workType.estimatedTimeMinutes,
                        quantity = quantity,
                        minutesOverride = initialItems[workTypeId]?.minutesOverride
                    )
                } else {
                    null
                }
            }

            itemsError = items.isEmpty()

            if (!itemsError) {
                val calculatedTotalMinutes = items.sumOf { item ->
                    (item.minutesOverride ?: item.minutesPerOne) * item.quantity
                }
                sliderActive = false
                Log.d("SubmissionRequestInput", "Submitting with totalMinutes: $calculatedTotalMinutes, items: $items")
                onSubmit(
                    SubmissionRequestEntity(
                        id = editing?.id,
                        queuePlanId = editing?.queuePlanId ?: queuePlanShort.id,
                        studentId = editing?.studentId ?: me.id,
                        status = editing?.status ?: SubmissionStatus.PENDING,
                        createdAt = editing?.createdAt ?: Instant.now(),
                        updatedAt = Instant.now(),
                        totalMinutes = calculatedTotalMinutes,
                        items = items
                    ),
                    shortCode
                )
            } else {
                sliderResetKey++
            }
        }
    }
}

@Composable
private fun SubmissionWorkTypeItem(
    modifier: Modifier = Modifier,
    workType: WorkTypeEntity,
    selected: Boolean,
    quantity: String,
    onSelectedChange: (Boolean) -> Unit,
    onQuantityChange: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectedChange(!selected) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = onSelectedChange
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = workType.name,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${workType.estimatedTimeMinutes} ${stringResource(R.string.minutes_per_one)}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (selected) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = onQuantityChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.quantity)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}