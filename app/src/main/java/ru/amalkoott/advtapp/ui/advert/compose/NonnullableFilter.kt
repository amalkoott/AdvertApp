package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.SelectableChipBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.amalkoott.advtapp.domain.Advert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonnullableFilter(name:String, map: SnapshotStateMap<String, Boolean>, setValue: (String)-> Unit,){
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = name,
            modifier = Modifier.padding(4.dp))
        RowWrap {
            val livingTypes = map.keys.toList()
            livingTypes.forEach { type ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        for (key in map.keys){ map[key] = false }
                        map[type] = !map[type]!!
                        //if (map[type]!!) setValue(type)//selected = type
                    },
                    label = {Text(text = type)},
                    selected = map[type]!!,
                    leadingIcon = {
                        /*Text(text = type)
                         */
                    }
                )
                if (map[type]!!) setValue(type)//selected = type
            }
        }
        /*
        LazyHorizontalGrid(rows = GridCells.Fixed(2),
            Modifier
                .height((map.size * 32).dp)
                .padding(0.dp)){
            val livingTypes = map.keys.toList()
            items(items = livingTypes){type ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        for (key in map.keys){ map[key] = false }
                        map[type] = !map[type]!!
                        if (map[type]!!) setValue(type)//selected = type
                    },
                    label = {Text(text = type)},
                    selected = map[type]!!,
                    leadingIcon = {
                        /*Text(text = type)
                         */
                    }
                )
            }
        }

         */
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonnullableFilter(map: SnapshotStateMap<String, Boolean>, setValue: (String)-> Unit,){
    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween) {
        RowWrap {
            val livingTypes = map.keys.toList()
            livingTypes.forEach {type ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        for (key in map.keys){ map[key] = false }
                        map[type] = !map[type]!!
                        if (map[type]!!) setValue(type)//selected = type
                    },
                    label = {Text(text = type)},
                    selected = map[type]!!,
                    leadingIcon = {
                        /*Text(text = type)
                         */
                    }
                )
            }
        }
        /*
        LazyHorizontalGrid(rows = GridCells.Fixed(2),
            Modifier
                .height((map.size * 32).dp)
                .padding(0.dp)){
            val livingTypes = map.keys.toList()
            items(items = livingTypes){type ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        for (key in map.keys){ map[key] = false }
                        map[type] = !map[type]!!
                        if (map[type]!!) setValue(type)//selected = type
                    },
                    label = {Text(text = type)},
                    selected = map[type]!!,
                    leadingIcon = {
                        /*Text(text = type)
                         */
                    }
                )
            }
        }

         */
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewNonnullableFilter(){
    val name: String = "Этаж"
    val map = remember { mutableStateMapOf<String,Boolean>(
        "Не первый" to false,
        "Не последний" to false,
        "Только последний" to false,
    ) }

    Column(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween) {
        Text(text = name,
            modifier = Modifier.padding(4.dp))
        LazyHorizontalGrid(rows = GridCells.Fixed(2),
            Modifier
                .height((map.size * 32).dp)
                .padding(0.dp)){
            val livingTypes = map.keys.toList()
            items(items = livingTypes){type ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        for (key in map.keys){ map[key] = false }
                        map[type] = !map[type]!!
                    },
                    label = {Text(text = type,)},
                    selected = map[type]!!,
                    leadingIcon = {
                    /*Text(text = type,) */
                    }
                )
            }
        }
    }

}