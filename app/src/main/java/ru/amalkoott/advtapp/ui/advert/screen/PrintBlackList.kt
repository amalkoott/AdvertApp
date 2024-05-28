package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.amalkoott.advtapp.domain.Advert

@Composable
fun PrintBlackList(items: MutableList<Advert>){
    Column(Modifier.padding(top = 68.dp)) {
        if (items.size == 0){
            Text(text = "В черном списке нет объявлений")
        }else
        {
            LazyVerticalGrid(columns = GridCells.Fixed(1),
                Modifier
                    .fillMaxHeight()
                    .padding(start = 32.dp, end = 32.dp)
            ){
                item { Text(text = "Объявлений: <count>") }
                items(items){
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(top = 16.dp, bottom = 16.dp)
                            .clickable {
                                // появляется выбранная подборка, клик - вывод списка объявлений подборки
                                //selectAd(advert)
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ){}
                }
            }
        }
    }
}