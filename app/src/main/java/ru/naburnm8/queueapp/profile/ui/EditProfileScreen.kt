package ru.naburnm8.queueapp.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.authorization.ui.main.SlideToConfirm
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMultifieldType
import ru.naburnm8.queueapp.profile.entity.UpdateProfileEntity
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    profile: ProfileEntity,
    onConfirmClick: (UpdateProfileEntity) -> Unit,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var multifield by remember { mutableStateOf("") }
    var telegram by remember { mutableStateOf("") }

    var firstNameError by remember {mutableStateOf(false)}
    var lastNameError by remember {mutableStateOf(false)}
    var multifieldError by remember {mutableStateOf(false)}

    var sliderActive by remember {mutableStateOf(true)}

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.edit_profile),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            value = multifield,
            onValueChange = { multifield = it },
            label = {
                        Text(
                            text = if (profile.multifieldType == ProfileMultifieldType.ACADEMIC_GROUP) stringResource(R.string.academic_group)
                                    else stringResource(R.string.department)
                        )
                    },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {Icon(imageVector = Icons.Default.Info, contentDescription = "")},
            isError = multifieldError
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

        Spacer(modifier = Modifier.height(36.dp))

        SlideToConfirm(
            text = stringResource(R.string.slide_to_confirm),
            enabled = sliderActive
        ) {
            firstNameError = false
            lastNameError = false
            multifieldError = false

            if (firstName == "") firstNameError = true
            if (lastName == "") lastNameError = true
            if (multifield == "") multifieldError = true

            if (!firstNameError && !lastNameError && !multifieldError) {
                onConfirmClick(
                    UpdateProfileEntity(
                        firstName,
                        lastName,
                        patronymic,
                        multifield,
                        telegram,
                        profile.avatarUrl,
                        profile.multifieldType
                    )
                )
            }
        }
    }

}


@Preview
@Composable
fun EditProfileScreenPreview() {
    QueueAppTheme() {
        EditProfileScreen(
            profile = ProfileEntity(
                id = UUID(0, 0),
                firstName = "Артем",
                lastName = "Линт",
                patronymic = "Дмитриевич",
                multifield = "ИУ3",
                multifieldType = ProfileMultifieldType.DEPARTMENT,
                telegram = "naburnm8",
                avatarUrl = ""
            ),
            onConfirmClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
@Preview
@Composable
fun EditProfileScreenPreviewDark() {
    QueueAppTheme(
        darkTheme = true
    ) {
        EditProfileScreen(
            profile = ProfileEntity(
                id = UUID(0, 0),
                firstName = "Артем",
                lastName = "Линт",
                patronymic = "Дмитриевич",
                multifield = "ИУ3-82Б",
                multifieldType = ProfileMultifieldType.ACADEMIC_GROUP,
                telegram = "naburnm8",
                avatarUrl = ""
            ),
            onConfirmClick = {}
        )
    }
}