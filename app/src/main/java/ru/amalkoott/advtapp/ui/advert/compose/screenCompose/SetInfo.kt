package ru.amalkoott.advtapp.ui.advert.compose.screenCompose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.R
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.ui.advert.screen.ImageFromUrl

@Composable
fun SetInfo(ads: MutableStateFlow<List<Advert>>?,
            selectAd: (Advert)-> Unit,
            removeAd: (Advert)-> Unit,
            addFavourites: (Advert)-> Unit,
            setContext: (Context) -> Unit,
            ){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val adverts by ads!!.collectAsState()
    val g = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.1f),
            Color.Transparent,
        )
    )
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(g)
            .zIndex(1f))
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxHeight()
                //.padding(top = 176.dp)
                .background(Color.Transparent)
                .padding(start = 32.dp, end = 32.dp))
        // Карточки с картинками
        {
            item {
                Text(
                    text = "Объявлений: " + adverts.size.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 32.dp, bottom = 8.dp))
            }
            items(adverts){advert ->
                scope.launch{ advert.saveImages()}
                Card(
                    colors = CardDefaults.cardColors(
                        //containerColor = MaterialTheme.colorScheme.surfaceTint,
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(top = 16.dp, bottom = 16.dp)
                        .clickable {
                            // появляется выбранная подборка, клик - вывод списка объявлений подборки
                            selectAd(advert)
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
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
                        ) {
                            // todo фикс images
                            val images = advert.images
                            if (images != null) ImageFromUrl(url = images[0])
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
                                        Toast.makeText(context, "Объявление помещено в черный список!", Toast.LENGTH_SHORT).show()
                                        setContext(context)
                                        removeAd(advert)
                                    },
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp),
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
                                        setContext(context)
                                    },
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp),
                                    content = {
                                        if(true){
                                        //todo if(advert.isGeoOn){
                                            Icon(
                                                Icons.Filled.LocationOn,
                                                modifier = Modifier
                                                    .height(24.dp)
                                                    .width(24.dp),
                                                contentDescription = "Localized description",
                                            )
                                        }else{
                                            Icon(
                                                painter = painterResource(R.drawable.ic_walk),
                                                modifier = Modifier
                                                    .height(24.dp)
                                                    .width(24.dp),
                                                contentDescription = "Localized description",
                                            )
                                        }
                                    })
                                IconButton(
                                    onClick = {
                                        Toast.makeText(context, "Объявление добавлено в избранное!", Toast.LENGTH_SHORT).show()
                                        setContext(context)
                                        selectAd(advert)
                                        addFavourites(advert)
                                    },
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp),
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
}