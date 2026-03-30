package ru.naburnm8.queueapp.queueOperator.queues.queueRules.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.body.IdentifierBonusRuleBody
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.body.IdentifierField
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.QueueRuleEntity
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.entity.RuleType
import ru.naburnm8.queueapp.ui.ImportancePickerComponent
import ru.naburnm8.queueapp.ui.checkNotInBoundaries
import ru.naburnm8.queueapp.ui.filterDoubleInput
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentifierBonusRuleInputComponent (
    modifier: Modifier = Modifier,
    editingRule: QueueRuleEntity? = null,
    studentsByGroup: Map<String, List<ProfileEntity>>,
    queuePlanId: UUID,
    onConfirm: (QueueRuleEntity) -> Unit
) {
    val initialBody = remember(editingRule) {
        editingRule?.payload?.let {
            runCatching {
                Json.decodeFromJsonElement<IdentifierBonusRuleBody>(it)
            }.getOrNull()
        }
    }

    var selectedField by remember {
        mutableStateOf(initialBody?.field ?: IdentifierField.TELEGRAM)
    }
    var selectedStudents by remember { mutableStateOf(listOf<ProfileEntity>()) }
    var bonus by remember {
        mutableFloatStateOf(initialBody?.bonus?.toFloat() ?: 0.5f)
    }
    var enabled by remember {
        mutableStateOf(editingRule?.enabled ?: true)
    }

    var fieldExpanded by remember { mutableStateOf(false) }
    var studentsError by remember { mutableStateOf(false) }
    var bonusError by remember { mutableStateOf(false) }

    var sliderActive by remember {
        mutableStateOf(true)
    }

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

        Row(verticalAlignment = Alignment.CenterVertically) {
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

        ExposedDropdownMenuBox(
            expanded = fieldExpanded,
            onExpandedChange = { fieldExpanded = !fieldExpanded }
        ) {
            OutlinedTextField(
                value = selectedField.toReadableText(),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                label = { Text(stringResource(R.string.identifier_field)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = fieldExpanded)
                }
            )

            ExposedDropdownMenu(
                expanded = fieldExpanded,
                onDismissRequest = { fieldExpanded = false }
            ) {
                IdentifierField.entries.forEach { field ->
                    DropdownMenuItem(
                        text = { Text(field.toReadableText()) },
                        onClick = {
                            selectedField = field
                            fieldExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.students),
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            items(
                items = studentsByGroup.entries.toList(),
                key = { it.key }
            ) {(groupName, students) ->
                IdentifierStudentGroupItem(
                    groupName = groupName,
                    students = students,
                    selectedStudents = selectedStudents,
                    onToggleStudent = { student ->
                        selectedStudents =
                            if (selectedStudents.any { it.id == student.id }) {
                                selectedStudents.filterNot { it.id == student.id }
                            } else {
                                selectedStudents + student
                            }
                        studentsError = false
                    }
                )
            }
        }

        if (studentsError) {
            Text(
                text = stringResource(R.string.choose_at_least_one_student),
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
            enabled = sliderActive
        ) {
            val parsedBonus = bonus.toDouble()

            studentsError = selectedStudents.isEmpty()

            if (!studentsError) {
                val values = selectedStudents
                    .mapNotNull { student ->
                        extractIdentifierValue(student, selectedField)
                    }
                    .distinct()

                if (values.isNotEmpty()) {
                    val body = IdentifierBonusRuleBody(
                        field = selectedField,
                        values = values,
                        bonus = parsedBonus
                    )
                    sliderActive = false
                    onConfirm(
                        QueueRuleEntity(
                            id = editingRule?.id,
                            type = RuleType.IDENTIFIER_BONUS,
                            enabled = enabled,
                            payload = Json.encodeToJsonElement(body),
                            queuePlanId = queuePlanId
                        )
                    )
                } else {
                    studentsError = true
                }
            }
        }
    }
}

@Composable
private fun IdentifierStudentGroupItem(
    modifier: Modifier = Modifier,
    groupName: String,
    students: List<ProfileEntity>,
    selectedStudents: List<ProfileEntity>,
    onToggleStudent: (ProfileEntity) -> Unit
) {
    var expanded by rememberSaveable(groupName) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = groupName,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        if (expanded) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                items (
                    items = students,
                    key = { it.id }
                ) { student ->
                    val selected = selectedStudents.any { it.id == student.id }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { onToggleStudent(student) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selected,
                            onCheckedChange = { onToggleStudent(student) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = buildStudentFullName(student)
                        )
                    }
                }
            }
        }
    }
}

private fun extractIdentifierValue(
    student: ProfileEntity,
    field: IdentifierField
): String? {
    return when (field) {
        IdentifierField.TELEGRAM -> student.telegram
        IdentifierField.FULL_NAME -> buildStudentFullName(student)
    }?.takeIf { it.isNotBlank() }
}

private fun buildStudentFullName(student: ProfileEntity): String {
    return listOfNotNull(
        student.lastName,
        student.firstName,
        student.patronymic
    ).joinToString(" ")
}

@Composable
private fun IdentifierField.toReadableText(): String {
    return when (this) {
        IdentifierField.TELEGRAM -> stringResource(R.string.telegram)
        IdentifierField.FULL_NAME -> stringResource(R.string.full_name)
    }
}
