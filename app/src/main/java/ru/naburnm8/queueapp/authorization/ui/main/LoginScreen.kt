package ru.naburnm8.queueapp.authorization.ui.main


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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (String, String) -> Unit,
    onRegistrationClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}

    var emailError by remember {mutableStateOf(false)}
    var passwordError by remember {mutableStateOf(false)}



    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email == "") {
                    emailError = true
                }
                if (password == "") {
                    passwordError = true
                }
                if (email != "" && password != ""){
                    emailError = false
                    passwordError = false
                    onLoginClick(email, password)
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRegistrationClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.register))
        }
    }

}

@Preview
@Composable
private fun LoginScreenPreview() {
    QueueAppTheme(

    ) {
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            onLoginClick = { _, _ -> },
            onRegistrationClick = { }
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreviewDark() {
    QueueAppTheme(
        darkTheme = true
    ) {
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            onLoginClick = { _, _ -> },
            onRegistrationClick = { }
        )
    }
}