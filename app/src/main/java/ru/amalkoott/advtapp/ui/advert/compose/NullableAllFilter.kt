package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// может быть выбрано один, два или больше (все) или ничего
@Composable
fun NullableAllFilter(name: String, map: SnapshotStateMap<String,Boolean>, setValue: (String?)-> Unit,){
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = name,
            modifier = Modifier.padding(4.dp), color =  MaterialTheme.colorScheme.onSurfaceVariant)
        RowWrap/*(horizontalSpacer = 16.dp)*/{
            val livingTypes = map.toSortedMap(naturalOrder()).keys.toList()
            livingTypes.forEach { type->
                FilterChip(
                    modifier = Modifier.padding(2.dp),
                    onClick = {
                        scope.launch{
                            map[type] = !map[type]!!
                            if (map[type]!!) { setValue(type); return@launch }
                            else {setValue("-$type")}
                        }
                              },
                    label = { Text(text = type)},
                    selected = map[type]!!,
                    leadingIcon = { }
                )
            }
        }
    }
}

@Composable
fun NullableAllFilter(name: String, map: MutableState<MutableMap<String, Boolean>>, setValue: (String?)-> Unit,){
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = name, modifier = Modifier.padding(4.dp), color =  MaterialTheme.colorScheme.onSurfaceVariant)
        RowWrap/*(horizontalSpacer = 16.dp)*/{
            val livingTypes = map.value.toSortedMap(naturalOrder()).keys.toList()
            livingTypes.forEach { type->
                FilterChip(
                    modifier = Modifier.padding(2.dp),
                    onClick = {
                        scope.launch {
                            map.value[type] = !map.value[type]!!
                            if (map.value[type]!!) { setValue(type); return@launch
                            }
                            else {setValue("-$type")}
                        }
                    },
                    label = { Text(text = type)},
                    selected = map.value[type]!!,
                    leadingIcon = { }
                )
            }
        }
    }
}

@Composable
fun NullableAllFilter(map: MutableState<MutableMap<String, Boolean>>, setValue: (String?)-> Unit,){
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 16.dp)) {
        RowWrap(horizontalSpacer = 16.dp){
            val livingTypes = map.value.toSortedMap(naturalOrder()).keys.toList()
            livingTypes.forEach { type->
                FilterChip(
                    modifier = Modifier.padding(0.dp),
                    onClick = {
                        scope.launch {
                            map.value[type] = !map.value[type]!!
                            if (map.value[type]!!) { setValue(type); return@launch
                            }
                            else {setValue("-$type")}
                        }
                    },
                    label = { Text(text = type)},
                    selected = map.value[type]!!,
                    leadingIcon = { }
                )
            }
        }
    }
}

@Composable
fun NullableAllFilter(map: MutableState<MutableMap<String, Boolean>>, setValue: (String?)-> Unit,mod: Modifier){
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = mod)//Modifier.padding(vertical = 16.dp))
    {
        RowWrap(horizontalSpacer = 16.dp){
            val livingTypes = map.value.toSortedMap(naturalOrder()).keys.toList()
            livingTypes.forEach { type->
                FilterChip(
                    modifier = Modifier.padding(0.dp),
                    onClick = {
                        scope.launch {
                            map.value[type] = !map.value[type]!!
                            if (map.value[type]!!) { setValue(type); return@launch
                            }
                            else {setValue("-$type")}
                        }
                    },
                    label = { Text(text = type)},
                    selected = map.value[type]!!,
                    leadingIcon = { }
                )
            }
        }
    }
}
