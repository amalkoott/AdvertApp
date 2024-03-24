package ru.amalkoott.advtapp.ui.advert.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advrt

// отрисовка выбранной подборки

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSet(
    setChange: ()-> Unit,
    selected: MutableState<AdSet?>,
    selectAd: (Advrt)-> Unit,
    removeAd: (Advrt)-> Unit,
    selectedAd: MutableState<Advrt?>,
    addFavourites: (Advrt)-> Unit){
    val context = LocalContext.current

    var update_interval by remember { mutableStateOf(selected.value!!.update_interval) }
    var name by remember { mutableStateOf(selected.value!!.name) }
    val adverts = selected.value!!.adverts

    Box(modifier = Modifier
        .padding(top = 60.dp)
        .fillMaxSize())
    {
        if(selectedAd.value != null){
            PrintAdvert(selectedAd)
        }else
        {
            TextField(
                value = name,
                placeholder = { Text(text = "test",  color = MaterialTheme.colorScheme.onPrimaryContainer) },
                onValueChange = {
                    name = it
                    selected.value!!.name = it
                    setChange()
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary),
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),
            )
            OutlinedTextField(
                value = update_interval.toString(),
                onValueChange = {
                    try{
                        update_interval = it.toInt()
                        selected.value!!.update_interval = it.toInt()
                    }catch (e: NumberFormatException){
                        // если интервал не указан, то идет значение по умолчанию
                        update_interval = 10
                        selected.value!!.update_interval = 10
                    }
                    setChange()
                },
                placeholder = { Text(
                    text = "Интервал обновления",
                    color = MaterialTheme.colorScheme.onBackground)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .padding(
                        top = 70.dp,
                        bottom = 10.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
                    .fillMaxWidth()
            )
            if (selected.value!!.adverts.isEmpty()){
                PrintFilters()
            }else{
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 130.dp)
                        .padding(6.dp)
                        .background(MaterialTheme.colorScheme.background)){
                    items(adverts){advert ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 8.dp)
                                .clickable {
                                    // появляется выбранная подборка, клик - вывод списка объявлений подборки
                                    //selectedAd.value = advert
                                    selectAd(advert)
                                    //selected.value = set
                                },
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ){
                            Text(text = advert.name)
                            Text(text = advert.ad_caption)
                            Text(text = advert.price.toString())
                            Row(
                                modifier = Modifier
                                    .padding(end = 100.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ){
                                IconButton(
                                    onClick = {
                                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                                        //selectedAd.value = advert
                                        //selectAd(advert)
                                        removeAd(advert)
                                    },
                                    modifier = Modifier.width(80.dp),
                                    content = {
                                        Icon(
                                            Icons.Filled.Delete,
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
                                    modifier = Modifier.width(80.dp),
                                    content = {
                                        Icon(
                                            Icons.Filled.FavoriteBorder,
                                            contentDescription = "Localized description",
                                        )
                                    })
                                IconButton(
                                    onClick = {
                                        Toast.makeText(context, "Location", Toast.LENGTH_SHORT).show()
                                        //   vm.onSettingsClick()
                                    },
                                    modifier = Modifier.width(80.dp),
                                    content = {
                                        Icon(
                                            Icons.Filled.LocationOn,
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
}

@Composable
fun PrintFilters(){
    LazyVerticalGrid(columns = GridCells.Fixed(1),
        Modifier.fillMaxHeight()
            .padding(top = 130.dp)
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)){
        item(1){
            Text(text = "filters")
        }
    }
}
