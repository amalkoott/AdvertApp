package ru.amalkoott.advtapp.ui.advert.screen

// отрисовка подборок

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.util.JsonToken
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import com.google.gson.annotations.Until
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.MainActivity
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.BlackList
import ru.amalkoott.advtapp.domain.Constants
import ru.amalkoott.advtapp.domain.getFromUrl
import ru.amalkoott.advtapp.domain.notification.sendBadNotification
import ru.amalkoott.advtapp.domain.notification.sendGeoNotification
import ru.amalkoott.advtapp.domain.notification.sendNotification
import ru.amalkoott.advtapp.domain.preferenceTools.AppPreferences
import ru.amalkoott.advtapp.ui.advert.compose.DropMenu
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.DrawFAB
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.DropUpMenu
import ru.amalkoott.advtapp.ui.advert.view.AppViewModel

//@TODO splash screen https://www.google.com/search?q=splash+screen+compose+android&oq=splash+screen+comp&gs_lcrp=EgZjaHJvbWUqDAgCEAAYFBiHAhiABDIHCAAQABiABDIGCAEQRRg5MgwIAhAAGBQYhwIYgAQyBwgDEAAYgAQyCAgEEAAYFhgeMggIBRAAGBYYHjIKCAYQABgPGBYYHjIKCAcQABgPGBYYHjIICAgQABgWGB4yCAgJEAAYFhge0gEIOTQxOWowajeoAgCwAgA&sourceid=chrome&ie=UTF-8#fpstate=ive&vld=cid:57e4cdbe,vid:VTRz-8DPowM,st:0

// вывод списка подборок (главный экран по сути)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun AdSetScreen(vm: AppViewModel, token: State<Boolean>) {
    val sets by vm.adSet.collectAsState()
    val selected by remember { mutableStateOf(vm.selectedSet) }

    val favs by vm.favList.collectAsState()
    val blackList by vm.blackLists.collectAsState()
    //...

    val roomsText by remember {
        mutableStateOf(vm.roomsText)
    }
    val search by remember { mutableStateOf(vm.searching) }
    val createSearching:() -> Unit = { vm.createSearching() }

    val setLivingType:(String?) -> Unit = { type -> vm.setLivingType(type)}
    val setDealType:(String?) -> Unit = { type -> vm.setDealType(type)}
    val setPriceType:(String?) -> Unit = { type -> vm.setPriceType(type)}
    val setRentType:(String?) -> Unit = { type -> vm.setRentType(type)}
    val setFloorType:(String?) -> Unit = { type -> vm.setFloorType(type)}
    val setMinPrice:(String?) -> Unit = { type -> vm.setMinPrice(type)}
    val setMaxPrice:(String?) -> Unit = { type -> vm.setMaxPrice(type)}
    val setMinArea:(String?)-> Unit = {type -> vm.setMinArea(type)}
    val setMaxArea:(String?) -> Unit = {type -> vm.setMaxArea(type)}
    val setMinLArea:(String?)-> Unit = {type -> vm.setMinLArea(type)}
    val setMaxLArea:(String?)-> Unit = {type -> vm.setMaxLArea(type)}
    val setMinKArea:(String?)-> Unit = {type -> vm.setMinKArea(type)}
    val setMaxKArea:(String?)-> Unit = {type -> vm.setMaxKArea(type)}
    val setMinFloor:(String?)-> Unit = {type -> vm.setMinFloor(type)}
    val setMaxFloor:(String?)-> Unit = {type -> vm.setMaxFloor(type)}
    val setMinFloors:(String?)-> Unit = {type -> vm.setMinFloors(type)}
    val setMaxFloors:(String?)-> Unit = {type -> vm.setMaxFloors(type)}
    val setRepair:(String?)-> Unit = {type -> vm.setRepair(type)}
    val setFinish:(String?)-> Unit = {type -> vm.setFinish(type)}
    val setTravelTime:(String?)-> Unit = {type -> vm.setTravelTime(type)}
    val setTravelType:(String?)-> Unit = {type -> vm.setTravelType(type)}
    val setCell:(String?)-> Unit = {type -> vm.setCell(type)}
    val setApart:(String?)-> Unit = {type -> vm.setApart(type)}
    val setRoomType:(String?)-> Unit = {type -> vm.setRoomType(type)}
    val setRoom:(String?)-> Unit = {type -> vm.setRoom(type)}
    val setCRoom:(String?) -> Unit = {type -> vm.setCRoom(type)}
    val setToilet:(String?)-> Unit = {type -> vm.setToiletType(type)}
    val setWall:(String?)-> Unit = {type -> vm.setWallMaterial(type)}
    val setBalcony:(String?)-> Unit = {type -> vm.setBalconyType(type)}
    val setParking:(String?)-> Unit = {type -> vm.setParking(type)}
    val setLift:(String?)-> Unit = {type -> vm.setLiftType(type)}
    val setAmenities:(String?)-> Unit = {type -> vm.setAmenities(type)}
    val setView:(String?)-> Unit = {type -> vm.setView(type)}
    val setCommunications:(String?)-> Unit = {type -> vm.setCommunication(type)}
    val setInclude:(String?)-> Unit = {type -> vm.setInclude(type)}
    val setExclude:(String?)-> Unit = {type -> vm.setExclude(type)}
    val setRentFeatures:(String?)-> Unit = {type -> vm.setRentFeatures(type)}
    val setCity:(String?)-> Unit = {city -> vm.setCity(city)}
    val setCategory:(String?)-> Unit = {category -> vm.setCategory(category)}
    //val ():-> Unit = {type -> vm}
    val filterFunctions = mapOf<String,(String?)->Unit>(
        "city" to setCity,
        "living" to setLivingType,
        "deal" to setDealType,
        "price" to setPriceType,
        "rent" to setRentType,
        "floor" to setFloorType,
        "minPrice" to setMinPrice,
        "maxPrice" to setMaxPrice,
        "minArea" to setMinArea,
        "maxArea" to setMaxArea,
        "minLArea" to setMinLArea,
        "maxLArea" to setMaxLArea,
        "minKArea" to setMinKArea,
        "maxKArea" to setMaxKArea,
        "minFloor" to setMinFloor,
        "maxFloor" to setMaxFloor,
        "minFloors" to setMinFloors,
        "maxFloors" to setMaxFloors,
        "repair" to setRepair,
        "finish" to setFinish,
        "travelTime" to setTravelTime,
        "travelType" to setTravelType,
        "room" to setRoom,
        "roomType" to setRoomType,
        "countryRoom" to setCRoom,
        "cell" to setCell,
        "apart" to setApart,
        "toilet" to setToilet,
        "wall" to setWall,
        "balcony" to setBalcony,
        "parking" to setParking,
        "lift" to setLift,
        "amenities" to setAmenities,
        "view" to setView,
        "communication" to setCommunications,
        "include" to setInclude,
        "exclude" to setExclude,
        "rentFeatures" to setRentFeatures,
        "adCategory" to setCategory
        //"" to set,
    )



    val selectedAd by remember { mutableStateOf(vm.selectedAd) }
    val category by remember { mutableStateOf(vm.category)    }
    val dealType by remember { mutableStateOf(vm.dealType) }
    val flatType by remember { mutableStateOf(vm.flatType) }
    val city by remember { mutableStateOf(vm.city) }
    val travel by remember { mutableStateOf(vm.travel)}

    val scope = rememberCoroutineScope()
    val setChange: suspend () -> Unit = { vm.onSetChange(selected.value!!.name!!,selected.value!!.update_interval!!)    }
    val favouritesClick:() -> Unit = {vm.onFavouritesClick()}
    val settingsClick:() -> Unit = {vm.onSettingsClick()}
    val notificationClick:() -> Unit = {vm.onPushesClick()}
    val howItWorkClick:() -> Unit ={vm.onHowItWorkClick()}
    val onBackClick:() -> Unit = {vm.onBackClick()}
    val onDeleteSet:() -> Unit = { vm.onDeleteSet()}
    val removeSelectedAd:() -> Unit = {vm.onRemoveAd()}
    val onUpdateSet: ()-> Unit ={vm.onUpdateSet()}
    val removeAd:(Advert) -> Unit = {
        advrt -> vm.onRemoveAd(advrt)
    }
    val selectSet: (AdSet) -> Unit = { set ->
        scope.launch {
            vm.onSetSelected(set)
        }
    }
    val selectAd: (Advert) -> Unit = {
        advert ->
        vm.onAdSelected(advert)
    }
    val addFavourites: (Advert) -> Unit = {
        advert ->
        vm.onFavouritesAdd(advert)
    }
    val deleteFavourites: (Advert) -> Unit = {
        advert -> vm.onDeleteFavourites(advert)
    }
    val removeFromBlackList: (BlackList) -> Unit = {
            advert -> vm.onRemoveFromBlackList(advert)
    }

    val adsCount: (Long) -> MutableStateFlow<Int> = {
            id -> vm.getAdsCountInSet(id)
    }

    val onBlackListClicked: () -> Unit = {vm.onBlackListClick() }


    val filterParameters = vm.parameters
    val favourites by remember { mutableStateOf(vm.favourites) }
    val settings by remember { mutableStateOf(vm.settings) }
    val loading by remember { mutableStateOf(vm.loading) }
    val successfulSearch by remember { mutableStateOf(vm.successfulSearch) }
    val onCancelSearch:() -> Unit = { vm.cancelSearching()}

    val context = LocalContext.current
    val setContext: (Context)-> Unit = {
        vm.setContextValue(it)
    }
    val screen_name by remember { mutableStateOf(vm.screen_name) }
    val screenState by remember { mutableStateOf(vm.screenState) }

    val setContextValue: (Context) -> Unit = {vm.setContextValue(it)}
    val onEditComplete:() -> Unit ={vm.onEditComplete()}
    val onAddSetClicked:() -> Unit ={vm.onAddSetClicked()}

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawSheet(scope,drawerState,favouritesClick, settingsClick, notificationClick, howItWorkClick)
        },
    ) {
        if (loading.value){
            LoadScreen(value = true, isSuccessful = successfulSearch, cancelSearch = onCancelSearch)
        }else{
            Scaffold(
                topBar = {
                    DrawTopBar(screen_name,selected,selectedAd,removeSelectedAd,onDeleteSet, onUpdateSet,favourites,settings, onBackClick ,scope, drawerState, setContext, screenState)
                },
                floatingActionButton = {
                    if((screenState.value == "main")||(screenState.value == "add")||(screenState.value == "sets"))
                        DrawFAB(selected,context,setContextValue,onEditComplete,onAddSetClicked)
                },
                bottomBar = {
                    if(selectedAd.value!= null){
                        // todo фикс url
                        val array = selectedAd.value!!.URLs
                        if(array != null) DropUpMenu(Constants.SITES.getFromUrl(array))
                    }
                }
            ) {
                if (selected.value == null) {
                    when(screenState.value){
                        "favourites" -> {
                            if(selectedAd.value == null){
                                PrintFavourites(favs, deleteFavourites, selectedAd, selectAd)
                            }else{
                                PrintAdvert(selectedAd)
                            }
                        }
                        "pushes" -> PrintPushes()
                        "settings" -> PrintSettings(onBlackListClicked, token)
                        "blackList" -> PrintBlackList(blackList, removeFromBlackList)
                        "how" -> PrintHowItWorkScreen()
                        "advert" -> PrintAdvert(selectedAd)
                       // "sets" ->PrintSet(sets, selected, selectSet, adsCount)
                        "main" -> PrintSet(sets, selected, selectSet, adsCount)
                        else -> {}
                    }
                } else {
                    // создаем новую подборку
                    val ads = vm.adsMap[selected.value!!.id]
                    AddSet(setChange,selected,selectAd,removeAd,selectedAd, addFavourites,ads,search,filterFunctions,createSearching,category,dealType,flatType,city,travel, filterParameters, setContext, roomsText)//vm.temp_ads)//, getAdverts)
                }
            } 
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PrintSet(sets: List<AdSet>,
             selected: MutableState<AdSet?>,
             selectSet: (AdSet)-> Unit,
             adsCount: (Long)-> MutableStateFlow<Int>
){
    val context = LocalContext.current
    var notify by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    if (sets.size == 0){

        Column(Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "Создайте подборку, нажав на \"+\"",
                color = MaterialTheme.colorScheme.outline,
                fontSize = 16.sp)
        }

    }else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .width(600.dp)
                .height(1300.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 64.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
        ){
            /*
            item(){

                Button(onClick = { sendNotification(context,"Найдены новые объявления!","Подборка Sale flat обновилась! Нажмите, чтобы посмотреть...") }) {

                }
            }
            item(){

                Button(onClick = { sendBadNotification(context) }) {

                }
            }
            item(){

                Button(onClick = { sendGeoNotification(context) }) {

                }
            }
            */
            items(sets){
                    set ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp)
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .clickable {
                            // появляется выбранная подборка, клик - вывод списка объявлений подборки
                            selectSet(set)
                        }
                    ,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = set.name!!,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 24.dp),
                                color = MaterialTheme.colorScheme.onSurface)
                            SuggestionChip(
                                modifier = Modifier.padding(end = 8.dp),
                                border = BorderStroke(
                                    color = MaterialTheme.colorScheme.tertiaryContainer, // Укажите желаемый цвет
                                    width = 1.dp // Укажите толщину рамки
                                ),
                                onClick = { //Log.d(set.update_interval.toString(), "hello world")
                                          },
                                label = {
                                    Text(getTime(set.update_interval), color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.SemiBold)},//MaterialTheme.colorScheme.surfaceVariant) },
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .height(48.dp)
                                .padding(start = 24.dp)
                        ){
                            val count = adsCount(set.id!!).collectAsState().value
                            Text(text = "Объявлений: " + count/* + set.adverts!!.count().toString() */, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "Обновлено: " + set.last_update.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
private fun getTime(interval: Int?):String{
    return try {
        if (interval!!/60 < 1) "$interval мин." else "${interval/60} час."
    } catch (e: Exception){
        "???"
    }
}

@Composable
fun DrawSheet(
    scope: CoroutineScope,
    drawerState: DrawerState,
    favouritesClick: () -> Unit,
    settingsClick: () -> Unit,
    notificationClick: () -> Unit,
    howItWorkClick: () -> Unit,
) {
    // todo отключить боковое меню на всех экранах, кроме главного
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Text("AdSpider", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favourites", tint = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "Избранное", modifier = Modifier.padding(start = 8.dp), color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.apply { if (isOpen) close() else open() }
                }
                favouritesClick()
            })
        /*
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Push", tint = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "Уведомления", modifier = Modifier.padding(start = 8.dp), color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.apply { if (isOpen) close() else open() }
                }
                notificationClick()
            })
        */
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, contentDescription = "How it work", tint = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "Как это работает?", modifier = Modifier.padding(start = 8.dp), color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.apply { if (isOpen) close() else open() }
                }
                howItWorkClick()
            })
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "Настройки", modifier = Modifier.padding(start = 8.dp), color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.apply { if (isOpen) close() else open() }
                }
                settingsClick()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawTopBar(
    screen_name: MutableState<String>,
    selected: MutableState<AdSet?>,
    selectedAd: MutableState<Advert?>,
    removeSelectedAd: () -> Unit,
    onDeleteSet: () -> Unit,
    onUpdateSet: () -> Unit,
    favourites: MutableState<Boolean>,
    settings: MutableState<Boolean>,
    onBackClick: () -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState,
    setContext: (Context)-> Unit,
    screenState: MutableState<String>
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,//primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        title = {
            Text(
                text = screen_name.value,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

        },
        actions = {
            if (selected.value != null) {
                if (selectedAd.value != null) {
                    DropMenu(removeSelectedAd)
                } else {
                    if (selected.value!!.name != "") {
                        DropMenu(onDeleteSet, onUpdateSet, setContext)
                    }
                }
            }
        },

        navigationIcon = {
            when(screenState.value){
                "main" -> {
                    DrawNavigationMenu(scope, drawerState)
                }
                else -> DrawBackButton(onBackClick)
            }
        }

    )
}

@Composable
fun DrawBackButton(onBackClick: () -> Unit,){
    IconButton(onClick = {
        onBackClick()
    }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Localized description",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
@Composable
fun DrawNavigationMenu(scope: CoroutineScope, drawerState: DrawerState){
    IconButton(
        content = {
            Icon(Icons.Filled.Menu, contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimaryContainer)
        },
        onClick = {
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        })
}


