package ru.amalkoott.advtapp.ui.advert.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.MutableStateFlow
import ru.amalkoott.advtapp.R
import ru.amalkoott.advtapp.domain.entities.AdSet
import ru.amalkoott.advtapp.domain.entities.Advert
import ru.amalkoott.advtapp.ui.advert.compose.elements.DropdownFilter
import ru.amalkoott.advtapp.ui.advert.compose.screens.GeneralSetInfo
import ru.amalkoott.advtapp.ui.advert.compose.screens.SetInfo
import ru.amalkoott.advtapp.ui.advert.screen.filter.RealEstateFilter
import ru.amalkoott.advtapp.ui.advert.screen.filter.ServiceFilter
import ru.amalkoott.advtapp.ui.advert.screen.filter.TransportFilter

// отрисовка выбранной подборки

@Composable
fun AddSet(
    setChange: suspend () -> Unit,
    selected: MutableState<AdSet?>,
    selectAd: (Advert)-> Unit,
    removeAd: (Advert)-> Unit,
    selectedAd: MutableState<Advert?>,
    addFavourites: (Advert)-> Unit,
    ads: MutableStateFlow<List<Advert>>?,
    filterFunctions: Map<String,(String?)->Unit>,
    createSearching:()->Unit,
    category: MutableState<String?>,
    dealType: MutableState<Boolean>,
    flatType: MutableState<String>,
    city: MutableState<String>,
    travel:MutableState<String?>,
    parameters: Map<String, Map<String, SnapshotStateMap<String, Boolean>>>,
    setContext: (Context) -> Unit,
    roomsText:MutableState<String>
){
    // beauty drop down https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose

    Column(modifier = Modifier
        .background(Color.White)
        .padding(top = 60.dp)
        .fillMaxWidth())
    {
        if(selectedAd.value != null){
            // рисуем объявление
            PrintAdvert(selectedAd)
        }else
        {
            // рисуем подборку
            GeneralSetInfo(selected,setChange)

            // если новая подборка, рисуем фильтры
            if (selected.value!!.adverts!!.isEmpty()){
                PrintFilters(filterFunctions,createSearching,category,dealType,flatType,city,travel, parameters, roomsText)
            }else{
                SetInfo(ads,selectAd, removeAd,addFavourites, setContext)
            }

        }
    }
}
@Composable
fun ImageFromUrl(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(url)
            .crossfade(true)
            .build()
        ,
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth(),
        onError = { }
    )
}

@Composable
fun PrintFilters(realEstateFunctions: Map<String,(String?)->Unit>,
                 createSearching:()->Unit,
                 ctgry: MutableState<String?>,
                 dealType: MutableState<Boolean>,
                 flatType: MutableState<String>,
                 city: MutableState<String>,
                 travel: MutableState<String?>,
                 parameters: Map<String, Map<String, SnapshotStateMap<String, Boolean>>>,
                 roomsText: MutableState<String>
                 ){
    createSearching()
    val category by remember { mutableStateOf(ctgry) }
    Box(Modifier
        .background(MaterialTheme.colorScheme.background)) {
        val g = Brush.verticalGradient(
            colors = listOf(
                Color.Black.copy(alpha = 0.1f),
                Color.Transparent,
            )
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(g)
            .zIndex(1f))
        LazyVerticalGrid(columns = GridCells.Fixed(1),
            Modifier
                .fillMaxHeight()
                .padding(start = 32.dp, end = 32.dp)
        ){
            item { Spacer(Modifier.padding(16.dp)) }
            item{
                Text(text = "Фильтры:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 0.dp))
            }
            item(1){
                Column(modifier = Modifier
                ) {
                    DropdownFilter(arrayOf("Недвижимость", "Транспорт", "Услуги"), "Категория",realEstateFunctions["adCategory"]!!)

                    when(category.value){
                        "Недвижимость" -> {
                            RealEstateFilter(realEstateFunctions,dealType,flatType,city,travel, parameters["realEstate"]!!,  roomsText)
                        }
                        "Транспорт" -> TransportFilter()
                        "Услуги"-> ServiceFilter()
                        else -> { }
                    }
                }
            }
        }
    }
}
