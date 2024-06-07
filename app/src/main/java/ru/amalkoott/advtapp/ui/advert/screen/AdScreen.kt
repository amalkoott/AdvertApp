package ru.amalkoott.advtapp.ui.advert.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
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
import ru.amalkoott.advtapp.R
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.ui.theme.AdvtAppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrintAdvert(selectedAd: MutableState<Advert?>){
    val scrollState = rememberScrollState(0)
    Column(
        Modifier
            .padding(bottom = 0.dp)
            .verticalScroll(state = scrollState)
            .background(color = MaterialTheme.colorScheme.background),) {
        val images = selectedAd.value!!.images
        if(images != null) {


            val testImg = arrayOf("https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903923-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903930-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903833-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903919-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903832-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903926-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903927-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903823-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903827-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903837-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903839-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903918-1.jpg",
            "https://images.cdn-cian.ru/images/kvartira-sanktpeterburg-karavaevskaya-ulica-2114903920-1.jpg",)



        AutoSlidingCarousel(
            itemsCount = testImg.size,//images.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(testImg[index])
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(250.dp)
                )
            }
        )
        }
        Column(Modifier.padding(horizontal = 32.dp, vertical = 0.dp),) {
            Text(text = "4400000"/*(selectedAd.value!!.price!!).toString()*/  + ' ' + '₽',
                Modifier.padding(top = 32.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,)
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                text = "Ипотека",
                fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

            Row(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colorScheme.onSurface
                )

                // местоположение (адрес)
                Text(modifier = Modifier.padding(start = 5.dp),
                    text = "Санкт-Петербург, р-н Невский, Рыбацкое, Караваевская ул., 35",/*selectedAd.value!!.address.toString(),*///"Звенигородский пр-кт, д 17/1, Санкт-Петербург",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface)
            }
            Column(Modifier.padding(bottom = 16.dp)) {
                val testSub = "Рыбацкое 10 мин. пешком; Обухово 7 мин. транспортом; Пролетарская 8 мин. транспортом;"
                if (testSub/*selectedAd.value!!.location*/ != null){
                    val subways = testSub/*selectedAd.value!!.location?*/.split("; ")
                    subways?.forEach{
                        if(it == "") return@forEach
                        val words = it.split(' ')
                        val travel = words[words.size - 1]
                        Row() {
                            Text(modifier = Modifier
                                .padding(start = 0.dp),
                                text = it.replace(travel,""),//selectedAd.value!!.location.toString().replace("\"",""),//"Звенигородский пр-кт, д 17/1, Санкт-Петербург",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)

                                    if(travel == "транспортом")
                                        Icon(painter = painterResource(R.drawable.ic_bus), contentDescription = "Localized description",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,)
                                    else
                                        Icon(painter = painterResource(R.drawable.ic_walk), contentDescription = "Localized description",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                    }
                }
            }
            // до метро\центра
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Описание",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,)
                val testDes = "Продается квартира недалеко от метро Рыбацкое. Квартира теплая, требует ремонта. Парадная  чистая, после ремонта. Хороший зеленый двор с детской площадкой. Район города зеленый, тихий, с хорошей инфраструктурой. Рядом КАД. Один собственник, никто не прописан. Звоните, покажем в удобное для вас время."
                Text(
                    text = testDes/*selectedAd.value!!.description.toString()*/.replace("n","\n"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,)
            }

            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    modifier = Modifier.padding( bottom = 8.dp),
                    text = "О квартире",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,)

                Row() {
                    Column() {
                        Text(text = "Недвижимость", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Этаж", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Общая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Жилая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Площадь кухни", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Высота потолков", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Санузел", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Вид из окон", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Ремонт", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    }
                    Column(Modifier.padding(start = 4.dp)){
                        Text(text = "Вторичка", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "1", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "31", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "17", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "5,5", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "2,5", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Совмещенный", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Во двор", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Косметический", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    }
                }
            }

            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    modifier = Modifier.padding( bottom = 8.dp),text = "О доме",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,)

                Row(Modifier
                    .padding(bottom = 60.dp)) {
                    Column() {
                        Text(text = "Год постройки", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Стены", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Перекрытия", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Этажность", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Парковка", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Подъезды", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Лифт", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Отопление", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Аварийность", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Газоснабжение", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                    }
                    Column(Modifier.padding(start = 4.dp)){
                        Text(text = "1961", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Кирпичные", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Железобетонные", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "4", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Наземная", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "3", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "-", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Центральное", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "-", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Центральное", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    }
                }
            }
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    modifier = Modifier.padding( bottom = 8.dp),text = "Продавец",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,)

                val context = LocalContext.current
                Row(Modifier
                    .padding(bottom = 60.dp)) {
                    Column() {
                        Text(text = "Агенство", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Риэлтор", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Телефон", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        Text(text = "Документы", modifier = Modifier.padding(top = (1*14).dp, bottom = 85.dp),fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Профиль", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                    }
                    Column(Modifier.padding(start = 4.dp)){
                        Text(text = "ИТЕРА", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Лариса Новикова", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "+79219561384", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        Column(Modifier.padding(start = 4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "ЦИАН", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                                TextButton(onClick = {

                                }) {
                                    Text(text = "(паспорт не верифицирован)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error,)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Авито", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                                TextButton(onClick = {
                                }) {
                                    Text(text = "Подтвержден телефон", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary,)
                                }
                            }

                        }

                        Column(Modifier.padding(start = 4.dp)) {

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "ЦИАН", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                                    TextButton(onClick = {

                                    }) {
                                        Text(text = "(скрыт)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary,)
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Авито", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                                    TextButton(onClick = {
                                        val ur = "https://www.avito.ru/brands/i116715797/all/nedvizhimost?page_from=from_item_card_icon&iid=3923473055&sellerId=31f2f79719311e57f92be9cf9931f4f8"

                                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(ur))
                                        context.startActivity(browserIntent)
                                    }) {
                                        Text(text = "ПРОФИЛЬ", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.tertiary,)
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
    selectedColor: Color = MaterialTheme.colorScheme.tertiary /* Color.Yellow */,
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
