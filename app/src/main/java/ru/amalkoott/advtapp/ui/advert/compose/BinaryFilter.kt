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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BinaryFilter(firstValue: String, secondValue: String, setValue: (String)-> Unit,){
    var type by remember { mutableStateOf(false) }
    Row(modifier = Modifier.padding(vertical = 8.dp)){
        FilterChip(
            modifier = Modifier.padding(end = 4.dp),
            onClick = {
                type = !type
                //setValue(firstValue)
                setValue(type.toString())
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
                setValue(type.toString())
                //setValue(secondValue)
                      },
            label = {Text(text = secondValue)},
            selected = type,
            leadingIcon = {  }
        )
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewBinaryFilter(){
    val firstValue: String = "1"
    val secondValue: String = "2"
    var type by remember { mutableStateOf(true) }
    Row(modifier = Modifier.padding(vertical = 8.dp)){
        FilterChip(
            onClick = {
                type = !type},
            label = {},
            selected = !type,
            leadingIcon = { Text(text = firstValue) }
        )
        FilterChip(
            onClick = {
                type = !type},
            label = {},
            selected = type,
            leadingIcon = { Text(text = secondValue) }
        )
    }
}