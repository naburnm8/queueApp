package ru.naburnm8.queueapp.queueOperator.discipline.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineState
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.R


@Composable
fun ActiveDisciplineAddOwnerScreen(
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

            var email by remember { mutableStateOf("") }
            var emailError by remember {mutableStateOf(false)}

            var buttonActive by remember { mutableStateOf(true) }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = stringResource(R.string.add_owners),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = ""
                        )
                    },
                    isError = emailError,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        emailError = false
                        if (email == "") {
                            emailError = true
                        }

                        if (!emailError) {
                            buttonActive = false
                            vm.addOwnerByEmail(email) {
                                buttonActive = true
                                onBackNavigate()
                            }
                        }
                    },
                    enabled = buttonActive
                ) {
                    Text(
                        text = stringResource(R.string.submit)
                    )
                }


            }
        }
    }
}