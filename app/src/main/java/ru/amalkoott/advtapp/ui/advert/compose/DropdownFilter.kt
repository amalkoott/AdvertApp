package ru.amalkoott.advtapp.ui.advert.compose

import android.widget.Toast
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFilter(items:Array<String>, name:String, setCategory:(String) -> Unit) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .padding(
            start = 16.dp,
            // end = 16.dp,
            bottom = 24.dp,
        )
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = name, color = MaterialTheme.colorScheme.onSurfaceVariant)
        val context = LocalContext.current
        //val coffeeDrinks = arrayOf("Недвижимость", "Транспорт", "Услуги")
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(items[0]) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
            //.padding(32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
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
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                            }
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
