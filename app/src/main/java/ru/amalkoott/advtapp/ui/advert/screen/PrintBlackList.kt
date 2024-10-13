package ru.amalkoott.advtapp.ui.advert.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import ru.amalkoott.advtapp.domain.entities.BlackList

@Composable
fun PrintBlackList(items: List<BlackList>, removeFromBlackList:(BlackList) -> Unit){
    Column(Modifier.padding(top = 68.dp)) {
        if (items.size == 0){
            Column(Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "В черном списке нет объявлений",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 16.sp)
            }
        }else
        {
            LazyVerticalGrid(columns = GridCells.Fixed(1),
                Modifier
                    .fillMaxHeight()
                    .padding(start = 32.dp, end = 32.dp)
            ){
                item { Text(text = "Объявлений: ${items.size}") }
                items(items){advert ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(224.dp)
                            .padding(vertical = 12.dp)
                            .clickable {
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
                                Card(modifier = Modifier
                                    .fillMaxSize()
                                    .weight(5f),
                                    shape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp,),) {
                                    val images = advert.images
                                    if(images != null){
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
                                Column(modifier = Modifier
                                    .weight(3f)
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start) {
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
                                    removeFromBlackList(advert)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
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
}