package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.amalkoott.advtapp.domain.Advert

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrintFavourites(favs: SnapshotStateList<Advert>,
                    deleteFavourites: (Advert)-> Unit,
                    selectedAd: MutableState<Advert?>,
                    selectAd: (Advert)-> Unit){
    // TODO красиво оформить список избранного
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
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(168.dp)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .clickable {
                        // появляется выбранная подборка, клик - вывод списка объявлений подборки
                        selectAd(advert)
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
            ) {
                    Row(Modifier.padding(start = 16.dp,bottom = 16.dp,top = 16.dp, end = 16.dp),
                    //    verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Column(Modifier.fillMaxHeight().weight(2f),//.padding(start = 16.dp,bottom = 16.dp,top = 16.dp),
                            verticalArrangement = Arrangement.SpaceAround
                            ) {
                            Column {
                                Text(
                                    text = advert.name.toString(),
                                    fontWeight = FontWeight.SemiBold,
                                    )
                                Text(
                                    text = (advert.price!!).toString() + ' ' + '₽',
                                    fontWeight = FontWeight.SemiBold,
                                    //modifier = Modifier.padding(top = 15.dp, bottom = 5.dp)
                                )
                            }
/*
                            Text(
                                //text = advert.ad_caption.toString(),
                                text = advert.location!!,
                                maxLines = 1,
                                lineHeight = 16.sp)
                            */
                            IconButton(
                                onClick = {
                                    //selectedAd.value = advert
                                    //selectAd(advert)
                                    deleteFavourites(advert)
                                },
                                modifier = Modifier
                                    .size(48.dp),
                                content = {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Localized description",
                                    )
                                })
                        }
                        Card(modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(advert.images[0])
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.height(250.dp)
                            )
                        }
                }
            }
        }
    }
}