package ru.amalkoott.advtapp.ui.advert.compose.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeFilter(
    name: String,
    setMinValue:(String)-> Unit,
    setMaxValue:(String) -> Unit
){
    val scope = rememberCoroutineScope()
    var minValue by remember { mutableStateOf(0) }
    var maxValue by remember { mutableStateOf(0) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = name,
            color =  MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row {
            TextField(
                label = { Text("От",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                value = minValue.toString(),
                onValueChange = {
                    scope.launch {
                        try {
                            minValue = Integer.parseInt(it)
                            setMinValue(it)
                        }catch (e:NumberFormatException){
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    if(minValue > maxValue) minValue = maxValue
                   }),
                modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface
                ),
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
            )

            TextField(
                label = { Text("До",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                value = maxValue.toString(),
                onValueChange = {
                    scope.launch {
                        try {
                            maxValue = Integer.parseInt(it)
                            setMaxValue(it)
                        }catch (e:NumberFormatException){
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (maxValue < minValue) maxValue = minValue
                    keyboardController?.hide()}),
                modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface
                ),
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
            )
        }
    }
}

@Composable
fun RangeFilter(
    name: String,
    setMinValue:(String)-> Unit,
    setMaxValue:(String) -> Unit,
    modifier: Modifier
){
    val scope = rememberCoroutineScope()
    var minValue by remember { mutableStateOf(0) }
    var maxValue by remember { mutableStateOf(0) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = name,
            color =  MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        )
        Row {
            TextField(
                label = { Text("От",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                value = minValue.toString(),
                onValueChange = {
                    scope.launch {
                        try {
                            minValue = Integer.parseInt(it)
                            setMinValue(it)
                        }catch (e:NumberFormatException){
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    if(minValue > maxValue) minValue = maxValue
                }),
                modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface
                ),
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
            )

            TextField(
                label = { Text("До",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                value = maxValue.toString(),
                onValueChange = {
                    scope.launch {
                        try {
                            maxValue = Integer.parseInt(it)
                            setMaxValue(it)
                        }catch (e:NumberFormatException){
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (maxValue < minValue) maxValue = minValue
                    keyboardController?.hide()}),
                modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface
                ),
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
            )
        }
    }
}
