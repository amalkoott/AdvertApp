package ru.amalkoott.advtapp.ui.advert.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import ru.amalkoott.advtapp.R
import ru.amalkoott.advtapp.domain.entities.Advert


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrintAdvert(selectedAd: MutableState<Advert?>){
    val scrollState = rememberScrollState(0)

    val additionalParam = selectedAd.value!!.additionalParam //"{\"aboutFlat\":{\"Недвижимость\":\"Комната\",\"Этаж\":\"4\",\"ОбщаяПлощадь\":\"650,1\",\"ЖилаяПлощадь\":\"24,7\",\"ПлощадьКухни\":\"9,5\",\"Комнат\":\"1\",\"ВысотаПотолков\":\"2,75\",\"Санузел\":\"Раздельный\",\"ВидИзОкон\":\"-\",\"Ремонт\":\"Евроремонт\"},\"aboutHouse\":{\"ГодПостройки\":\"1961\",\"ТипСтен\":\"Панель\",\"Перекрытия\":\"Железобетонные\",\"Этажность\":\"5\",\"Парковка\":\"ВоДворе\",\"Подъезды\":\"1\",\"Лифт\":\"Пассажирский\",\"Отопление\":\"Центральное\",\"Аварийность\":\"Нет\",\"Газоснабжение\":\"Центральное\"},aboutSeller:{\"Агенство\":\"ЛабецкийНедвижимость\",\"Риэлтор\":{\"Домклик\":\"ДаниилУдовицкий\",\"ЦИАН\":\"НикитаЛабецкий\"},\"Телефон\":{\"Домклик\":\"+79062585011\",\"ЦИАН\":\"+7981226-82-05\"},\"Документы\":{\"Домклик\":\"СберID\",\"ЦИАН\":\"Паспорт\"},\"Профиль\":{\"Домклик\":\"https://agencies.domclick.ru/agent/3716624?region_id=70293348-4ea2-4741-934f-c1e9549d1fba?utm_content=offers.agent\",\"ЦИАН\":\"https://spb.cian.ru/agents/9447844/\"}}}"
    val jsonAP = Gson().fromJson(additionalParam, JsonObject::class.java)
    Column(
        Modifier
            .padding(bottom = 0.dp)
            .verticalScroll(state = scrollState)
            .background(color = MaterialTheme.colorScheme.background),) {
        val images = selectedAd.value!!.images
        if(images != null) {
        AutoSlidingCarousel(
            itemsCount = images.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[index])
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(250.dp)
                )
            }
        )
        }
        Column(Modifier.padding(horizontal = 32.dp, vertical = 0.dp),) {
            Text(text = (selectedAd.value!!.price!!).toString()  + ' ' + '₽',
                Modifier.padding(top = 32.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,)

            if (selectedAd.value!!.priceInfo != null){
                Text(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    text = selectedAd.value!!.priceInfo!!.toString(),//"Залог 100%",
                    fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
            }

            Row(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colorScheme.onSurface
                )

                // местоположение (адрес)
                Text(modifier = Modifier.padding(start = 5.dp),
                    text = selectedAd.value!!.address.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface)
            }
            Column(Modifier.padding(bottom = 16.dp)) {
                if (selectedAd.value!!.location != null){
                    val subways = selectedAd.value!!.location?.split("; ")
                    subways?.forEach{
                        if(it == "") return@forEach
                        val words = it.split(' ')
                        val travel = words[words.size - 1]
                        Row() {
                            Text(modifier = Modifier
                                .padding(start = 0.dp),
                                text = it.replace(travel,""),
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
                Text(
                    text = selectedAd.value!!.description.toString().replace("n","\n"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,)
            }

            if(jsonAP!= null){
                val paramModifier = Modifier.weight(3f)
                val rowModifier = Modifier.padding(bottom = 12.dp)
                val valueModifier = Modifier.padding(start = 6.dp).weight(4f)
                val sellerValueModifier = Modifier.padding(start = 6.dp).weight(7f)
                val aboutFlat = jsonAP["aboutFlat"].asJsonObject
                val aboutBuild = jsonAP["aboutHouse"].asJsonObject
                val aboutSeller = jsonAP["aboutSeller"].asJsonObject

                Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                    Text(
                        modifier = Modifier.padding( bottom = 18.dp),
                        text = "О квартире",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Недвижимость", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = valueModifier) {

                            Text(text = aboutFlat["Недвижимость"].toString().toValueFormatt(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }

                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Этаж", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["Этаж"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Общая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["ОбщаяПлощадь"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {

                            Text(text = "Жилая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["ЖилаяПлощадь"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Площадь кухни", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["ПлощадьКухни"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Комнат", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["Комнат"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Высота потолков", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["ВысотаПотолков"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Санузел", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["Санузел"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Вид из окон", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["ВидИзОкон"].toString().toValueFormatt(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Ремонт", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutFlat["Ремонт"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }

                }

                Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                    Text(
                        modifier = Modifier.padding( bottom = 18.dp),text = "О доме",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,)

                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Год постройки", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["ГодПостройки"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Тип стен", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["ТипСтен"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Перекрытия", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Перекрытия"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Этажность", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Этажность"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Парковка", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Парковка"].toString().toValueFormatt(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Подъезды", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Подъезды"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Лифт", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Лифт"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Отопление", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Отопление"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }

                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Газоснабжение", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Газоснабжение"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Аварийность", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = aboutBuild["Аварийность"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                }
                Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                    Text(
                        modifier = Modifier.padding( bottom = 18.dp),text = "Продавец",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,)

                    val context = LocalContext.current
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Агенство", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = sellerValueModifier) {
                            Text(text = aboutSeller["Агенство"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Риэлтор", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = sellerValueModifier) {
                            var sellers: JsonObject? = null
                            try {
                                sellers = aboutSeller["Риэлтор"].asJsonObject
                            }
                            catch (e:IllegalStateException){
                                Text(text = aboutSeller["Риэлтор"].toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            }
                            sellers?.entrySet()?.forEach{
                                Text(text = "${it.key}: ${it.value.toString().toValueFormattUpperCase()}", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                            }
                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Телефон", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = sellerValueModifier) {
                            var phones: JsonObject? = null
                            try {
                                phones = aboutSeller["Телефон"].asJsonObject
                            }
                            catch (e:IllegalStateException){
                                Text(text = aboutSeller["Телефон"].toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            }
                            phones?.entrySet()?.forEach{
                                Text(text = "${it.key}: ${it.value.toString().toValueFormattUpperCase()}", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                            }
                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier, ) {
                            Text(text = "Документы", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = sellerValueModifier) {
                            var docs: JsonObject? = null
                            try {
                                docs = aboutSeller["Документы"].asJsonObject
                            }
                            catch (e:IllegalStateException){
                                val value = aboutSeller["Документы"].toString()
                                when(value){
                                    "\"-\"" -> Text(text = "(не указано)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary)
                                    "\"нет\"" -> Text(text = "Отсустствуют/Не подтверждены", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error)
                                    else -> Text(text = value.toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            docs?.entrySet()?.forEach{
                                val value = it.value.toString()
                                when(value){
                                    "\"-\"" -> Text(text = "(не указано)", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary)
                                    "\"нет\"" -> Text(text = "Отсустствуют/Не подтверждены", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error)
                                    else -> Text(text = "${it.key}: ${value.toValueFormattUpperCase()}", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                    val browsContext = LocalContext.current
                    Row(Modifier.padding(bottom = 72.dp)){
                        Column(modifier = paramModifier) {
                            Text(text = "Профиль", modifier = Modifier.padding(top = 12.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = sellerValueModifier) {

                            aboutSeller["Профиль"].asJsonObject.entrySet().forEach {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val url = it.value.toString()
                                    Text(text = it.key, fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                                    TextButton(onClick = {
                                        try{
                                            if (url != "-"){
                                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url.replace("\"","")))
                                                browsContext.startActivity(browserIntent)
                                            }
                                        }catch (e:Exception){
                                            Toast.makeText(browsContext,"Сейчас невозможно открыть браузер...",Toast.LENGTH_SHORT).show()
                                        }

                                    }) {
                                        if (url != "-"){
                                            Text(text = "Профиль", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.tertiary,)
                                        }else{
                                            Text(text = "(скрыт)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary,)
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
    selectedColor: Color = MaterialTheme.colorScheme.tertiary,
    unSelectedColor: Color = Color.Gray,
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 5000,
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
