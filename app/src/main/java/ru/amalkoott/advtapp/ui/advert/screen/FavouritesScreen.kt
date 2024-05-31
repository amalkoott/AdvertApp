package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.amalkoott.advtapp.domain.Advert

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrintFavourites(favs: List<Advert>,//SnapshotStateList<Advert>,
                    deleteFavourites: (Advert)-> Unit,
                    selectedAd: MutableState<Advert?>,
                    selectAd: (Advert)-> Unit){
    if (favs.size == 0){

        Column(Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "В списке избранного нет объявлений",
                color = MaterialTheme.colorScheme.outline,
                fontSize = 16.sp)
        }

    }else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .padding(top = 65.dp)
                .padding(top = 16.dp, bottom = 16.dp, end = 32.dp, start = 32.dp)
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
                        .height(224.dp)
                        .padding(vertical = 12.dp)
                        .clickable {
                            // появляется выбранная подборка, клик - вывод списка объявлений подборки
                            selectAd(advert)
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(4f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Column(Modifier.weight(5f)) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(5f),
                                shape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp,),
                            ) {
                                val images = advert.images
                                if (images != null) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(images[0])
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.height(250.dp)
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = advert.name.toString(),
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = (advert.price!!).toString() + ' ' + '₽',
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp,
                                )
                            }
                        }
                        VerticalDivider(Modifier.fillMaxHeight())
                        IconButton(
                            onClick = {
                                deleteFavourites(advert)
                            },
                            modifier = Modifier
                                .fillMaxSize()//.size(48.dp)
                                .weight(1f),
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
}