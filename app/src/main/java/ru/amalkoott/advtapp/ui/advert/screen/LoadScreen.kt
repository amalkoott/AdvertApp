package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadScreen(value: Boolean, isSuccessful: MutableState<Boolean?>, cancelSearch: () -> Unit) {
    var loading by remember { mutableStateOf(value) }
    var isSearching by remember { isSuccessful}

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isSearching == false){
            Text(text = "Объявления не найдены!")
            Text(text = "Попробуйте поменять фильтры или выполните поиск позже...")
            Button(onClick = {
                cancelSearch()
                //loading = false
            }) {
                Text("Назад")
            }
        }else{

            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Row {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp).padding(bottom = 48.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        strokeWidth = 8.dp
                    )
                }
                Row {
                    Text(text = "Ищем объявления...")
                }
            }


        }
    }
}
