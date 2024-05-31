package ru.amalkoott.advtapp.ui.advert.screen

import android.content.Context
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.MutableStateFlow
import ru.amalkoott.advtapp.R
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.ui.advert.compose.DropdownFilter
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.GeneralSetInfo
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.SetInfo
import ru.amalkoott.advtapp.ui.advert.screen.filterScreen.RealEstateFilter
import ru.amalkoott.advtapp.ui.advert.screen.filterScreen.ServiceFilter
import ru.amalkoott.advtapp.ui.advert.screen.filterScreen.TransportFilter

// отрисовка выбранной подборки

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSet(
    setChange: suspend () -> Unit,
    selected: MutableState<AdSet?>,
    selectAd: (Advert)-> Unit,
    removeAd: (Advert)-> Unit,
    selectedAd: MutableState<Advert?>,
    addFavourites: (Advert)-> Unit,
    ads: MutableStateFlow<List<Advert>>?,
    search: MutableState<RealEstateSearchParameters?>,
    filterFunctions: Map<String,(String?)->Unit>,
    createSearching:()->Unit,
    category: MutableState<String?>,
    dealType: MutableState<Boolean>,
    flatType: MutableState<String>,
    city: MutableState<String>,
    travel:MutableState<String?>,
    parameters: Map<String, Map<String, SnapshotStateMap<String, Boolean>>>,
    setContext: (Context) -> Unit,
){
    /*
    todo beauty drop down https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose
    */
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
                PrintFilters(search,filterFunctions,createSearching,category,dealType,flatType,city,travel, parameters)
            }else{
                SetInfo(ads,selectAd, removeAd,addFavourites, setContext)
            }

        }
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageFromUrl(url: String) {
    val context = LocalContext.current
    AsyncImage(
        model = /*Loader(context,url)*/ImageRequest.Builder(context = LocalContext.current)
            .data(url)
            .crossfade(true)
            .build()
        ,
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth(),
        onError = {
           // Log.d("ERROR",it.toString())
        }
    )
}

@Composable
fun PrintFilters(search: MutableState<RealEstateSearchParameters?>,
                 realEstateFunctions: Map<String,(String?)->Unit>,
                 createSearching:()->Unit,
                 ctgry: MutableState<String?>,
                 dealType: MutableState<Boolean>,
                 flatType: MutableState<String>,
                 city: MutableState<String>,
                 travel: MutableState<String?>,
                 parameters: Map<String, Map<String, SnapshotStateMap<String, Boolean>>>
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
              //  .background(MaterialTheme.colorScheme.background)
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
                            RealEstateFilter(realEstateFunctions,dealType,flatType,city,travel, parameters["realEstate"]!!)
                        }
                        "Транспорт" -> TransportFilter()
                        "Услуги"-> ServiceFilter()
                        else -> {}//Log.d("me","me")
                    }
                }
            }
        }
    }
}
