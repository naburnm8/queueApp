package ru.naburnm8.queueapp.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.naburnm8.queueapp.profile.entity.ProfileEntity
import ru.naburnm8.queueapp.profile.entity.ProfileMultifieldType
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import java.util.UUID

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    profile: ProfileEntity,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.height(96.dp).width(96.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${profile.lastName} ${profile.firstName} ${profile.patronymic ?: ""}",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp).width(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(4.dp))
                    Text (
                        text = profile.multifield,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(8.dp))
                    if (profile.telegram != null) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.height(16.dp).width(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.width(4.dp))
                        Text (
                            text = "@${profile.telegram}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onEditClick() },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }

            Button(
                onClick = { onLogoutClick() },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ExitToApp,
                    contentDescription = null
                )
            }

        }
    }

}


@Preview
@Composable
fun ProfileCardPreview() {
    QueueAppTheme() {
        ProfileCard(
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
            onEditClick = {},
            onLogoutClick = {}
        )
    }

}

@Preview
@Composable
fun ProfileCardPreviewDark() {
    QueueAppTheme(
        darkTheme = true
    ) {
        ProfileCard(
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
            onEditClick = {},
            onLogoutClick = {}
        )
    }

}