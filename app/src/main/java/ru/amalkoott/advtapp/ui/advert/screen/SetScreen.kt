package ru.amalkoott.advtapp.ui.advert.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
import ru.amalkoott.advtapp.data.remote.SearchParameters
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.ui.advert.compose.DropdownFilter
import ru.amalkoott.advtapp.ui.advert.compose.RangeSliderFilter
import ru.amalkoott.advtapp.ui.advert.compose.TextFilter
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.GeneralSetFilters
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.GeneralSetInfo
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.SetInfo
import ru.amalkoott.advtapp.ui.advert.screen.filterScreen.RealEstateFilter
import ru.amalkoott.advtapp.ui.advert.screen.filterScreen.ServiceFilter
import ru.amalkoott.advtapp.ui.advert.screen.filterScreen.TransportFilter

// отрисовка выбранной подборки

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSet(
    setChange: ()-> Unit,
    selected: MutableState<AdSet?>,
    selectAd: (Advert)-> Unit,
    removeAd: (Advert)-> Unit,
    selectedAd: MutableState<Advert?>,
    addFavourites: (Advert)-> Unit,
    ads: MutableStateFlow<List<Advert>>?,
    search: MutableState<SearchParameters?>,
    filterFunctions: Map<String,(String)->Unit>,
    createSearching:()->Unit,
    category: MutableState<String?>,
    dealType: MutableState<Boolean>,
    flatType: MutableState<String>,
    city: MutableState<String>,
    travel:MutableState<String?>
    //getAdverts:()->Unit
    //adSetAdverts: MutableStateFlow<List<Advert>>
){

    val scrollState = rememberScrollState(0)
    var update_interval by remember { mutableStateOf(selected.value!!.update_interval) }
    var name by remember { mutableStateOf(selected.value!!.name) }
        // val adverts = selected.value!!.adverts

    //val test_adverts = getAdverts // каждый раз (при открытии окошка) adverts строится новый
    // -> стоит в SetScreen.adverts возварщать MutableStateFlow объект
    //val adverts by adSetAdverts.collectAsState()
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
  /*
            Column{
                TextField(
                    value = name!!,
                    onValueChange = { name = it
                        selected.value!!.name = it
                        setChange() },
                    label = { Text("Название подборки",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text(
                        text = "Введите название",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },

                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface),

                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            bottom = 16.dp,
                            end = 16.dp
                        )
                        .fillMaxWidth()
                )
                TextField(
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
                    label = { Text("Интервал обновления",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text(
                        text = "Введите значение",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        )
                        .fillMaxWidth()
                )
            }
*/
            // если новая подборка, рисуем фильтры
            if (selected.value!!.adverts!!.isEmpty()){
                PrintFilters(search,filterFunctions,createSearching,category,dealType,flatType,city,travel)
            }else{
                SetInfo(ads,selectAd, removeAd,addFavourites)
                /*
                // иначе рисуем список подборок
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
                                            text = (advert.price!! * 100000).toString() + ' ' + '₽',
                                            fontSize = 18.sp,
                                            modifier = Modifier.padding(top = 0.dp, bottom = 0.dp),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Text(
                                        text = advert.ad_caption.toString(),
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
                                                Icon(
                                                    Icons.Filled.FavoriteBorder,
                                                    modifier = Modifier
                                                        .height(24.dp)
                                                        .width(24.dp),
                                                    contentDescription = "Localized description",
                                                )
                                            })
                                    }
                                }
                            }
                        }
                    }
                }
                */
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
fun PrintFilters(search: MutableState<SearchParameters?>,
                 realEstateFunctions: Map<String,(String)->Unit>,
                 createSearching:()->Unit,
                 ctgry: MutableState<String?>,
                 dealType: MutableState<Boolean>,
                 flatType: MutableState<String>,
                 city: MutableState<String>,
                 travel: MutableState<String?>
){
    createSearching()
    val category by remember { mutableStateOf(ctgry) }
    LazyVerticalGrid(columns = GridCells.Fixed(1),
        Modifier
            .fillMaxHeight()
            .padding(top = 24.dp)
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)){
        item{
            Text(text = "Фильтры:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(start = 24.dp))
        }
        item(1){
            Column(modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                )) {
                DropdownFilter(arrayOf("Недвижимость", "Транспорт", "Услуги"), "Категория",realEstateFunctions["adCategory"]!!)
                /*
                RangeSliderFilter("Цена",0f,100f,realEstateFunctions["minPrice"]!!,realEstateFunctions["maxPrice"]!!)
                TextFilter("Санкт-Петербург", "Город", "Название города",realEstateFunctions["city"]!!)
*/
                Log.d("MEEEEEEEEEEEEEEEEEEE",search.value!!.toString())

                when(category.value){
                    "Недвижимость" -> {
                       // GeneralSetFilters(functions = realEstateFunctions)
                        RealEstateFilter(realEstateFunctions,dealType,flatType,city,travel)
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
