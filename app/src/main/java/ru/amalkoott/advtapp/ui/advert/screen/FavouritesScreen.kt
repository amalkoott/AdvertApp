package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.amalkoott.advtapp.domain.Advrt

@Composable
fun PrintFavourites(favs: SnapshotStateList<Advrt>,
                    deleteFavourites: (Advrt)-> Unit,
                    selectedAd: MutableState<Advrt?>,
                    selectAd: (Advrt)-> Unit){
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .padding(top = 65.dp)
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxHeight()
    ) {
        items(favs) { advert ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .clickable {
                        // появляется выбранная подборка, клик - вывод списка объявлений подборки
                        selectAd(advert)
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
            ) {

                    Column {
                        Text(text = advert.price.toString() + ' '+'Р')
                        Text(text = String.format("%s, %f кв.м, %d комнаты, этаж %d",advert.name,advert.footage,advert.room, advert.floor))

                    IconButton(
                        onClick = {
                            //selectedAd.value = advert
                            //selectAd(advert)
                            deleteFavourites(advert)
                        },
                        modifier = Modifier.width(80.dp),
                        content = {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Localized description",
                            )
                        })
                }
            }
        }
    }
}