package ru.naburnm8.queueapp.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.naburnm8.queueapp.R

@Composable
fun GenericErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onExitClick: () -> Unit
) {
    BackHandler() {
        onExitClick()
    }


    Column (
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onExitClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.error_occurred),
                tint = MaterialTheme.colorScheme.error
            )
        }
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
    }

}

@Composable
fun GenericLoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Preview
@Composable
fun GenericLoadingScreenPreview() {
    GenericLoadingScreen(modifier = Modifier.fillMaxSize(),)
}

@Preview
@Composable
private fun GenericErrorScreenPreview() {
    GenericErrorScreen(
        modifier = Modifier.fillMaxSize(),
        errorMessage = "Login failed. Please check your credentials and try again.",
        onExitClick = {}
    )
}

@Composable
fun GenericDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    text: String = stringResource(R.string.are_you_sure)
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.confirm_request))
        },
        text = {
            Text(text)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}