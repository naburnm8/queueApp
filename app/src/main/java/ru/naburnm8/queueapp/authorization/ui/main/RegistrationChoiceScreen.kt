package ru.naburnm8.queueapp.authorization.ui.main

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.entity.Role
import ru.naburnm8.queueapp.authorization.entity.RoleEntity
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme




@Composable
fun RegistrationChoiceScreen(
    modifier: Modifier = Modifier,
    onStudentRegistrationClick: () -> Unit,
    onTeacherRegistrationClick: () -> Unit,
    availableRoles: List<RoleEntity> = listOf(
        RoleEntity(identifier = Role.ROLE_QCONSUMER, displayName = stringResource(R.string.student)),
        RoleEntity(identifier = Role.ROLE_QOPERATOR, displayName = stringResource(R.string.teacher))
    )
){
    var chosenRole by rememberSaveable { mutableStateOf(availableRoles[0].identifier) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.choose_role),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.primaryContainer).verticalScroll(
                rememberScrollState()
            ),
        ) {
            for (role in availableRoles) {
                RoleItem(
                    role = role,
                    isChosen = chosenRole == role.identifier
                ) {
                    chosenRole = role.identifier
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (chosenRole == Role.ROLE_QCONSUMER) {
                    onStudentRegistrationClick()
                } else {
                    onTeacherRegistrationClick()
                }
            },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
        ) {
            Text(
                text = stringResource(R.string.next)
            )
        }



    }

}

@Composable
private fun RoleItem(
    modifier: Modifier = Modifier,
    role: RoleEntity,
    isChosen: Boolean,
    onClick: (RoleEntity) -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {onClick(role) }

            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimaryContainer)

            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = role.displayName,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Icon(
            imageVector = if (isChosen) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isChosen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error,
        )
    }
}

@Preview
@Composable
private fun RegistrationChoiceScreenPreview() {
    QueueAppTheme() {
        RegistrationChoiceScreen(
            modifier = Modifier.fillMaxSize(),
            onStudentRegistrationClick = {},
            onTeacherRegistrationClick = {},
        )
    }

}