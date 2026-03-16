package ru.naburnm8.queueapp.queueOperator.discipline.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.queueOperator.discipline.entity.DisciplineEntity
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineState
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

@Composable
fun ActiveDisciplineEditScreen(
    modifier: Modifier = Modifier,
    vm: DisciplineViewmodel,
    onBackNavigate: () -> Unit,
) {
    val state by vm.stateFlow.collectAsState()

    when (state) {
        is DisciplineState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is DisciplineState.Error -> {
            GenericErrorScreen(
                modifier,
                (state as DisciplineState.Error).message
            ) {
                onBackNavigate()
            }
        }
        is DisciplineState.Main -> {
            if ((state as DisciplineState.Main).activeDiscipline == null) {
                GenericErrorScreen(
                    modifier,
                    stringResource(R.string.error_occurred)
                ) {
                    onBackNavigate()
                }
                return
            }
            val activeDiscipline = (state as DisciplineState.Main).activeDiscipline!!

            var name by remember { mutableStateOf(activeDiscipline.discipline.name) }
            var personalAchievementsScoreLimit by remember { mutableStateOf(activeDiscipline.discipline.personalAchievementsScoreLimit.toString()) }

            var nameError by remember { mutableStateOf(false) }
            var personalAchievementsScoreLimitError by remember { mutableStateOf(false) }

            var sliderActive by remember { mutableStateOf(true) }

            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.edit_discipline),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = ""
                        )
                    },
                    isError = nameError,
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = personalAchievementsScoreLimit,
                    onValueChange = { personalAchievementsScoreLimit = it },
                    label = { Text(stringResource(R.string.personal_score_limit)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = ""
                        )
                    },
                    isError = personalAchievementsScoreLimitError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SlideToConfirm(
                    text = stringResource(R.string.slide_to_confirm),
                    enabled = sliderActive
                ) {
                    sliderActive = false
                    nameError = false
                    personalAchievementsScoreLimitError = false

                    if (name == "") nameError = true
                    if (personalAchievementsScoreLimit == "") personalAchievementsScoreLimitError =
                        true

                    if (!nameError && !personalAchievementsScoreLimitError) {
                        vm.updateDiscipline(
                            DisciplineEntity(
                                id = activeDiscipline.discipline.id,
                                name = name,
                                personalAchievementsScoreLimit = personalAchievementsScoreLimit.toIntOrNull() ?: 0
                            )
                        ) {
                            onBackNavigate()
                        }
                    }
                }
            }
        }
    }
}