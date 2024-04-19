package ru.amalkoott.advtapp.ui.advert.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert

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
    //getAdverts:()->Unit
    //adSetAdverts: MutableStateFlow<List<Advert>>
){
    val context = LocalContext.current
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
            PrintAdvert(selectedAd)
        }else
        {

            //Surface(//Modifier.fillMaxWidth().shadow(elevation = 2.dp, clip = true),
                //shadowElevation = 4.dp)

                Column(//modifier = Modifier.shadow(elevation = 2.dp, clip = true)//, shape = RoundedCornerShape(8.dp))
                  /*  modifier = Modifier.drawBehind {
                    val strokeWidth = 200 * density
                    val y = size.height - strokeWidth / 2

                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.DarkGray, Color.Transparent.copy(alpha = 1f)),
                        ),
                        Offset(-80f, size.height-40),

                        )
                }*/
                )
                {
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

            if (selected.value!!.adverts!!.isEmpty()){
                PrintFilters()
            }else{
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Test(){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(text = "Квартиры",
                        color = MaterialTheme.colorScheme.onPrimary)

                },

                actions = {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Localized description"
                    )
                },

                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )

                }

            )
        },
        floatingActionButton = {
            FloatingActionButton(
            onClick = {

            },
            contentColor = MaterialTheme.colorScheme.onTertiary,
            containerColor = MaterialTheme.colorScheme.tertiary,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
            content = { Icon(Icons.Filled.Done, contentDescription = "Сохранить") }

        )
        }

    ) {
        Column(modifier = Modifier
            .background(Color.White)
            .padding(top = 64.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxWidth())
        {
            Column(modifier = Modifier.drawBehind {
                val strokeWidth = 200 * density
                val y = size.height - strokeWidth / 2

                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.DarkGray, Color.Transparent),
                    ),
                    Offset(-80f, size.height-20),

                )
            })
            {
                TextField(
                    value = "Квартиры",
                    onValueChange = {  },
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
                    value = "6",
                    onValueChange = {

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


            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .fillMaxHeight()
                    //.padding(top = 176.dp)
                    .background(Color.Transparent))
            // Карточки с картинками
            {
                item{
                    var price by remember { mutableStateOf("") }
                    var sliderPosition by remember { mutableStateOf(0f..100f) }

                    Column(modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        )) {
                        Column (
                            modifier = Modifier
                                .padding(
                                    start = 0.dp,
                                    end = 0.dp,
                                    bottom = 24.dp,
                                )
                                .fillMaxWidth())
                        {
                            TextField(
                                value = sliderPosition.toString(),
                                onValueChange = {

                                },
                                label = { Text("Цена",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                placeholder = { Text(
                                    text = "Название города",
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
                                        //start = 16.dp,
                                        // end = 16.dp,
                                        //bottom = 16.dp,
                                    )
                                    .fillMaxWidth()
                            )
                            /*
                            Text(text = sliderPosition.toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.padding(top = 8.dp))
                            */
                            RangeSlider(
                                value = sliderPosition,
                                steps = 5,
                                onValueChange = { range -> sliderPosition = range },
                                valueRange = 0f..100f,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                onValueChangeFinished = {
                                    // launch some business logic update with the state you hold
                                    // viewModel.updateSelectedSliderValue(sliderPosition)
                                },
                            )
                        }
                        TextField(
                            value = "Санкт-Петербург",
                            onValueChange = {

                            },
                            label = { Text("Город",
                                color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            placeholder = { Text(
                                text = "Название города",
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
                                    //start = 16.dp,
                                    // end = 16.dp,
                                    bottom = 24.dp,
                                )
                                .fillMaxWidth()
                        )
                        Column(modifier = Modifier
                            .padding(
                                start = 16.dp,
                                // end = 16.dp,
                                bottom = 24.dp,
                            )
                            .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Категория", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            val context = LocalContext.current
                            val coffeeDrinks = arrayOf("Недвижимость", "Транспорт", "Электроника", "Услуги")
                            var expanded by remember { mutableStateOf(false) }
                            var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    //.padding(32.dp)
                            ) {
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = {
                                        expanded = !expanded
                                    }
                                ) {
                                    TextField(
                                        value = selectedText,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.surface
                                        ),
                                        onValueChange = {},
                                        readOnly = true,
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                        modifier = Modifier.menuAnchor()
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        coffeeDrinks.forEach { item ->
                                            DropdownMenuItem(
                                                text = { Text(text = item) },
                                                onClick = {
                                                    selectedText = item
                                                    expanded = false
                                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                /*
                items(3){
                    Card(
                        colors = CardDefaults.cardColors(
                            //containerColor = MaterialTheme.colorScheme.surfaceTint,
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
                            .clickable {
                                // появляется выбранная подборка, клик - вывод списка объявлений подборки
                                //selectedAd.value = advert
                                //selectAd(advert)
                                //selected.value = set
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ){
                        Column(){
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(Color.Red)
                                    .weight(2f),
                                //colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                            ) {

                            }
                            Column(
                                Modifier
                                    .weight(2f)
                                    .padding(
                                        start = 24.dp,
                                        top = 16.dp,
                                        bottom = 16.dp,
                                        end = 24.dp
                                    )
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                 verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "1 комн. кв Санкт-Петербург",
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        //style = MaterialTheme.typography.titleMedium.copy(hyphens = Hyphens.None),
                                        modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                                    )
                                    Text(
                                        text = "1800000" + ' ' + '₽',
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(top = 0.dp, bottom = 0.dp),
                                    )
                                }
                                Text(
                                    text = "Продам 1-комн. квартиру в Кировском районе Санкт-Петербурга. Окна выходят на зеленый двор, есть закрытый балкон. Рядом (в 5 мин. хотьбы) остановка общественного транспорта",
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Row(
                                    modifier = Modifier
                                        .padding(end = 0.dp)
                                        .height(48.dp)
                                        .fillMaxHeight()
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    IconButton(
                                        onClick = {
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
                                            //Toast.makeText(context, "Fav", Toast.LENGTH_SHORT).show()
                                            //selectedAd.value = advert
                                            //selectAd(advert)
                                            // addFavourites(advert)
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
                                    IconButton(
                                        onClick = {
                                            // Toast.makeText(context, "Location", Toast.LENGTH_SHORT).show()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox() {
    val context = LocalContext.current
    val coffeeDrinks = arrayOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintFilters(){
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
            var price by remember { mutableStateOf("") }
            var sliderPosition by remember { mutableStateOf(0f..100f) }

            Column(modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                )) {
                Column (
                    modifier = Modifier
                        .padding(
                            start = 0.dp,
                            end = 0.dp,
                            bottom = 24.dp,
                        )
                        .fillMaxWidth())
                {
                    TextField(
                        value = sliderPosition.toString(),
                        onValueChange = {

                        },
                        label = { Text("Цена",
                            color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        placeholder = { Text(
                            text = "Название города",
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
                                //start = 16.dp,
                                // end = 16.dp,
                                //bottom = 16.dp,
                            )
                            .fillMaxWidth()
                    )
                    RangeSlider(
                        value = sliderPosition,
                        steps = 5,
                        onValueChange = { range -> sliderPosition = range },
                        valueRange = 0f..100f,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onValueChangeFinished = {
                            // launch some business logic update with the state you hold
                            // viewModel.updateSelectedSliderValue(sliderPosition)
                        },
                    )
                }
                TextField(
                    value = "Санкт-Петербург",
                    onValueChange = {

                    },
                    label = { Text("Город",
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text(
                        text = "Название города",
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
                            //start = 16.dp,
                            // end = 16.dp,
                            bottom = 24.dp,
                        )
                        .fillMaxWidth()
                )
                Column(modifier = Modifier
                    .padding(
                        start = 16.dp,
                        // end = 16.dp,
                        bottom = 24.dp,
                    )
                    .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Категория", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val context = LocalContext.current
                    val coffeeDrinks = arrayOf("Недвижимость", "Транспорт", "Электроника", "Услуги")
                    var expanded by remember { mutableStateOf(false) }
                    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                        //.padding(32.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {
                            TextField(
                                value = selectedText,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.surface
                                ),
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                coffeeDrinks.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedText = item
                                            expanded = false
                                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
