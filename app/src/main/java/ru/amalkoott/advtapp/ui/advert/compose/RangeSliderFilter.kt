package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSliderFilter(
    name: String,
    minValue: Float,
    maxValue: Float,
    setMinValue:(String)-> Unit,
    setMaxValue:(String) -> Unit
) {
    var minSliderPosition by remember { mutableStateOf(minValue) }
    var maxSliderPosition by remember { mutableStateOf(maxValue) }
    Column (
        modifier = Modifier
            .padding(
                vertical = 8.dp
            )
            .fillMaxWidth())
    {
        Column {
            Text(
                text = name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
                //color = MaterialTheme.colorScheme.surface
            )
            Row{
                TextField(
                    label = { Text("От",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    value = minSliderPosition.toString(),
                    onValueChange = {
                        setMinValue(it)
                    },
                    //label = { Text("Min Price") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurface),
                )

                TextField(
                    label = { Text("До",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    value = maxSliderPosition.toString(),
                    onValueChange = {
                        setMaxValue(it)
                    },
                    //label = { Text("Max Price") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurface),
                )
            }
        }

        RangeSlider(
            value = (minSliderPosition..maxSliderPosition),
            onValueChange = { range ->
               // setMinValue(range.start.toString())
              //  setMaxValue(range.endInclusive.toString())
                minSliderPosition = range.start
                maxSliderPosition = range.endInclusive
            },
            modifier = Modifier.padding(horizontal = 16.dp),
        )


    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewRangeSliderFilter() {
    val name = "Цена"
    val minValue: Float = 0f
    val maxValue: Float = 100f
    var minSliderPosition by remember { mutableStateOf(minValue) }
    var maxSliderPosition by remember { mutableStateOf(maxValue) }
    Column (
        modifier = Modifier
            .padding(
                start = 0.dp,
                end = 0.dp,
                bottom = 24.dp,
            )
            .fillMaxWidth())
    {
        Column(verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start) {
            Text(
                text = name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
            )
            Row {
                TextField(
                    label = { Text("От",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    value = minSliderPosition.toString(),
                    onValueChange = {
                    },
                    //label = { Text("Min Price") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurface),
                )

                TextField(
                    label = { Text("До",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    value = maxSliderPosition.toString(),
                    onValueChange = {
                    },
                    //label = { Text("Max Price") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurface),
                )
            }
        }

        RangeSlider(
            value = (minSliderPosition..maxSliderPosition),
            onValueChange = { range ->
                // setMinValue(range.start.toString())
                //  setMaxValue(range.endInclusive.toString())
                minSliderPosition = range.start
                maxSliderPosition = range.endInclusive
            },
            modifier = Modifier.padding(horizontal = 16.dp),
        )


    }
}