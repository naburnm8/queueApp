package ru.naburnm8.queueapp.navigaton.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel

private data class NavigationButtonConfig(
    val state: NavigationState,
    val painter: Painter,
    val contentDescription: String,
    val onClick: () -> Unit
)

@Composable
fun QueueConsumerNavigationBar(
    modifier: Modifier = Modifier,
    viewmodel: NavigationViewmodel = koinViewModel()
) {
    val buttons = listOf(
        NavigationButtonConfig(
            state = NavigationState.QueueConsumer.MyQueue,
            painter = painterResource(R.drawable.queue),
            contentDescription = stringResource(R.string.my_queues),
            onClick = {}
        ),
        NavigationButtonConfig(
            state = NavigationState.QueueConsumer.MyRequests,
            painter = painterResource(R.drawable.submission_request), // assignment on material icons
            contentDescription = stringResource(R.string.my_submission_requests),
            onClick = {}
        ),
        NavigationButtonConfig(
            state = NavigationState.QueueConsumer.MyProfile,
            painter = painterResource(R.drawable.profile),
            contentDescription = stringResource(R.string.profile),
            onClick = {}
        )
    )

    AbstractNavigationBar(
        modifier = modifier,
        viewmodel = viewmodel,
        buttons = buttons
    )
}

@Composable
fun QueueOperatorNavigationBar(
    modifier: Modifier = Modifier,
    viewmodel: NavigationViewmodel = koinViewModel()
) {
    val buttons = listOf(
        NavigationButtonConfig(
            state = NavigationState.QueueOperator.MyQueues,
            painter = painterResource(R.drawable.queue),
            contentDescription = stringResource(R.string.my_queues),
            onClick = {}
        ),
        NavigationButtonConfig(
            state = NavigationState.QueueOperator.MySettings,
            painter = painterResource(R.drawable.settings),
            contentDescription = stringResource(R.string.settings),
            onClick = {}
        ),
        NavigationButtonConfig(
            state = NavigationState.QueueOperator.MyProfile,
            painter = painterResource(R.drawable.profile),
            contentDescription = stringResource(R.string.profile),
            onClick = {}
        )
    )

    AbstractNavigationBar(
        modifier = modifier,
        viewmodel = viewmodel,
        buttons = buttons
    )
}

@Composable
private fun AbstractNavigationBar(
    modifier: Modifier = Modifier,
    viewmodel: NavigationViewmodel = koinViewModel(),
    buttons: List<NavigationButtonConfig>
) {
    val currentState by viewmodel.stateFlow.collectAsState()

    Row (
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background).padding(top = 10.dp, bottom = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        buttons.forEach { config ->
            NavigationButton(
                painter = config.painter,
                contentDescription = config.contentDescription,
                isSelected = currentState == config.state,
                onClick = {
                    config.onClick()
                    viewmodel.changeState(config.state)
                }
            )
        }
    }
}

@Composable
private fun NavigationButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val mainColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary

    Column (
        modifier = modifier
            .padding(4.dp)
            .shadow(
                elevation = if (isSelected) 20.dp else 10.dp,
                clip = true,
                shape = RoundedCornerShape(10.dp),
                spotColor = mainColor,
                ambientColor = mainColor
            )
            .background(MaterialTheme.colorScheme.onBackground)
            .height(90.dp)
            .width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .wrapContentHeight(Alignment.CenterVertically)
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = mainColor,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text (
            text = contentDescription,
            textAlign = TextAlign.Center,
            color = mainColor,
            fontSize = 14.sp,
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Composable
@Preview
private fun NavigationButtonPreview() {
    NavigationButton(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = "Android",
        isSelected = false,
        onClick = {}
    )
}
