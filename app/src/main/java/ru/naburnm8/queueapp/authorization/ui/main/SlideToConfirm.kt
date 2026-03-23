package ru.naburnm8.queueapp.authorization.ui.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme
import kotlin.math.roundToInt

@Composable
fun SlideToConfirm(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    resetKey: Int = 0,
    onConfirmed: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    var trackWidthPx by remember { mutableStateOf(0f) }
    val thumbSize = 56.dp
    val thumbSizePx = with(density) { thumbSize.toPx() }

    val offsetX = remember { Animatable(0f) }

    val maxOffset = (trackWidthPx - thumbSizePx).coerceAtLeast(0f)
    val confirmThreshold = maxOffset * 0.85f

    LaunchedEffect(resetKey) {
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(250)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(32.dp)
            )
            .onSizeChanged { trackWidthPx = it.width.toFloat() },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .padding(4.dp)
                .offset {
                    IntOffset(offsetX.value.roundToInt(), 0)
                }
                .size(thumbSize)
                .background(
                    color = if (enabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    shape = CircleShape
                )
                .draggable(
                    enabled = enabled && maxOffset > 0f,
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val newValue = (offsetX.value + delta).coerceIn(0f, maxOffset)
                            offsetX.snapTo(newValue)
                        }
                    },
                    onDragStopped = {
                        scope.launch {
                            if (offsetX.value >= confirmThreshold) {
                                offsetX.animateTo(
                                    targetValue = maxOffset,
                                    animationSpec = tween(150)
                                )
                                onConfirmed()
                            } else {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(250)
                                )
                            }
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "»",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}


@Preview
@Composable
fun SlideToConfirmPreview(){
    QueueAppTheme() {
        SlideToConfirm(
            text = "Slide to confirm"
        ) { }
    }
}