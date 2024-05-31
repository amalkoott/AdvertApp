package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BinaryFilter(firstValue: String, secondValue: String, setValue: (String)-> Unit,){
    var type by remember { mutableStateOf(false) }
    Row(modifier = Modifier.padding(vertical = 8.dp)){
        FilterChip(
            modifier = Modifier.padding(end = 4.dp),
            onClick = {
                type = !type
                      },
            label = {
                Text(
                    text = firstValue,
                )},
                    selected = !type,
                    leadingIcon = {  }
                )
        FilterChip(
            onClick = {
                type = !type
                      },
            label = {Text(text = secondValue)},
            selected = type,
            leadingIcon = {  }
        )
    }
    val scope = rememberCoroutineScope()
    scope.launch{
        setValue(type.toString())
    }
}
