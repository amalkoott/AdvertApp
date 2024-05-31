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

            Row(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colorScheme.onSurface
                )

                // местоположение (адрес)
                Text(modifier = Modifier.padding(start = 5.dp),
                    text = selectedAd.value!!.address.toString(),//"Звенигородский пр-кт, д 17/1, Санкт-Петербург",
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

                Text(
                    text = selectedAd.value!!.description.toString().replace("n","\n"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,)
            }
            /*
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    modifier = Modifier.padding( bottom = 8.dp),
                    text = "О квартире",
                    fontSize = 22.sp,
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
            }

            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    modifier = Modifier.padding( bottom = 8.dp),text = "О доме",
                    fontSize = 22.sp,
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
            */
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
