package ru.amalkoott.advtapp.ui.advert.compose.screens

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
import ru.amalkoott.advtapp.domain.entities.AdSet


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSetInfo(selected: MutableState<AdSet?>, setChange: suspend ()-> Unit,
) {
    val scope = rememberCoroutineScope()
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
            val times = mapOf(
                5 to "5 минут",
                10 to "10 минут",
                15 to "15 минут",
                30 to "30 минут",
                60 to "1 час",
                180 to "3 часа",
                360 to "6 часов",
                480 to "8 часов",
                720 to "12 часов",
                960 to "16 часов",
                1440 to "24 часа",
            ).toSortedMap()
            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember { try {
                mutableStateOf(times[selected.value!!.update_interval!!])
            }catch (e:Exception){
                mutableStateOf(times[5])
            }}

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
                        value = selectedText!!,
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
                        times.values.forEach{item ->
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
                val time = selectedText!!.split(' ')[0].toInt()
                if (!expanded) {
                    if (selectedText!!.contains("мин"))
                        selected.value!!.update_interval = time
                    else selected.value!!.update_interval = time*60
                }
            }
        }
    }

}