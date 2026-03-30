package ru.naburnm8.queueapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.naburnm8.queueapp.R
import ru.naburnm8.queueapp.ui.theme.QueueAppTheme


@Composable
fun ImportancePickerComponent(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    steps: Int = 10,
    text: String = stringResource(R.string.importance)
) {
    Column (
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${text}: ${value.toImportanceReadableText()}",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
            ),
            steps = steps,
            valueRange = 0f..1f
        )
    }
}

@Preview
@Composable
fun ImportancePickerComponentPreview() {
    QueueAppTheme() {
        var sliderPos by remember {mutableFloatStateOf(0.5f)}

        ImportancePickerComponent(
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
            value = sliderPos,
            onValueChange = {sliderPos = it},
        )
    }
}

