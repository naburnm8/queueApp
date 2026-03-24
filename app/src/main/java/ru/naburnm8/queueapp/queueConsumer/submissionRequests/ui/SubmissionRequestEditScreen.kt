package ru.naburnm8.queueapp.queueConsumer.submissionRequests.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel.SubmissionRequestsState
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.viewmodel.SubmissionRequestsViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen

@Composable
fun SubmissionRequestEditScreen (
    modifier: Modifier = Modifier,
    vm: SubmissionRequestsViewmodel,
    onNavigateBack: () -> Unit,
) {

    val state by vm.stateFlow.collectAsState()

    when (state) {
        is SubmissionRequestsState.Loading -> {
            GenericLoadingScreen(modifier)
        }
        is SubmissionRequestsState.Error -> {
            GenericErrorScreen(
                modifier = modifier,
                errorMessage = (state as SubmissionRequestsState.Error).errorMessage,
                onExitClick = onNavigateBack
            )
        }

        is SubmissionRequestsState.Main -> {
            if (
                (state as SubmissionRequestsState.Main).inputBundle == null ||
                (state as SubmissionRequestsState.Main).activeRequest == null
            ) {
                return
            }
            SubmissionRequestsInputComponent(
                modifier = modifier,
                editing = (state as SubmissionRequestsState.Main).activeRequest,
                workTypes = (state as SubmissionRequestsState.Main).inputBundle?.workTypes!!,
                queuePlanShort = (state as SubmissionRequestsState.Main).inputBundle?.queuePlan!!,
                me = (state as SubmissionRequestsState.Main).inputBundle?.me!!,
                onSubmit = { req, _ ->
                    vm.updateMySubmissionRequest(req) {
                        onNavigateBack()
                    }
                }
            )
        }
    }

}
