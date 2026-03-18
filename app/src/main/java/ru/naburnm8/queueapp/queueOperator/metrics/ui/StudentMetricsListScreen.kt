package ru.naburnm8.queueapp.queueOperator.metrics.ui

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMultifieldType
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.metrics.entity.StudentMetricEntity
import ru.naburnm8.queueapp.queueOperator.metrics.viewmodel.StudentMetricsState
import ru.naburnm8.queueapp.queueOperator.metrics.viewmodel.StudentMetricsViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID


@Composable
fun StudentMetricsListScreen(
    modifier: Modifier = Modifier,
    vm: StudentMetricsViewmodel,
    onNavigateToMetric: () -> Unit,
    onNavigateToNewMetric: () -> Unit,
) {
    val state by vm.stateFlow.collectAsState()

    when (state) {
        is StudentMetricsState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is StudentMetricsState.Error -> {
            GenericErrorScreen(
                errorMessage = (state as StudentMetricsState.Error).message,
            ) {
                vm.loadMetrics()
            }
        }
        is StudentMetricsState.Main -> {
            StudentMetricsListComponent(
                modifier = modifier,
                metricToDiscipline = (state as StudentMetricsState.Main).metricToDiscipline,
                onMetricClick = {
                    vm.switchActiveMetric(it)
                    onNavigateToMetric()
                },
                onCreateNewMetricClick = {
                    vm.switchCreatingFor(it)
                    onNavigateToNewMetric()
                },
                onDeleteMetric = {
                    vm.deleteMetric(it)
                }
            )
        }
    }
}

@Composable
private fun StudentMetricsListComponent(
    modifier: Modifier = Modifier,
    metricToDiscipline: Map<DisciplineEntity, List<StudentMetricEntity>>,
    onMetricClick: (StudentMetricEntity) -> Unit,
    onCreateNewMetricClick: (DisciplineEntity) -> Unit,
    onDeleteMetric: (StudentMetricEntity) -> Unit,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.student_metrics),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (metricToDiscipline.keys.isEmpty()) {
            Text(
                text = stringResource(R.string.your_disciplines_empty),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            return@Column
        }

        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(
                items = metricToDiscipline.keys.toList(),
                key = {it.id}
            ) {
                DisciplineGroupItem(
                    modifier = Modifier.heightIn(max = 300.dp),
                    discipline = it,
                    metrics = metricToDiscipline[it] ?: emptyList(),
                    onMetricClick = onMetricClick,
                    onDeleteMetricConfirmed = onDeleteMetric,
                    onCreateMetricInDisciplineClick = {
                        onCreateNewMetricClick(it)
                    }
                )
            }
        }
    }
}


@Composable
private fun DisciplineGroupItem(
    modifier: Modifier = Modifier,
    discipline: DisciplineEntity,
    metrics: List<StudentMetricEntity>,
    onMetricClick: (StudentMetricEntity) -> Unit,
    onDeleteMetricConfirmed: (StudentMetricEntity) -> Unit,
    onCreateMetricInDisciplineClick: () -> Unit
) {
    var expanded by rememberSaveable(discipline.id, discipline.name) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = discipline.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = metrics.size.toString(),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {onCreateMetricInDisciplineClick()},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = metrics,
                    key = { it.id!! }
                ) {
                    StudentMetricItem (
                        modifier = Modifier,
                        metric = it,
                        onClick = { onMetricClick(it) },
                        onDeleteConfirmed = { onDeleteMetricConfirmed(it) }
                    )
                }
            }
        }


    }
}

@Composable
private fun StudentMetricItem(
    modifier: Modifier = Modifier,
    metric: StudentMetricEntity,
    onClick: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
                false
            } else {
                false
            }
        }
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                scope.launch {
                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            title = {
                Text(stringResource(R.string.delete_metric))
            },
            text = {
                Text(
                    "${stringResource(R.string.delete_metric_of_student)} ${metric.student.lastName} ${metric.student.firstName}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteConfirmed()
                    }
                ) {
                    Text(stringResource(R.string.delete_metric))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        scope.launch {
                            dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                        }
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${metric.student.lastName} ${metric.student.firstName}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${stringResource(R.string.teacher)} ${metric.teacher.lastName} ${metric.teacher.firstName}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${stringResource(R.string.debts)} ${metric.debtsCount}, ${stringResource(R.string.score).toLowerCase(Locale.current)} ${metric.personalAchievementsScore}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }


}




private val mockDiscipline = DisciplineEntity(
    id = UUID.randomUUID(),
    name = "Технология программирования",
    personalAchievementsScoreLimit = 100
)
private val mockDiscipline2 = DisciplineEntity(
    id = UUID.randomUUID(),
    name = "Инфокоммуникационные технологии в образовании",
    personalAchievementsScoreLimit = 100
)
private val mockTeacher = ProfileEntity(
    id = UUID(0, 0),
    firstName = "Артем",
    lastName = "Линт",
    patronymic = "Дмитриевич",
    multifield = "ИУ3",
    multifieldType = ProfileMultifieldType.DEPARTMENT,
    telegram = "naburnm8",
    avatarUrl = ""
)

private val mockStudent = ProfileEntity(
    id = UUID(0, 0),
    firstName = "Михаил",
    lastName = "Цапков",
    patronymic = "Андреевич",
    multifield = "ИУ3-82Б",
    multifieldType = ProfileMultifieldType.ACADEMIC_GROUP,
    telegram = "gigo",
    avatarUrl = ""
)

private val mockMetric = StudentMetricEntity(
    id = UUID.randomUUID(),
    discipline = mockDiscipline,
    teacher = mockTeacher,
    student = mockStudent,
    debtsCount = 2,
    personalAchievementsScore = 50
)
private val mockMetric2 = StudentMetricEntity(
    id = UUID.randomUUID(),
    discipline = mockDiscipline,
    teacher = mockTeacher,
    student = mockStudent,
    debtsCount = 2,
    personalAchievementsScore = 50
)
private val mockMetric3 = StudentMetricEntity(
    id = UUID.randomUUID(),
    discipline = mockDiscipline,
    teacher = mockTeacher,
    student = mockStudent,
    debtsCount = 2,
    personalAchievementsScore = 50
)

private val mockMap = mapOf(
    mockDiscipline to listOf(mockMetric, mockMetric2, mockMetric3),
    mockDiscipline2 to listOf(mockMetric, mockMetric3, mockMetric2)
)


@Preview
@Composable
private fun StudentMetricsListComponentPreview() {
    QueueAppTheme() {
        StudentMetricsListComponent(
            modifier = Modifier.fillMaxSize(),
            metricToDiscipline = mockMap,
            onMetricClick = {},
            onCreateNewMetricClick = {},
            onDeleteMetric = {}
        )
    }
}