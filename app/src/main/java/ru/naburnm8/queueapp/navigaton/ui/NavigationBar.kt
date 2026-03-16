package ru.naburnm8.queueapp.navigaton.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationState
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.queueConsumer.navigation.QueueConsumerFlowNavigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme

private data class NavigationButtonConfig(
    val route: String,
    val painter: Painter,
    val contentDescription: String,
)

@Composable
fun QueueConsumerNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    onTabClick: (String) -> Unit,
) {
    val buttons = listOf(
        NavigationButtonConfig(
            route = QueueConsumerFlowNavigation.MyQueue.name,
            painter = painterResource(R.drawable.queue),
            contentDescription = stringResource(R.string.my_queues)
        ),
        NavigationButtonConfig(
            route = QueueConsumerFlowNavigation.MyRequests.name,
            painter = painterResource(R.drawable.submission_request),
            contentDescription = stringResource(R.string.my_submission_requests)
        ),
        NavigationButtonConfig(
            route = QueueConsumerFlowNavigation.MyProfile.name,
            painter = painterResource(R.drawable.profile),
            contentDescription = stringResource(R.string.profile)
        )
    )

    AbstractNavigationBar(
        modifier = modifier,
        currentDestination = currentDestination,
        buttons = buttons,
        onTabClick = onTabClick
    )
}

@Composable
fun QueueOperatorNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    onTabClick: (String) -> Unit,
) {
    val buttons = listOf(
        NavigationButtonConfig(
            route = QueueOperatorFlowNavigation.MyQueues.name,
            painter = painterResource(R.drawable.queue),
            contentDescription = stringResource(R.string.my_queues)
        ),
        NavigationButtonConfig(
            route = QueueOperatorFlowNavigation.Metrics.name,
            painter = painterResource(R.drawable.star),
            contentDescription = stringResource(R.string.metrics)
        ),

        NavigationButtonConfig(
            route = QueueOperatorFlowNavigation.Disciplines.name,
            painter = painterResource(R.drawable.book),
            contentDescription = stringResource(R.string.disciplines)
        ),
        NavigationButtonConfig(
            route = QueueOperatorFlowNavigation.Profile.name,
            painter = painterResource(R.drawable.profile),
            contentDescription = stringResource(R.string.profile)
        )
    )

    AbstractNavigationBar(
        modifier = modifier,
        currentDestination = currentDestination,
        buttons = buttons,
        onTabClick = onTabClick
    )
}

@Composable
private fun AbstractNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    buttons: List<NavigationButtonConfig>,
    onTabClick: (String) -> Unit
) {
    Row (
        modifier = modifier.fillMaxWidth()
            .padding(top = 10.dp, bottom = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        buttons.forEach { config ->
            val isSelected = currentDestination
                ?.hierarchy
                ?.any { it.route == config.route } == true
            NavigationButton(
                painter = config.painter,
                contentDescription = config.contentDescription,
                isSelected = isSelected,
                onClick = { onTabClick(config.route) }
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
                elevation = if (isSelected) 1.dp else 5.dp,
                clip = true,
                shape = RoundedCornerShape(10.dp),
                spotColor = mainColor,
                ambientColor = mainColor
            )
            .background(MaterialTheme.colorScheme.primaryContainer)
            .height(72.dp)
            .width(72.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = mainColor,
            modifier = Modifier
                .padding(8.dp)
        )

        Text(
            text = contentDescription,
            color = mainColor,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}

@Composable
@Preview
private fun NavigationButtonPreview() {
    QueueAppTheme (darkTheme = false) {
        NavigationButton(
            painter = painterResource(R.drawable.queue),
            contentDescription = "My Queues",
            isSelected = false,
            onClick = {}
        )
    }
}
