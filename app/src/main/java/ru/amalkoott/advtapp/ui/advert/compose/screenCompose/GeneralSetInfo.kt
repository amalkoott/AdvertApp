package ru.amalkoott.advtapp.ui.advert.compose.screenCompose

import android.util.Log
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.ui.advert.compose.DropdownFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSetInfo(
    name: String,
    updateInterval: Int,
    onNameChange: (String) -> Unit,
    onUpdateIntervalChange: (Int) -> Unit
) {
    Column {
        TextField(
            value = name,
            onValueChange = { onNameChange(it) },
            label = { Text("Название подборки") },
            placeholder = { Text("Введите название") },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        TextField(
            value = updateInterval.toString(),
            onValueChange = {
                try {
                    onUpdateIntervalChange(it.toInt())
                } catch (e: NumberFormatException) {
                    onUpdateIntervalChange(10) // По умолчанию
                }
            },
            label = { Text("Интервал обновления") },
            placeholder = { Text("Введите значение") },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSetInfo(selected: MutableState<AdSet?>, setChange: suspend ()-> Unit,
) {
    val scope = rememberCoroutineScope()
    var update_interval by remember { mutableStateOf(selected.value!!.update_interval) }
    var name by remember { mutableStateOf(selected.value!!.name) }

    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)
    ){
        TextField(
            value = name!!,
            onValueChange = {
                name = it
                selected.value!!.name = it
                scope.launch {
                    setChange()
                }
                 },
            label = { Text("Название подборки",
                color = MaterialTheme.colorScheme.onSurfaceVariant) },
            placeholder = { Text(
                text = "Введите название",
                color = MaterialTheme.colorScheme.onSurfaceVariant) },

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface),

            textStyle = MaterialTheme.typography.bodyLarge
                .copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth()
        )

        val scope = rememberCoroutineScope()
        Column(modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 32.dp, end = 32.dp)
            .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Интервал обновления", color = MaterialTheme.colorScheme.onSurfaceVariant)
            val context = LocalContext.current
            val items = arrayOf("5 минут","10 минут", "15 минут", "30 минут", "1 час", "3 часа", "6 часов", "8 часов", "12 часов", "16 часов", "24 часа")
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
                        modifier = Modifier.menuAnchor().fillMaxWidth()
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
                val time = selectedText.split(' ')[0].toInt()
                if (!expanded) {
                    if (selectedText.contains("мин"))
                        selected.value!!.update_interval = time
                    else selected.value!!.update_interval = time*60//setCategory(selectedText)
                }

                Log.d("interval","${selected.value!!.update_interval}")
            }
        }
        /*
        TextField(
            value = update_interval.toString(),
            onValueChange = {
                    try{
                        update_interval = it.toInt()
                        selected.value!!.update_interval = it.toInt()
                    }catch (e: NumberFormatException){
                        // если интервал не указан, то идет значение по умолчанию
                        update_interval = 10
                        selected.value!!.update_interval = 10
                    }
                scope.launch {
                    setChange()
                }
            },
            label = { Text("Интервал обновления",
                color = MaterialTheme.colorScheme.onSurfaceVariant) },
            placeholder = { Text(
                text = "Введите значение",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface
            ),
            textStyle = MaterialTheme.typography.bodyLarge
                .copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp,
                )
                .fillMaxWidth()
        )
        */
    }

}