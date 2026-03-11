package ru.naburnm8.queueapp.authorization.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.entity.IntegrationEntity
import ru.naburnm8.queueapp.authorization.entity.RegisterStudentEntity
import ru.naburnm8.queueapp.authorization.entity.RegisterTeacherEntity
import ru.naburnm8.queueapp.authorization.viewmodel.RegistrationState
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID

@Composable
fun TeacherRegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: (RegisterTeacherEntity, UUID) -> Unit,
    state: RegistrationState,
    onProceedToLoginClick: () -> Unit
) {
    var selectedItemId by remember {mutableStateOf(UUID(0,0))}

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var telegram by remember { mutableStateOf("") }

    var emailError by remember {mutableStateOf(false)}
    var passwordError by remember {mutableStateOf(false)}
    var firstNameError by remember {mutableStateOf(false)}
    var lastNameError by remember {mutableStateOf(false)}
    var departmentError by remember {mutableStateOf(false)}

    var sliderActive by remember {mutableStateOf(true)}

    if (state is RegistrationState.Loading || state is RegistrationState.Error) sliderActive = false
    if (state is RegistrationState.IntegrationsLoaded) sliderActive = true

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state is RegistrationState.RegistrationSuccess) {
            BackHandler() {
                onProceedToLoginClick()
            }


            Text(
                text = stringResource(R.string.registration_success),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onProceedToLoginClick()
                }
            ) {
                Text(
                    text = stringResource(R.string.proceed_to_login)
                )
            }
        } else {
            Text(
                text = stringResource(R.string.teacher_registration),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            IntegrationsDisplay(
                state = state
            ) {
                selectedItemId = it.id
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedItemId == UUID(0,0)) {

                Text(
                    text = stringResource(R.string.teacher_integration_notice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

            } else {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = ""
                        )
                    },
                    isError = emailError,
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = ""
                        )
                    },
                    isError = passwordError
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(stringResource(R.string.first_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = ""
                        )
                    },
                    isError = firstNameError
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text(stringResource(R.string.last_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = ""
                        )
                    },
                    isError = lastNameError
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = patronymic,
                    onValueChange = { patronymic = it },
                    label = { Text(stringResource(R.string.patronymic)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = ""
                        )
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text(stringResource(R.string.academic_group)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = ""
                        )
                    },
                    isError = departmentError
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = telegram,
                    onValueChange = { telegram = it },
                    label = { Text(stringResource(R.string.telegram)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = ""
                        )
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                SlideToConfirm(
                    text = stringResource(R.string.slide_to_confirm),
                    enabled = sliderActive
                ) {
                    if (email == "") emailError = true
                    if (password == "") passwordError = true
                    if (firstName == "") firstNameError = true
                    if (lastName == "") lastNameError = true
                    if (department == "") departmentError = true

                    if (!emailError && !passwordError && !firstNameError && !lastNameError && !departmentError) {
                        sliderActive = false
                        emailError = false
                        passwordError = false
                        firstNameError = false
                        lastNameError = false
                        departmentError = false
                        onRegisterClick(
                            RegisterTeacherEntity(
                                email = email,
                                password = password,
                                firstName = firstName,
                                lastName = lastName,
                                patronymic = patronymic,
                                department = department,
                                telegram = telegram,
                                null
                            ),
                            selectedItemId
                        )
                    }
                }
            }
        }



    }
}

@Preview
@Composable
private fun TeacherRegistrationScreenPreview() {
    QueueAppTheme() {
        TeacherRegistrationScreen(
            modifier = Modifier.fillMaxSize(),
            onRegisterClick = {_, _ ->},
            state = RegistrationState.IntegrationsLoaded(
                listOf(
                    IntegrationEntity(
                        id = UUID.randomUUID(),
                        name = "Native",
                        payload = ""
                    )
                )
            ),
            onProceedToLoginClick = {}
        )
    }
}