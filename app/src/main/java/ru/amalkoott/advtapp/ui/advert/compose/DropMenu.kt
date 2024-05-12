package ru.amalkoott.advtapp.ui.advert.compose

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.domain.AdSet

@Composable
fun DropMenu(onDeleteSet: ()-> Unit, onUpdateSet: ()-> Unit){
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(onClick = {
        coroutineScope.launch {
            expanded = !expanded
        }
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Localized description"
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Удалить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить асинхронную операцию, например, вызов удаления
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                    onDeleteSet()
                }
            }
        )
        DropdownMenuItem(
            text = { Text("Обновить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить другую асинхронную операцию, например, обновление данных
                    // TODO Обновление подборки
                    Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show()
                    onUpdateSet()
                }
            }
        )
    }
}


@Composable
fun DropMenu(onDeleteAd: ()-> Unit){
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(onClick = {
        coroutineScope.launch {
            expanded = !expanded
        }
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Localized description"
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Удалить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить асинхронную операцию, например, вызов удаления
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                    onDeleteAd()
                }
            }
        )
        /*
        DropdownMenuItem(
            text = { Text("Обновить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить другую асинхронную операцию, например, обновление данных
                    // TODO Обновление подборки
                    Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show()
                }
            }
        )
        */
    }
}
