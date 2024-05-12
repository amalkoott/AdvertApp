package ru.amalkoott.advtapp.ui.advert.compose.screenCompose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.ui.advert.screen.ImageFromUrl

@Composable
fun SetInfo(ads: MutableStateFlow<List<Advert>>?,
            selectAd: (Advert)-> Unit,
            removeAd: (Advert)-> Unit,
            addFavourites: (Advert)-> Unit,
            ){
    val context = LocalContext.current
    val adverts by ads!!.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxHeight()
            //.padding(top = 176.dp)
            .background(Color.Transparent))
    // Карточки с картинками
    {item {
        Text(
            text = "Объявлений: " + adverts.size.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp))
    }
        items(adverts){advert ->
            Card(
                colors = CardDefaults.cardColors(
                    //containerColor = MaterialTheme.colorScheme.surfaceTint,
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(start = 24.dp, top = 16.dp, bottom = 16.dp, end = 24.dp)
                    .clickable {
                        // появляется выбранная подборка, клик - вывод списка объявлений подборки
                        selectAd(advert)
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ){
                Column(){
                    Box(
                        Modifier
                            .shadow(
                                elevation = 1.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .weight(2f)
                            .background(
                                Color.LightGray,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .fillMaxSize(),
                        //  colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        ImageFromUrl(url = advert.images[0])
                        Text(
                            text = (adverts.indexOf(advert) + 1).toString(),
                            modifier = Modifier.padding(all = 8.dp))

                    }
                    Column(
                        Modifier
                            .weight(2f)
                            .padding(
                                start = 24.dp,
                                top = 16.dp,
                                bottom = 8.dp,
                                end = 24.dp
                            )
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(
                                text = advert.name.toString(),
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                //style = MaterialTheme.typography.titleMedium.copy(hyphens = Hyphens.None),
                                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                            )
                            Text(
                                text = (advert.price!!).toString() + ' ' + '₽',
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 0.dp, bottom = 0.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = advert.description.toString(),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Row(
                            modifier = Modifier
                                .padding(end = 0.dp)
                                .height(48.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                                    //selectedAd.value = advert
                                    //selectAd(advert)
                                    removeAd(advert)
                                },
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp),
                                // .padding(all = 8.dp),
                                content = {
                                    Icon(
                                        Icons.Filled.Delete,
                                        modifier = Modifier
                                            .height(24.dp)
                                            .width(24.dp),
                                        contentDescription = "Localized description",
                                    )
                                })
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Location", Toast.LENGTH_SHORT).show()
                                    //   vm.onSettingsClick()
                                },
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp),
                                // .padding(all = 8.dp),
                                content = {
                                    Icon(
                                        Icons.Filled.LocationOn,
                                        modifier = Modifier
                                            .height(24.dp)
                                            .width(24.dp),
                                        contentDescription = "Localized description",
                                    )
                                })
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Fav", Toast.LENGTH_SHORT).show()
                                    //selectedAd.value = advert
                                    selectAd(advert)
                                    addFavourites(advert)
                                },
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp),
                                //.padding(all = 8.dp),
                                content = {
                                    if (advert.isFavourite){
                                        Icon(
                                            Icons.Filled.Favorite,
                                            modifier = Modifier
                                                .height(24.dp)
                                                .width(24.dp),
                                            contentDescription = "Localized description",
                                        )
                                    }else{
                                        Icon(
                                            Icons.Filled.FavoriteBorder,
                                            modifier = Modifier
                                                .height(24.dp)
                                                .width(24.dp),
                                            contentDescription = "Localized description",
                                        )
                                    }

                                })
                        }
                    }
                }
            }
        }
    }
}