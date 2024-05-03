package ru.amalkoott.advtapp.ui.advert.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShowMoreButton(isShowMore: MutableState<Boolean>){
    val scope = rememberCoroutineScope()
    TextButton(
        onClick = {
            scope.launch {
                isShowMore.value = !isShowMore.value
            }
        }
    ) {
        if (isShowMore.value){
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Скрыть")
            Text("Скрыть дополнительные фильтры")
        }else{
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Раскрыть")
            Text("Показать еще фильтры")
        }
    }
}