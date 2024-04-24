package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFilter(value:String, name:String, placeholder:String, setValue:(String)->Unit){
    var textValue by remember { mutableStateOf(value) }
    TextField(
        value = textValue,
        onValueChange = {
            textValue = it
            setValue(it) },
        label = { Text(name,
            color = MaterialTheme.colorScheme.onSurfaceVariant) },
        placeholder = { Text(
            text = placeholder,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface
        ),
        textStyle = MaterialTheme.typography.bodyLarge
            .copy(color = MaterialTheme.colorScheme.onSurface),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewTextFilter(){
    val value = "Санкт-Петербург"
    val name:String = "Город"
    val placeholder:String = "Введите название города"
    var textValue by remember { mutableStateOf(value) }
    TextField(
        value = textValue,
        onValueChange = {
            textValue = it},
        label = { Text(name,
            color = MaterialTheme.colorScheme.onSurfaceVariant) },
        placeholder = { Text(
            text = placeholder,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface
        ),
        textStyle = MaterialTheme.typography.bodyLarge
            .copy(color = MaterialTheme.colorScheme.onSurface),
        modifier = Modifier
            .padding(
                16.dp
            //start = 16.dp,
                // end = 16.dp,
                //bottom = 24.dp,
            )
            .fillMaxWidth()
    )
}