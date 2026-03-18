package ru.naburnm8.queueapp.queueOperator.metrics.ui


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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.metrics.entity.StudentMetricEntity


@Composable
fun StudentMetricsInputComponent(
    modifier: Modifier = Modifier,
    teacher: ProfileEntity,
    studentsByGroup: Map<String, List<ProfileEntity>>,
    discipline: DisciplineEntity,
    editing: StudentMetricEntity? = null,
    onSubmit: (StudentMetricEntity) -> Unit
) {

    var debtsCount by remember { mutableStateOf(
        editing
        ?.debtsCount
        ?.toString() ?: "") }
    var personalAchievementsScore by remember {
        mutableStateOf(
        editing
        ?.personalAchievementsScore
        ?.toString() ?: "")
    }

    var debtsError by remember { mutableStateOf(false) }
    var personalAchievementsScoreError by remember { mutableStateOf(false) }

    var sliderActive by remember { mutableStateOf(true) }

    var selectedStudent by remember { mutableStateOf(editing?.student) }
    var studentError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if(editing == null) stringResource(R.string.create_metric) else stringResource(R.string.edit_metric),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = debtsCount,
            onValueChange = { debtsCount = it },
            label = { Text(stringResource(R.string.debts)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = ""
                )
            },
            isError = debtsError,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = personalAchievementsScore,
            onValueChange = { personalAchievementsScore = it },
            label = { Text(stringResource(R.string.score)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = ""
                )
            },
            isError = personalAchievementsScoreError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        if (editing == null) {
            Spacer(modifier = Modifier.height(16.dp))

            StudentPickerComponent(
                studentsByGroup = studentsByGroup,
                selectedStudent = selectedStudent,
                onStudentSelected = {
                    selectedStudent = it
                    studentError = false
                }
            )

            if (studentError) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.pick_a_student),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SlideToConfirm(
            text = stringResource(R.string.slide_to_confirm),
            enabled = sliderActive
        ) {
            sliderActive = false
            debtsError = false
            personalAchievementsScoreError = false

            if (debtsCount == "") {
                debtsError = true
            }
            if (personalAchievementsScore == "") {
                personalAchievementsScoreError = true
            }

            if (!debtsError && !personalAchievementsScoreError) {
                onSubmit(
                    StudentMetricEntity(
                        id = editing?.id,
                        discipline = discipline,
                        student = selectedStudent ?: throw IllegalStateException("student is null"),
                        teacher = teacher,
                        debtsCount = debtsCount.toInt(),
                        personalAchievementsScore = personalAchievementsScore.toInt()
                    )
                )
            }
        }
    }

}

@Composable
private fun StudentPickerComponent(
    modifier: Modifier = Modifier,
    studentsByGroup: Map<String, List<ProfileEntity>>,
    onStudentSelected: (ProfileEntity) -> Unit,
    selectedStudent: ProfileEntity?
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.student),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                )
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = studentsByGroup.keys.toList(),
                key = { it }
            ) {
                StudentGroupPickerItem(
                    groupName = it,
                    students = studentsByGroup[it] ?: emptyList(),
                    selectedStudent = selectedStudent,
                    onStudentSelected = onStudentSelected
                )
            }
        }
    }
}

@Composable
private fun StudentGroupPickerItem(
    modifier: Modifier = Modifier,
    groupName: String,
    students: List<ProfileEntity>,
    selectedStudent: ProfileEntity?,
    onStudentSelected: (ProfileEntity) -> Unit
) {
    var expanded by rememberSaveable(groupName) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = groupName,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = students.size.toString(),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = students,
                    key = { it.id }
                ) {
                    StudentPickerItem(
                        student = it,
                        selected = selectedStudent?.id == it.id,
                        onClick = {
                            onStudentSelected(it)
                        }
                    )

                }
            }
        }
    }
}


@Composable
private fun StudentPickerItem(
    modifier: Modifier = Modifier,
    student: ProfileEntity,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "${student.lastName} ${student.firstName}",
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}