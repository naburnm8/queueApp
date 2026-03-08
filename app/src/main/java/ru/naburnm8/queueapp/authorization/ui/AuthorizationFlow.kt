package ru.naburnm8.queueapp.authorization.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.authorization.response.AuthorizationResponse
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel

@Composable
fun AuthorizationFlow(
    navigationViewmodel: NavigationViewmodel = koinViewModel()
) {

    Column(modifier = Modifier.fillMaxSize()) {

        Button(onClick = {navigationViewmodel.authorizeAsQueueOperator()}) {
            Text("Авторизоваться как учитель")
        }

        Button(onClick = {navigationViewmodel.authorizeAsQueueConsumer()}) {
            Text("Авторизоваться как студент")
        }

    }
}