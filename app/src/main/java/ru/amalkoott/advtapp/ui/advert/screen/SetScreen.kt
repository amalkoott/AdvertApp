package ru.amalkoott.advtapp.ui.advert.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
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
    val scope = rememberCoroutineScope()
    val setName = remember { mutableStateOf(selected.value?.name) }
    val updateInterval = remember { mutableStateOf(selected.value?.update_interval) }
    var isAdListEmpty = true
    /*
    todo beauty drop down https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose
    */
    Column(modifier = Modifier
        .background(Color.White)
        // .padding(top = 64.dp, start = 8.dp/*  , end = 8.dp */, bottom = 8.dp)
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
            // if (isAdListEmpty){
                PrintFilters(search,filterFunctions,createSearching,category,dealType,flatType,city,travel, parameters)
            }else{
                SetInfo(ads,selectAd, removeAd,addFavourites, setContext)
            }

        }
    }
}
@Composable
fun ImageFromUrl(url: String) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null, // Описание содержания, можно оставить null, если изображение не содержит содержательной информации
        modifier = Modifier.fillMaxSize(), // Размер изображения
        contentScale = ContentScale.Crop, // Масштабирование изображения
        //backgroundColor = Color.LightGray // Цвет фона, который отображается, пока изображение загружается
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
        Box(modifier = Modifier.fillMaxWidth().height(20.dp).background(g).zIndex(1f))
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
                   // .padding(top = 16.dp, start = 0.dp, end = 0.dp, bottom = 16.dp,)
                ) {
                    DropdownFilter(arrayOf("Недвижимость", "Транспорт", "Услуги"), "Категория",realEstateFunctions["adCategory"]!!)
                    /*
                    RangeSliderFilter("Цена",0f,100f,realEstateFunctions["minPrice"]!!,realEstateFunctions["maxPrice"]!!)
                    TextFilter("Санкт-Петербург", "Город", "Название города",realEstateFunctions["city"]!!)
    */
                    Log.d("MEEEEEEEEEEEEEEEEEEE",search.value!!.toString())

                    when(category.value){
                        "Недвижимость" -> {
                            // GeneralSetFilters(functions = realEstateFunctions)
                            RealEstateFilter(realEstateFunctions,dealType,flatType,city,travel, parameters["realEstate"]!!)
                        }
                        "Транспорт" -> TransportFilter()
                        "Услуги"-> ServiceFilter()
                        else -> Log.d("me","me")
                    }
                    /*
                                    // фильтры RealEstate

                                    // добавить слова
                                    TextFilter(value = "", name = "Добавить слова", placeholder = "Что-то важное для вас",realEstateFunctions["include"]!!)
                                    // исключить слова
                                    TextFilter(value = "", name = "Исключить слова", placeholder = "То, что вам не нужно",realEstateFunctions["exclude"]!!)
                    */
                }

            }
        }
    }

}
