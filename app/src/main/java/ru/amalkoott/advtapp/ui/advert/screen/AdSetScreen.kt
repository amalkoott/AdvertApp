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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advrt
import ru.amalkoott.advtapp.ui.advert.view.AdvrtViewModel
import ru.amalkoott.advtapp.ui.theme.AdvtAppTheme
import java.time.LocalDate

//@TODO splash screen https://www.google.com/search?q=splash+screen+compose+android&oq=splash+screen+comp&gs_lcrp=EgZjaHJvbWUqDAgCEAAYFBiHAhiABDIHCAAQABiABDIGCAEQRRg5MgwIAhAAGBQYhwIYgAQyBwgDEAAYgAQyCAgEEAAYFhgeMggIBRAAGBYYHjIKCAYQABgPGBYYHjIKCAcQABgPGBYYHjIICAgQABgWGB4yCAgJEAAYFhge0gEIOTQxOWowajeoAgCwAgA&sourceid=chrome&ie=UTF-8#fpstate=ive&vld=cid:57e4cdbe,vid:VTRz-8DPowM,st:0

// вывод списка подборок (главный экран по сути)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdSetScreen(vm: AdvrtViewModel) {
    val sets = vm.sets
    val favs by remember {
        mutableStateOf(vm.favs)
    }
    val selected by remember { mutableStateOf(vm.selectedSet) }
    val selectedAd by remember { mutableStateOf(vm.selectedAd) }

    val setChange:() -> Unit = {vm.onSetChange(selected.value!!.name,selected.value!!.update_interval)}
    val onDeleteSet:() -> Unit = { vm.onDeleteSet()}
    val removeSelectedAd:() -> Unit = {vm.onRemoveAd()}
    val removeAd:(Advrt) -> Unit = {
        advrt -> vm.onRemoveAd(advrt)
    }
    //val selectSet:(AdSet) -> Unit = {vm.onSetSelected(selected.value!!)}
    val selectSet: (AdSet) -> Unit = { set ->
        vm.onSetSelected(set)
    }
    //val selectAd:(Advrt) -> Unit = {vm.onAdSelected(selectedAd.value!!)}
    val selectAd: (Advrt) -> Unit = {
        advert ->
        vm.onAdSelected(advert)
    }
    //val addFavourites:(Advrt) -> Unit = {vm.onFavouritesAdd(selectedAd.value!!)}
    val addFavourites: (Advrt) -> Unit = {
        advert ->
        vm.onFavouritesAdd(advert)
    }
    //val deleteFavourites:(Advrt) -> Unit = {vm.onDeleteFavourites(selectedAd.value!!)}
    val deleteFavourites: (Advrt) -> Unit = {
        advert -> vm.onDeleteFavourites(advert)
    }

    val favourites by remember { mutableStateOf(vm.favourites) }
    val settings by remember { mutableStateOf(vm.settings) }

    val screen_name by remember { mutableStateOf(vm.screen_name) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = screen_name.value,
                        color = MaterialTheme.colorScheme.onSecondaryContainer)

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
                            vm.onBackClick()
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
                    }

                }

            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    if(selected.value == null){
                        IconButton(
                            onClick = {
                                vm.onSettingsClick()
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
                                vm.onFavouritesClick()
                            },
                            modifier = Modifier.width(80.dp),
                            content = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            }
                        )
                    }
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
                                    vm.onAddNoteClicked()
                                }

                            },
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            content = {
                                if(selected.value != null) Icon(Icons.Filled.Done, contentDescription = "Сохранить")
                                else Icon(Icons.Filled.Add, contentDescription = "Добавить") }

                        )
                    }
                }
            )
        },

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
             AddSet(setChange,selected,selectAd,removeAd,selectedAd, addFavourites)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PrintSet(sets: List<AdSet>, selected: MutableState<AdSet?>,selectSet: (AdSet)-> Unit){
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .padding(top = 65.dp)
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxHeight()
    ){
        items(sets){
                set ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .clickable {
                        // появляется выбранная подборка, клик - вывод списка объявлений подборки
                        //selected.value = set
                        selectSet(set)
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 10.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = set.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1, )
                        SuggestionChip(
                            colors = SuggestionChipDefaults.suggestionChipColors(MaterialTheme.colorScheme.tertiary),
                            border = SuggestionChipDefaults.suggestionChipBorder(Color.Transparent),
                            onClick = { Log.d(set.update_interval.toString(), "hello world") },
                            label = { Text(set.update_interval.toString() + " часов", color = MaterialTheme.colorScheme.surfaceVariant) },
                        )

                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(50.dp)
                            .padding(bottom = 5.dp)
                    ){
                        Text(text = "Объявлений: "+ set.adverts.count().toString())
                        Text(text = "Последнее обновление: " + set.last_update.toString())
                    }
                }


            }

        }
    }
}

@Composable
fun DrawDropmenu(onDeleteSet: ()-> Unit){
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(onClick = {
        expanded = !expanded
    }) {
        Icon(
            imageVector = Icons.Filled.Menu,
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
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                onDeleteSet()
            }
        )
        DropdownMenuItem(
            text = { Text("Обновить") },
            onClick = { Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show() }
        )
        DropdownMenuItem(
            text = { Text("Геолокация на все объекты") },
            onClick = { Toast.makeText(context, "Geoposition", Toast.LENGTH_SHORT).show() }
        )
    }
}

@Composable
fun DrawDropmenu(removeAd: (Advrt)-> Unit){
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(onClick = {
        expanded = !expanded
    }) {
        Icon(
            imageVector = Icons.Filled.Menu,
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
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                removeAd
            }
        )
        DropdownMenuItem(
            text = { Text("Обновить") },
            onClick = { Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show() }
        )
        DropdownMenuItem(
            text = { Text("Геолокация на все объекты") },
            onClick = { Toast.makeText(context, "Geoposition", Toast.LENGTH_SHORT).show() }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdvtAppTheme {

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            text = "screen_name.value"
                        )

                    },
                    actions = {
//                        DrawDropmenu(U)
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )

                    }

                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
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

                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {


                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            content = {
                                Icon(Icons.Filled.Add, contentDescription = "Добавить")
                            }

                        )
                    }
                )
            },

            ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .width(600.dp)
                    .height(1300.dp)
                    .padding(top = 60.dp)
                    .padding(6.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(3) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(all = 8.dp)
                            .clickable {
                                // появляется выбранная подборка, клик - вывод списка объявлений подборки
                                //selected.value = set
                                // selectSet(set)
                                //navController.navigate("set")
                                //addSet.value = !addSet.value
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        //onClick = {navController.navigate("set")}
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceAround

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Название подборки", fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                //Text(text = "5", fontSize = 20.sp)
                                AssistChip(
                                    onClick = { Log.d("5 часов", "hello world") },
                                    label = { Text("5 часов") },
                                )

                            }
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    //.padding(horizontal = 2.dp)
                                    .height(50.dp)
                            ){
                                Text(text = "Объявлений: 6")
                                Text(text = "Последнее обновление: " + LocalDate.now().toString())
                            }
                        }
                    }

                }
            }
        }


    }
}





enum class AppScreen(){
    Start,
    Set,
    Advert,
    Favourites,
    Settings
}

/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertApp(vm: AdvrtViewModel, navController: NavController = rememberNavController()) {
    val navController = rememberNavController()

    val sets = vm.sets
    val context = LocalContext.current
    val removedAd: Advrt = Advrt("","",0f)
    val selected by remember { mutableStateOf(vm.selectedSet) }
    val selectedAd by remember { mutableStateOf(vm.selectedAd) }

    val setChange:() -> Unit = {vm.onSetChange(selected.value!!.name,selected.value!!.update_interval)}
    val selectSet:(AdSet) -> Unit = {vm.onSetSelected(selected.value!!)}
    val onDeleteSet:() -> Unit = { vm.onDeleteSet()}
    val removeAd:() -> Unit = {vm.onRemoveAd()}
    val selectAd:(Advrt) -> Unit = {vm.onAdSelected(selectedAd.value!!)}

    val favourites by remember { mutableStateOf(vm.favourites) }
    val settings by remember { mutableStateOf(vm.settings) }

    val screen_name by remember { mutableStateOf(vm.screen_name) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = screen_name.value)

                },
                actions = {
                    if (selected.value != null){
                        if (selected.value!!.name != ""){
                            DrawDropmenu(onDeleteSet)
                        }
                    }
                },
                navigationIcon = {
                    if( favourites.value || settings.value){
                        IconButton(onClick = {
                            vm.onBackClick()
                            if(favourites.value){
                                // favourites = !favourites
                            }else if (settings.value){
                                //settings = !settings
                            }
                            else{
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }

                }

            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    if(selected.value == null){
                        IconButton(
                            onClick = {
                                // settings = !settings
                                vm.onSettingsClick()
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
                                vm.onFavouritesClick()
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
                    }
                },

                floatingActionButton = {
                    if(!favourites.value && !settings.value){
                        FloatingActionButton(
                            onClick = {
                                if(selected.value != null){
                                    // сохраняем
                                    vm.onEditComplete()
                                    //addSet.value = !addSet.value
                                }else{
                                    // Добавляем
                                    //addSet.value = !addSet.value
                                    vm.onAddNoteClicked()
                                }

                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            content = {
                                if(selected.value != null) Icon(Icons.Filled.Done, contentDescription = "Сохранить")
                                else Icon(Icons.Filled.Add, contentDescription = "Добавить") }

                        )
                    }
                }
            )
        },

        ){
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            // composable("main") { AdvrtScreen(vm, navController) }
            composable("settings") { PrintSettings() }
            composable("favourites") { PrintFavourites() }
            // composable("sets") { PrintSet(sets, selected,selectSet,navController) }
            composable("set") { AddSet(setChange,selected,selectAd,removeAd,selectedAd) }
            composable("advert") { PrintAdvert(selectedAd) }
        }
    }

}


/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    sliderList: MutableList<String>,
    backwardIcon: ImageVector = Icons.Default.KeyboardArrowLeft,
    forwardIcon: ImageVector = Icons.Default.KeyboardArrowRight,
    dotsActiveColor: Color = Color.DarkGray,
    dotsInActiveColor: Color = Color.LightGray,
    dotsSize: Dp = 10.dp,
    pagerPaddingValues: PaddingValues = PaddingValues(horizontal = 65.dp),
    imageCornerRadius: Dp = 16.dp,
    imageHeight: Dp = 250.dp,
) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {


            HorizontalPager(
                pageCount = sliderList.size,
                state = pagerState,
                contentPadding = pagerPaddingValues,
                modifier = modifier.weight(1f)
            ) { page ->
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                val scaleFactor = 0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)


                Box(modifier = modifier
                    .graphicsLayer {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    .alpha(
                        scaleFactor.coerceIn(0f, 1f)
                    )
                    .padding(10.dp)
                    .clip(RoundedCornerShape(imageCornerRadius))) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).scale(Scale.FILL)
                            .crossfade(true).data(sliderList[page]).build(),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.img),
                        modifier = modifier.height(imageHeight)
//                            .alpha(if (pagerState.currentPage == page) 1f else 0.5f)
                    )
                }
            }

        }
    }
}
*/
*/



