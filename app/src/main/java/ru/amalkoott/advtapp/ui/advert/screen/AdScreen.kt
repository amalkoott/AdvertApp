package ru.amalkoott.advtapp.ui.advert.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.ui.theme.AdvtAppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrintAdvert(selectedAd: MutableState<Advert?>){
    val scrollState = rememberScrollState(0)
    val imgs = arrayOf(
        "https://desktopmania.ru/pics/00/05/13/DesktopMania.ru-5132-300x240.jpg",
        "https://c.wallhere.com/photos/10/26/1920x1200_px_animals_cats_Tanks-1914705.jpg!s",
        "https://images.chesscomfiles.com/uploads/v1/user/77559592.9cb711dc.160x160o.e195dd620cda.jpeg",
    )
            Column(
                Modifier
                    .padding(bottom = 10.dp)
                    .verticalScroll(state = scrollState),) {
                AutoSlidingCarousel(
                    //itemsCount = images.size,
                    itemsCount = selectedAd.value!!.images.size,
                    itemContent = { index ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(selectedAd.value!!.images[index])
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(250.dp)
                        )
                    }
                )
                Text(text = (selectedAd.value!!.price!!).toString() + " Р",
                    Modifier.padding(horizontal = 25.dp, vertical = 20.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1)

                Column(Modifier.padding(horizontal = 25.dp, vertical = 10.dp),) {
                    Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = "Localized description",
                        )

                        // местоположение (адрес)
                        Text(modifier = Modifier.padding(start = 5.dp),
                            text = selectedAd.value!!.address.toString(),//"Звенигородский пр-кт, д 17/1, Санкт-Петербург",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,)


                        // до метро\центра
                        if (selectedAd.value!!.location != null){
                            Text(modifier = Modifier.padding(start = 5.dp),
                                text = selectedAd.value!!.location.toString(),//"Звенигородский пр-кт, д 17/1, Санкт-Петербург",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,)
                        }

                    }

                    Text(
                        modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                        text = "Описание",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Text(
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        text = selectedAd.value!!.description.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,)

                    Text(
                        modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                        text = "О квартире",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Row() {
                        Column() {
                            Text(text = "параметр 1")
                            Text(text = "параметр 2")
                            Text(text = "параметр 3")
                        }
                        Column(){
                            Text(text = "значение 1")
                            Text(text = "значение 2")
                            Text(text = "значение 3")
                        }
                    }

                    Text(
                        modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),text = "О доме",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Row(Modifier
                        .padding(bottom = 60.dp)) {
                        Column() {
                            Text(text = "параметр 1")
                            Text(text = "параметр 2")
                            Text(text = "параметр 3")
                        }
                        Column(){
                            Text(text = "значение 1")
                            Text(text = "значение 2")
                            Text(text = "значение 3")
                        }
                    }
                }

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun AdPreview() {
    /*
    val name: String,
    val ad_caption: String,
    * val price: Float,
    ! val footage: Float, // метраж
    ! val room: Int,
    ! val floor: Int,
    val location: String,
    val home_caption: String,
    */
    val scrollState = rememberScrollState(0)
    val imgs = arrayOf(
        "https://desktopmania.ru/pics/00/05/13/DesktopMania.ru-5132-300x240.jpg",
        "https://c.wallhere.com/photos/10/26/1920x1200_px_animals_cats_Tanks-1914705.jpg!s",
        "https://images.chesscomfiles.com/uploads/v1/user/77559592.9cb711dc.160x160o.e195dd620cda.jpeg",
    )
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
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            ){
                            Button(
                                onClick = {
                                },
                                border = BorderStroke(10.dp, MaterialTheme.colorScheme.background),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Magenta, contentColor = Color.Red),

                                modifier = Modifier
                                    .width(165.dp)
                                    .height(65.dp),
                                content = {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Localized description",
                                    )
                                }
                            )
                            Button(
                                onClick = {
                                },
                                border = BorderStroke(15.dp, MaterialTheme.colorScheme.background),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Cyan, contentColor = Color.Red),

                                modifier = Modifier
                                    .width(170.dp)
                                    .height(70.dp),
                                content = {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Localized description",
                                    )
                                }
                            )
                        }
                    },
                    containerColor = Color.Transparent
                )
            },

            ){

            Column(
                Modifier
                    .padding(bottom = 10.dp)
                    .verticalScroll(state = scrollState),) {
                AutoSlidingCarousel(
                    itemsCount = imgs.size,
                    //itemsCount = selectedAd.value!!.images.size,
                    itemContent = { index ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imgs[index])
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(top = 70.dp)
                                .height(250.dp)
                        )
                    }
                )
                Text(text = "2 400 000 Р",
                    Modifier.padding(horizontal = 25.dp, vertical = 20.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1)

                Row(
                    Modifier.fillMaxWidth().fillMaxHeight().padding(top = 10.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically){
                    SuggestionChip(
                        colors = SuggestionChipDefaults.suggestionChipColors(MaterialTheme.colorScheme.tertiaryContainer),
                       // border = SuggestionChipDefaults.suggestionChipBorder(Color.Transparent),
                        onClick = { Log.d("footage","24")  },
                        label = { Text(
                            text = "24\nкв.м",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.width(60.dp).padding(vertical = 7.dp),
                            textAlign = TextAlign.Center,

                            lineHeight = 12.sp)}
                    )
                    SuggestionChip(
                        colors = SuggestionChipDefaults.suggestionChipColors(MaterialTheme.colorScheme.tertiaryContainer),
                       // border = SuggestionChipDefaults.suggestionChipBorder(Color.Transparent),
                        onClick = { Log.d("footage","24")  },
                        label = { Text(
                            text = "1\n" +
                                    "комн.",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.width(60.dp).padding(vertical = 7.dp),
                            textAlign = TextAlign.Center,

                            lineHeight = 12.sp)}
                    )
                    SuggestionChip(
                        colors = SuggestionChipDefaults.suggestionChipColors(MaterialTheme.colorScheme.tertiaryContainer),
                        border = SuggestionChipDefaults.suggestionChipBorder(Color.Transparent),
                        onClick = { Log.d("footage","24")  },
                        label = { Text(
                            text = "14 этаж\nиз 19",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.width(60.dp).padding(vertical = 7.dp),
                            textAlign = TextAlign.Center,

                            lineHeight = 12.sp)}
                    )
                }

                Column(Modifier.padding(horizontal = 25.dp, vertical = 10.dp),) {
                    Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = "Localized description",
                        )

                        Text(modifier = Modifier.padding(start = 5.dp),
                            text = "Звенигородский пр-кт, д 17/1, Санкт-Петербург",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,)
                    }

                    Text(
                        modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                        text = "Описание",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Text(
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        text = "Продается уютная квартира в районе с развитой инфраструктурой. Евро-двушка.- 34,7 м.кв+лоджия. Просторная кухня 14 м.кв. Широкая застекленная лоджия.В квартире очень тепло. Прекрасны йремонт от застройщика. Светлые обои, качественный ламинат. В санузле кафельная плитка .Рядом с домом школа с бассейном, магазины и вся необходимая инфраструктура. Один собственник, обременений нет. Буду рада показать этот отличный вариант. Ключи у агента.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,)

                    Text(
                        modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                        text = "О квартире",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Row() {
                        Column() {
                            Text(text = "параметр 1")
                            Text(text = "параметр 2")
                            Text(text = "параметр 3")
                        }
                        Column(){
                            Text(text = "значение 1")
                            Text(text = "значение 2")
                            Text(text = "значение 3")
                        }
                    }

                    Text(
                        modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),text = "О доме",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Row(Modifier
                        .padding(bottom = 60.dp)) {
                        Column() {
                            Text(text = "параметр 1")
                            Text(text = "параметр 2")
                            Text(text = "параметр 3")
                        }
                        Column(){
                            Text(text = "значение 1")
                            Text(text = "значение 2")
                            Text(text = "значение 3")
                        }
                    }
                }

            }
        }
    }
}
*/