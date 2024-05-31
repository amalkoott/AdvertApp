package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@Composable
fun TextFilter(value:String, name:String, placeholder:String, setValue:(String)->Unit){
    var textValue by remember { mutableStateOf(value) }
    if (value != "") setValue(value)
    Column(modifier = Modifier
        .padding(vertical = 16.dp)
        .fillMaxWidth()) {
        Text(text = name, color =  MaterialTheme.colorScheme.onSurfaceVariant)
        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
                setValue(it) },
            placeholder = { Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface
            ),
            textStyle = MaterialTheme.typography.bodyLarge
                .copy(color = MaterialTheme.colorScheme.onSurface),

        )
    }

}
