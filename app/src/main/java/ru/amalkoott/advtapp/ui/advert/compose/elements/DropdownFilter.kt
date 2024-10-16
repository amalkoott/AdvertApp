package ru.amalkoott.advtapp.ui.advert.compose.elements

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFilter(items:Array<String>, name:String, setCategory:(String) -> Unit) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .padding(top = 16.dp, bottom = 16.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = name, color = MaterialTheme.colorScheme.onSurfaceVariant)
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(items[0]) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = selectedText,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
        scope.launch {
            if (!expanded) setCategory(selectedText)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownAllFilter(items:MutableMap<String,Boolean>, name:String, setCategory:(String) -> Unit, text: MutableState<String>) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .padding(top = 16.dp, bottom = 16.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = name, color = MaterialTheme.colorScheme.onSurfaceVariant)
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(text.value)}//items[0]) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = text.value,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    val rooms = items.toSortedMap(naturalOrder()).keys.toList()
                    rooms.forEach { type ->
                        DropdownMenuItem(
                            modifier = Modifier.padding(4.dp).background(color = if (items[type]!!) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface),
                            //
                            text = { Text(text = type, color = if (items[type]!!) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface)},
                            onClick = {
                                scope.launch{
                                    items[type] = !items[type]!!
                                    if (items[type]!!) { setCategory(type); return@launch }
                                    else {setCategory("-$type")}
                                }
                            },
                        )
                    }
                }
            }
        }
        scope.launch {
            //if (!expanded) setCategory(selectedText.toString())
        }
    }
}
private fun getSelectedText(items: MutableMap<String,Boolean>):String{
    var studio = ""
    var rooms = ""
    items.keys.sorted().forEach{
        if (it.length > 2) studio += it
        if(items[it]!!) {
            rooms += "$it, "
        }
    }
    val result = if (rooms.length>0) { if (studio.length > 0) ", Комнаты: $rooms" else "Комнаты: $rooms"} else ""
    return result
}
