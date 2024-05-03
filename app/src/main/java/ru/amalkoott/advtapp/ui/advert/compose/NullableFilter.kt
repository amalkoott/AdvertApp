package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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

// может быть выбрано что-то одно или ничего
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NullableFilter(name: String, map: SnapshotStateMap<String,Boolean>, setValue: (String?)-> Unit,){
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = name, modifier = Modifier.padding(4.dp))
        RowWrap {
            val livingTypes = map.keys.toList()
            livingTypes.forEach { type->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        scope.launch{
                            for (key in map.keys) {
                                if (key != type) map[key] = false
                            }
                            map[type] = !map[type]!!//@todo возможно здетсь будет проблема с конвертацией туалета (см. vm.setToilet())
                            if (map[type]!!) { setValue(type); return@launch }
                            else{ setValue(null)}
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NullableFilter(name: String, map: MutableState<MutableMap<String,Boolean>>, setValue: (String?)-> Unit,){
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = name, modifier = Modifier.padding(4.dp))
        RowWrap {
            val livingTypes = map.value.keys.toList()
            livingTypes.forEach { type->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        scope.launch {
                            for (key in map.value.keys) {
                                if (key != type) map.value[key] = false
                            }
                            map.value[type] = !map.value[type]!!//@todo возможно здетсь будет проблема с конвертацией туалета (см. vm.setToilet())
                            if (map.value[type]!!) { setValue(type); return@launch
                            }
                            else{ setValue(null)}
                        }
                        },
                    label = { Text(text = type)},
                    selected = map.value[type]!!,
                    leadingIcon = { }
                )
            }
        }
        /*
        LazyHorizontalGrid(rows = GridCells.Fixed(2), Modifier.height(300.dp)) {
            val livingTypes = map.keys.toList()
            items(items = livingTypes) { type ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        for (key in map.keys) {
                            if (key != type) map[key] = false
                        }
                        map[type] = !map[type]!!
                        if (map[type]!!) setValue(type)
                    },
                    label = {},
                    selected = map[type]!!,
                    leadingIcon = { Text(text = type) }
                )
            }
        }

         */
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NullableFilter(map: SnapshotStateMap<String,Boolean>, setValue: (String?)-> Unit,){
    RowWrap {
        val livingTypes = map.keys.toList()
        livingTypes.forEach { type->
            FilterChip(
                modifier = Modifier.padding(0.dp),
                onClick = {
                    for (key in map.keys){ if(key != type) map[key] = false }
                    map[type] = !map[type]!!
                    if (map[type]!!) { setValue(type); return@FilterChip }
                    else{ setValue(null)}
                },
                label = { Text(text = type)},
                selected = map[type]!!,
                leadingIcon = { }
            )
            //if (map[type]!!)setValue(type)
        }
    }

}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewNullableFilter(){
    val map = remember { mutableStateMapOf<String,Boolean>(
        "Не первый" to false,
        "Не последний" to false,
        "Только последний" to false,
    ) }

    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween) {
        Text(text = "name", modifier = Modifier.padding(4.dp))
        LazyHorizontalGrid(rows = GridCells.Fixed(2), Modifier.height((32 * map.size).dp)){
            val livingTypes = map.keys.toList()
            items(items = livingTypes){type ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        for (key in map.keys){ if(key != type) map[key] = false }
                        map[type] = !map[type]!!},
                    label = {Text(text = type)},
                    selected = map[type]!!,
                    leadingIcon = {  }
                )
            }
        }
    }

}