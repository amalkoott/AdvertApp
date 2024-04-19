package ru.amalkoott.advtapp.ui.advert.screen

// отрисовка подборок

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.ui.advert.view.AppViewModel
import ru.amalkoott.advtapp.ui.theme.AdvtAppTheme
import java.time.LocalDate

//@TODO splash screen https://www.google.com/search?q=splash+screen+compose+android&oq=splash+screen+comp&gs_lcrp=EgZjaHJvbWUqDAgCEAAYFBiHAhiABDIHCAAQABiABDIGCAEQRRg5MgwIAhAAGBQYhwIYgAQyBwgDEAAYgAQyCAgEEAAYFhgeMggIBRAAGBYYHjIKCAYQABgPGBYYHjIKCAcQABgPGBYYHjIICAgQABgWGB4yCAgJEAAYFhge0gEIOTQxOWowajeoAgCwAgA&sourceid=chrome&ie=UTF-8#fpstate=ive&vld=cid:57e4cdbe,vid:VTRz-8DPowM,st:0

// вывод списка подборок (главный экран по сути)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdSetScreen(vm: AppViewModel) {
    val sets by vm.adSet.collectAsState()
    val favs by remember {
        mutableStateOf(vm.favs)
    }


    val selected by remember { mutableStateOf(vm.selectedSet) }
    val selectedAd by remember { mutableStateOf(vm.selectedAd) }

    val setChange:() -> Unit = {vm.onSetChange(selected.value!!.name!!,selected.value!!.update_interval!!)}
    val favouritesClick:() -> Unit = {vm.onFavouritesClick()}
    val settingsClick:() -> Unit = {vm.onFavouritesClick()}
    val onBackClick:() -> Unit = {vm.onBackClick()}
    val onDeleteSet:() -> Unit = { vm.onDeleteSet()}
    val removeSelectedAd:() -> Unit = {vm.onRemoveAd()}
    val removeAd:(Advert) -> Unit = {
        advrt -> vm.onRemoveAd(advrt)
    }
    val selectSet: (AdSet) -> Unit = { set ->
        vm.onSetSelected(set)
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

    val favourites by remember { mutableStateOf(vm.favourites) }
    val settings by remember { mutableStateOf(vm.settings) }

    val screen_name by remember { mutableStateOf(vm.screen_name) }

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawSheet(scope,drawerState,favouritesClick, settingsClick)
        },
    ) {
        Scaffold(
            topBar = {
                DrawTopBar(screen_name,selected,selectedAd,removeSelectedAd,onDeleteSet,favourites,settings, onBackClick ,scope, drawerState)
            },
            floatingActionButton = {
                if(!favourites.value && !settings.value && selectedAd.value == null){
                    FloatingActionButton(
                        onClick = {
                            if(selected.value != null ){
                                // сохраняем
                                vm.onEditComplete()
                            }else{
                                // Добавляем
                                vm.onAddSetClicked()
                            }

                        },
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(defaultElevation = 8.dp),
                        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
                        content = {
                            if(selected.value != null) Icon(Icons.Filled.Done, contentDescription = "Сохранить")
                            else Icon(Icons.Filled.Add, contentDescription = "Добавить") }
                    )
                }
            }

        ) {
            if (selected.value == null) {
                if (favourites.value) {
                    if(selectedAd.value == null){
                        PrintFavourites(favs, deleteFavourites, selectedAd, selectAd)
                    }else{
                        PrintAdvert(selectedAd)
                    }
                } else {
                    if (settings.value) {
                        PrintSettings()
                    } else
                        PrintSet(sets, selected,selectSet)
                }
            } else {
                // создаем новую подборку
                val ads = vm.adsMap[selected.value!!.id]
                AddSet(setChange,selected,selectAd,removeAd,selectedAd, addFavourites,ads)//vm.temp_ads)//, getAdverts)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PrintSet(sets: List<AdSet>,
             selected: MutableState<AdSet?>,
             selectSet: (AdSet)-> Unit){
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .width(600.dp)
            .height(1300.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 64.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
    ){
        items(sets){
                set ->
            Card(
                colors = CardDefaults.cardColors(
                    //surfaceColorAtElevation(Color.White),
                    containerColor = MaterialTheme.colorScheme.surface
                    //containerColor = Color.White
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
                    defaultElevation = 4.dp
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
                            modifier = Modifier.padding(start = 24.dp))
                        //Text(text = "5", fontSize = 20.sp)
                        SuggestionChip(
                            modifier = Modifier.padding(end = 8.dp),
                            border = SuggestionChipDefaults.suggestionChipBorder(MaterialTheme.colorScheme.secondaryContainer),
                            onClick = { Log.d(set.update_interval.toString(), "hello world") },
                            label = {
                                Text(set.update_interval.toString() + " час.", color = MaterialTheme.colorScheme.secondaryContainer, fontWeight = FontWeight.SemiBold)},//MaterialTheme.colorScheme.surfaceVariant) },
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(48.dp)
                            .padding(start = 24.dp)
                    ){
                        Text(text = "Объявлений: "+ set.adverts!!.count().toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Обновлено: " + set.last_update.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawSheet(scope: CoroutineScope,
              drawerState: DrawerState,
              favouritesClick: () -> Unit,
              settingsClick: () -> Unit){
    ModalDrawerSheet {
        Text("Advert App", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(Icons.Filled.Favorite, contentDescription = "Favourites",)
                    Text(text = "Избранное", modifier = Modifier.padding(start = 8.dp))}
            }
            ,
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.apply { if (isOpen) close() else open() }
                }
                favouritesClick()
             //  vm.onFavouritesClick()
            })
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(Icons.Filled.Settings, contentDescription = "Settings",)
                    Text(text = "Настройки", modifier = Modifier.padding(start = 8.dp))}
            }
            ,
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.apply { if (isOpen) close() else open() }
                }
                settingsClick()
            //    vm.onSettingsClick()
            }
        )

        // ...другие элементы ящика
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawTopBar(screen_name: MutableState<String>,
               selected: MutableState<AdSet?>,
               selectedAd: MutableState<Advert?>,
               removeSelectedAd: () -> Unit,
               onDeleteSet: () -> Unit,
               favourites: MutableState<Boolean>,
               settings: MutableState<Boolean>,
               onBackClick: () -> Unit,
               scope: CoroutineScope,
               drawerState: DrawerState,
               ){
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            Text(text = screen_name.value,
                color = MaterialTheme.colorScheme.onPrimary)

        },

        actions = {
            if (selected.value != null){
                if (selectedAd.value != null){
                    DrawDropmenu(removeSelectedAd)
                }else{
                    if (selected.value!!.name != ""){
                        DrawDropmenu(onDeleteSet)
                    }
                }
            }
        },

        navigationIcon = {
            if( favourites.value || settings.value || selectedAd.value != null || selected.value != null){
                IconButton(onClick = {
                    //vm.onBackClick()
                    onBackClick()
                    if(favourites.value){
                    }else if (settings.value){
                    }
                    else{
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }else{/*
                            ExtendedFloatingActionButton(
                                text = { Text("Show drawer") },
                                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            )*/
                IconButton(
                    content = {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    },
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    })
            }

        }

    )
}

@Composable
fun DrawDropmenu(onDeleteSet: ()-> Unit){
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(onClick = {
        coroutineScope.launch {
            expanded = !expanded
        }
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Localized description"
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Удалить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить асинхронную операцию, например, вызов удаления
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                    onDeleteSet()
                }
            }
        )
        DropdownMenuItem(
            text = { Text("Обновить") },
            onClick = {
                coroutineScope.launch {
                    // Здесь можно выполнить другую асинхронную операцию, например, обновление данных
                    Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "InvalidColorHexValue")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdvtAppTheme {

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        //containerColor = MaterialTheme.colorScheme.primaryContainer,
                        //titleContentColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    title = {
                        Text(
                            text = "Подборки"
                        )

                    },
                    actions = {
//                        DrawDropmenu(U)
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description",
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                        )

                    }
                )
            },
            /*
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth().height(intrinsicSize = IntrinsicSize.Max),
                    actions = {

                        IconButton(
                            onClick = {
                                // settings = !settings
                                //vm.onSettingsClick()
                                //PrintSettings()
                            },
                            modifier = Modifier.width(80.dp),
                            content = {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = "Localized description",
                                )
                            })

                        IconButton(
                            onClick = {
                                //favourites = !favourites
                                //vm.onFavouritesClick()
                                //PrintFavourites()
                            },
                            modifier = Modifier.width(80.dp),
                            content = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            }
                        )

                    },


                )
            },
*/
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                    },
                    containerColor = Color(0xff84ffdb),
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(defaultElevation = 8.dp),
                    shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
                    content = {
                        Icon(Icons.Filled.Add, contentDescription = "Добавить")
                    }

                )
            }
            ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .width(600.dp)
                    .height(1300.dp)
                    // .background(MaterialTheme.colorScheme.background)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 64.dp)
                    .padding(8.dp)

            ) {
                items(3) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                            //containerColor = MaterialTheme.colorScheme.surface,
                           // Color(MaterialTheme.colorScheme.surface)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(176.dp)
                            .padding(vertical = 16.dp, horizontal = 16.dp)
                            .clickable {
                                // появляется выбранная подборка, клик - вывод списка объявлений подборки
                                //selected.value = set
                                // selectSet(set)
                                //navController.navigate("set")
                                //addSet.value = !addSet.value
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        //onClick = {navController.navigate("set")}
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
                                Text(text = "Название подборки",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    modifier = Modifier.padding(start = 24.dp))
                                //Text(text = "5", fontSize = 20.sp)
                                SuggestionChip(
                                    //colors = SuggestionChipDefaults.suggestionChipColors(Color(0xffffdc5f)),//MaterialTheme.colorScheme.tertiary),
                                    modifier = Modifier.padding(end = 8.dp),
                                    border = SuggestionChipDefaults.suggestionChipBorder(Color(0xffe7cc85)),
                                    onClick = { Log.d("set.update_interval.toString()", "hello world") },
                                    label = {
                                        Text("5" + " час.", color = Color(0xffe7cc85), fontWeight = FontWeight.SemiBold)},//MaterialTheme.colorScheme.surfaceVariant) },
                                )
                                /*
                                AssistChip(
                                    onClick = { Log.d("5 часов", "hello world") },
                                    label = { Text("5 часов") },
                                )*/

                            }
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    //.padding(horizontal = 2.dp)
                                    .height(48.dp)
                                    .padding(start = 24.dp)
                            ){
                                Text(text = "Объявлений: 6", color = Color(0xff666666))
                                Text(text = "Обновлено: " + LocalDate.now().toString(), color = Color(0xff666666))
                            }
                        }
                    }

                }
            }
        }


    }
}



