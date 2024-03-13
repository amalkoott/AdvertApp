package ru.amalkoott.advtapp.ui.advert



import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advrt
import ru.amalkoott.advtapp.ui.theme.AdvtAppTheme


//@TODO splash screen https://www.google.com/search?q=splash+screen+compose+android&oq=splash+screen+comp&gs_lcrp=EgZjaHJvbWUqDAgCEAAYFBiHAhiABDIHCAAQABiABDIGCAEQRRg5MgwIAhAAGBQYhwIYgAQyBwgDEAAYgAQyCAgEEAAYFhgeMggIBRAAGBYYHjIKCAYQABgPGBYYHjIKCAcQABgPGBYYHjIICAgQABgWGB4yCAgJEAAYFhge0gEIOTQxOWowajeoAgCwAgA&sourceid=chrome&ie=UTF-8#fpstate=ive&vld=cid:57e4cdbe,vid:VTRz-8DPowM,st:0
enum class AppScreen(){
    Start,
    Set,
    Advert,
    Favourites,
    Settings
}

// вывод списка подборок (главный экран по сути)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvrtScreen(vm: AdvrtViewModel) {
//    val selected by remember { mutableStateOf(vm.selected) }
    //val sets by vm.sets.collectAsState()
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
                            DrawDropmenu(onDeleteSet, selected.value!!)
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
        // мб стоит рассмотреть замену на switch?

        if(selected.value == null){
            if(favourites.value) {
                PrintFavourites()
            }else{
                if (settings.value) {
                    PrintSettings()
                } else
                    PrintSet(sets, selected,selectSet, screen_name)
            }
        } else{
            // создаем новую подборку
            AddSet(setChange,selected,selectAd,removeAd,selectedAd)
        }
        // если addSet = false -> PrintSet()
        // если addSet = true -> AddAdvert()
    }
}

@SuppressLint("Range")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdvtAppTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
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
                        .padding(all = 8.dp)
                        .clickable {
                            // появляется выбранная подборка, клик - вывод списка объявлений подборки
                            //selected.value = note
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .background(color = Color.Green)
                                .fillMaxWidth(fraction = 2f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(text = "set.name")
                            Text(text = "6")
                        }
                        Icon(
                            Icons.Filled.Menu,
                            modifier = Modifier
                                .padding(start = 100.dp),
                            contentDescription = "Настройки подборки")

                    }
                    Text(text = "Объявлений: " + "set.adverts.count().toString()")
                    Row {
                        Text(text = "Последнее обновление: " + "set.last_update.toString()")
                    }


                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintSet(sets: List<AdSet>, selected: MutableState<AdSet?>,selectSet: (AdSet)-> Unit, screen: MutableState<String>){
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .padding(top = 60.dp)
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)
    ){
        items(sets){
                set ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
                    .clickable {
                        // появляется выбранная подборка, клик - вывод списка объявлений подборки
                        selected.value = set
                        selectSet(set)
                        //addSet.value = !addSet.value
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(
                        modifier = Modifier
                            .padding(end = 100.dp),
                        horizontalArrangement = Arrangement.Start
                    ){

                        Text(text = set.name)
                        Text(text = set.update_interval.toString())
                    }
                }
                Text(text = "Объявлений: " + set.adverts.count().toString())
                Row {
                    Text(text = "Последнее обновление: " + set.last_update.toString())
                }
            }

        }
    }
}

@Composable
fun DrawDropmenu(onDeleteSet: ()-> Unit, set: AdSet){
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSet(setChange: ()-> Unit, select: MutableState<AdSet?>, selectAd: (Advrt)-> Unit, removeAd: ()-> Unit,selectedAd: MutableState<Advrt?>){
    val context = LocalContext.current

    var update_interval by remember { mutableStateOf(select.value!!.update_interval) }
    var name by remember { mutableStateOf(select.value!!.name) }
    val adverts = select.value!!.adverts

    
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
                    select.value!!.name = it
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
                        select.value!!.update_interval = it.toInt()
                    }catch (e: NumberFormatException){
                        // если интервал не указан, то идет значение по умолчанию
                        update_interval = 10
                        select.value!!.update_interval = 10
                    }
                    setChange()
                },
                placeholder = { Text(
                    text = "Интервал обновления",
                    color = MaterialTheme.colorScheme.onBackground)},
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .padding(top = 130.dp)
                    .padding(6.dp)
                    .background(MaterialTheme.colorScheme.background)){
                items(adverts){advert ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp)
                            .clickable {
                                // появляется выбранная подборка, клик - вывод списка объявлений подборки
                                selectedAd.value = advert
                                selectAd(advert)
                                //selected.value = set
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ){
                        Text(text = advert.name)
                        Text(text = advert.caption)
                        Text(text = advert.price.toString())
                        Row(
                            modifier = Modifier
                                .padding(end = 100.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                                    selectedAd.value = advert
                                    selectAd(advert)
                                    removeAd()
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
                                    //   vm.onSettingsClick()
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrintAdvert(selectedAd: MutableState<Advrt?>){
    val images = listOf(
        "https://desktopmania.ru/pics/00/05/13/DesktopMania.ru-5132-300x240.jpg",
        "https://c.wallhere.com/photos/10/26/1920x1200_px_animals_cats_Tanks-1914705.jpg!s",
        "https://images.chesscomfiles.com/uploads/v1/user/77559592.9cb711dc.160x160o.e195dd620cda.jpeg",
    )
    Card() {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            AutoSlidingCarousel(
                itemsCount = images.size,
                itemContent = { index ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[index])
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(200.dp)
                    )
                }
            )
        }
        Text(text = selectedAd.value!!.name)
        Text(text = selectedAd.value!!.price.toString())
        Text(text = selectedAd.value!!.caption)
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color.Yellow /* Color.Yellow */,
    unSelectedColor: Color = Color.Gray /* Color.Gray */,
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 5000,//AUTO_SLIDE_DURATION,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(pagerState.currentPage) {
        delay(autoSlideDuration)
        pagerState.animateScrollToPage((pagerState.currentPage + 1) % itemsCount)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(count = itemsCount, state = pagerState) { page ->
            itemContent(page)
        }

        // you can remove the surface in case you don't want
        // the transparant bacground
        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 8.dp
            )
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
@Composable
fun PrintFavourites(){
    Text(text = "Favourites", modifier = Modifier.padding(top = 60.dp))
    //onFavouritesClick()
}

@Composable
fun PrintSettings(){
    Text(text = "Settings", modifier = Modifier.padding(top = 60.dp))
}

