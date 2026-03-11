package ru.naburnm8.queueapp.authorization.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonObject
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.entity.IntegrationEntity
import ru.naburnm8.queueapp.authorization.entity.RegisterStudentEntity
import ru.naburnm8.queueapp.authorization.viewmodel.RegistrationState
import ru.naburnm8.queueapp.ui.screen.GenericErrorScreen
import ru.naburnm8.queueapp.ui.screen.GenericLoadingScreen
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID

@Composable
fun StudentRegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: (RegisterStudentEntity, UUID) -> Unit,
    state: RegistrationState,
    onProceedToLoginClick: () -> Unit
) {
    var selectedItemId by remember {mutableStateOf(UUID(0,0))}

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var academicGroup by remember { mutableStateOf("") }
    var telegram by remember { mutableStateOf("") }

    var emailError by remember {mutableStateOf(false)}
    var passwordError by remember {mutableStateOf(false)}
    var firstNameError by remember {mutableStateOf(false)}
    var lastNameError by remember {mutableStateOf(false)}
    var academicGroupError by remember {mutableStateOf(false)}

    var sliderActive by remember {mutableStateOf(true)}

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
                text = stringResource(R.string.register_as_student),
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")},
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
                leadingIcon = {Icon(imageVector = Icons.Default.Lock, contentDescription = "")},
                isError = passwordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(R.string.first_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {Icon(imageVector = Icons.Default.Face, contentDescription = "")},
                isError = firstNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.last_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {Icon(imageVector = Icons.Default.Face, contentDescription = "")},
                isError = lastNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = patronymic,
                onValueChange = { patronymic = it },
                label = { Text(stringResource(R.string.patronymic)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {Icon(imageVector = Icons.Default.Face, contentDescription = "")},
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = academicGroup,
                onValueChange = { academicGroup = it },
                label = { Text(stringResource(R.string.academic_group)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {Icon(imageVector = Icons.Default.Info, contentDescription = "")},
                isError = academicGroupError
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = telegram,
                onValueChange = { telegram = it },
                label = { Text(stringResource(R.string.telegram)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {Icon(imageVector = Icons.Default.Info, contentDescription = "")},
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
                if (academicGroup == "") academicGroupError = true

                if (!emailError && !passwordError && !firstNameError && !lastNameError && !academicGroupError) {
                    sliderActive = false
                    emailError = false
                    passwordError = false
                    firstNameError = false
                    lastNameError = false
                    academicGroupError = false
                    onRegisterClick(
                        RegisterStudentEntity(
                            email = email,
                            password = password,
                            firstName = firstName,
                            lastName = lastName,
                            patronymic = patronymic,
                            academicGroup = academicGroup,
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

@Composable
fun IntegrationsDisplay(
    modifier: Modifier = Modifier,
    state: RegistrationState,
    onElementClick: (IntegrationEntity) -> Unit,
) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .verticalScroll(rememberScrollState())
    ) {
         when (state) {
            is RegistrationState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            is RegistrationState.Error -> {
                Text(
                    text = stringResource(R.string.error_fetching_integrations),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }
            is RegistrationState.IntegrationsLoaded -> {
                var selectedItemId by remember {mutableStateOf(state.list[0].id)}

                Text(
                    text = stringResource(R.string.choose_integration),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                for (item in state.list) {
                    IntegrationItem(
                        item = item,
                        isSelected = item.id == selectedItemId
                    ) {
                        selectedItemId = it.id
                        onElementClick(item)
                    }
                }
            }
            else -> {

            }
        }
    }
}

@Composable
fun IntegrationItem(
    modifier: Modifier = Modifier,
    item: IntegrationEntity,
    isSelected: Boolean,
    onClick: (IntegrationEntity) -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {onClick(item) }
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Icon(
            imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error,
        )
    }
}


@Preview
@Composable
private fun StudentRegistrationScreenPreview() {
    QueueAppTheme() {
        StudentRegistrationScreen(
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