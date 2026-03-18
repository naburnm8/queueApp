package ru.naburnm8.queueapp.queueOperator.metrics.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.naburnm8.queueapp.queueOperator.metrics.viewmodel.StudentMetricsState
import ru.naburnm8.queueapp.queueOperator.metrics.viewmodel.StudentMetricsViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen


@Composable
fun StudentMetricsCreateScreen(
    modifier: Modifier = Modifier,
    vm: StudentMetricsViewmodel,
    onNavigateBack: () -> Unit,

) {

    val state by vm.stateFlow.collectAsState()

    when(state) {
        is StudentMetricsState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is StudentMetricsState.Error -> {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = (state as StudentMetricsState.Error).message
            ) {
                onNavigateBack()
            }
        }
        is StudentMetricsState.Main -> {
            val bundle = (state as StudentMetricsState.Main).creatingFor ?: return

            StudentMetricsInputComponent(
                modifier = modifier,
                teacher = bundle.teacher,
                discipline = bundle.discipline,
                studentsByGroup = bundle.studentsByGroup,
                onSubmit = {
                    vm.createMetric(it) {
                        onNavigateBack()
                    }
                }
            )
        }
    }

}