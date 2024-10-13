package ru.amalkoott.advtapp.ui.advert.compose.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ShowMoreButton(isShowMore: MutableState<Boolean>){
    val scope = rememberCoroutineScope()
    TextButton(
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
        onClick = {
            scope.launch {
                isShowMore.value = !isShowMore.value
            }
        },
        colors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.background
        )
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