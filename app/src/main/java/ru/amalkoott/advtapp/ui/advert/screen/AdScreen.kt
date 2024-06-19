package ru.amalkoott.advtapp.ui.advert.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import ru.amalkoott.advtapp.R
import ru.amalkoott.advtapp.domain.Advert


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrintAdvert(selectedAd: MutableState<Advert?>){
    val scrollState = rememberScrollState(0)
   // val additionalParam =  "{\"aboutFlat\":{\"Недвижимость\":\"Комната\",\"Этаж\":\"4\",\"ОбщаяПлощадь\":\"988\",\"ЖилаяПлощадь\":\"15\",\"ПлощадьКухни\":\"29\",\"Комнат\":\"1\",\"ВысотаПотолков\":\"3\",\"Санузел\":\"Раздельный\",\"ВидИзОкон\":\"НаУлицу\",\"Ремонт\":\"Евроремонт\"},\"aboutHouse\":{\"ГодПостройки\":\"1951\",\"ТипСтен\":\"Блочный\",\"Перекрытия\":\"Железобетонные\",\"Этажность\":\"5\",\"Парковка\":\"ВоДворе\",\"Подъезды\":\"-\",\"Лифт\":\"Пассажирский\",\"Отопление\":\"Котел\",\"Аварийность\":\"Нет\",\"Газоснабжение\":\"Центральное\"},aboutSeller:{\"Агенство\":\"ООО'Панорама'\",\"Риэлтор\":\"МаринаДербикова\",\"Телефон\":\"+79221245860\",\"Документы\":{\"Домклик\":\"-\"},\"Профиль\":{\"Домклик\":\"https://agencies.domclick.ru/agent/2928847?region_id=58d15e6b-f489-4e5f-9a98-7036a86f05ab?utm_content=offers.agent\"}}}"

    // "{\"aboutFlat\":{\"Недвижимость\":\"Комната\",\"Этаж\":\"4\",\"ОбщаяПлощадь\":\"650,1\",\"ЖилаяПлощадь\":\"24,7\",\"ПлощадьКухни\":\"9,5\",\"Комнат\":\"1\",\"ВысотаПотолков\":\"2,75\",\"Санузел\":\"Раздельный\",\"ВидИзОкон\":\"-\",\"Ремонт\":\"Евроремонт\"},\"aboutHouse\":{\"ГодПостройки\":\"1961\",\"ТипСтен\":\"Панель\",\"Перекрытия\":\"Железобетонные\",\"Этажность\":\"5\",\"Парковка\":\"ВоДворе\",\"Подъезды\":\"1\",\"Лифт\":\"Пассажирский\",\"Отопление\":\"Центральное\",\"Аварийность\":\"Нет\",\"Газоснабжение\":\"Центральное\"},aboutSeller:{\"Агенство\":\"ЛабецкийНедвижимость\",\"Риэлтор\":{\"Домклик\":\"ДаниилУдовицкий\",\"ЦИАН\":\"НикитаЛабецкий\"},\"Телефон\":{\"Домклик\":\"+79062585011\",\"ЦИАН\":\"+7981226-82-05\"},\"Документы\":{\"Домклик\":\"СберID\",\"ЦИАН\":\"Паспорт\"},\"Профиль\":{\"Домклик\":\"https://agencies.domclick.ru/agent/3716624?region_id=70293348-4ea2-4741-934f-c1e9549d1fba?utm_content=offers.agent\",\"ЦИАН\":\"https://spb.cian.ru/agents/9447844/\"}}}"
    /*
{
    "aboutFlat":{
                    "Недвижимость":"Комната",
                    "Этаж":"4",
                    "Общая площадь":"988",
                    "Жилая площадь":"15",
                    "Площадь кухни":"29",
                    "Комнат":"1",
                    "Высота потолков":"3",
                    "Санузел":"Раздельный",
                    "Вид из окон":"На улицу",
                    "Ремонт":"Евроремонт"
                },
    "aboutHouse":{
        "Год постройки":"1951",
        "Тип стен":"Блочный",
        "Перекрытия":"Железобетонные",
        "Этажность":"5",
        "Парковка":"Во дворе",
        "Подъезды":"",
        "Лифт":"Пассажирский",
        "Отопление":"Котел",
        "Аварийность":"-",
        "Газоснабжение":"Центральное"
    },
    aboutSeller:{
        "Агенство":"ООО 'Панорама'",
        "Риэлтор":"Марина Дербикова",
        "Телефон":"+79221245860",
        "Документы":{"Домклик":"-"},
        "Профиль":{"Домклик":"https://agencies.domclick.ru/agent/2928847?region_id=58d15e6b-f489-4e5f-9a98-7036a86f05ab?utm_content=offers.agent"}
    }
}

{
    "aboutFlat":{
                    "Недвижимость":"Комната",
                    "Этаж":"4",
                    "Общая площадь":"650,1",
                    "Жилая площадь":"24,7",
                    "Площадь кухни":"9,5",
                    "Комнат":"1",
                    "Высота потолков":"2,75",
                    "Санузел":"Раздельный",
                    "Ремонт":"Евроремонт"
                },
    "aboutHouse":{
        "Год постройки":"1961",
        "Тип стен":"Панель",
        "Перекрытия":"Железобетонные",
        "Этажность":"5",
        "Парковка":"Во дворе",
        "Подъезды":"1",
        "Лифт":"Пассажирский",
        "Отопление":"Центральное",
        "Аварийность":"Нет",
        "Газоснабжение":"Центральное"
    },
    aboutSeller:{
        "Агенство":"Лабецкий Недвижимость",
        "Риэлтор":{"Домклик":"Даниил Удовицкий","ЦИАН":"Никита Лабецкий"},
        "Телефон":{"Домклик":"+79062585011","ЦИАН":"+7 981 226-82-05"},
        "Документы":{"Домклик":"Сбер ID","ЦИАН":"Паспорт"},
        "Профиль":{"Домклик":"https://agencies.domclick.ru/agent/3716624?region_id=70293348-4ea2-4741-934f-c1e9549d1fba?utm_content=offers.agent","ЦИАН":"https://spb.cian.ru/agents/9447844/"}
    }
}


     */


    val adInfo = ""
    val additionalParam = selectedAd.value!!.additionalParam //"{\"aboutFlat\":{\"Недвижимость\":\"Комната\",\"Этаж\":\"4\",\"ОбщаяПлощадь\":\"650,1\",\"ЖилаяПлощадь\":\"24,7\",\"ПлощадьКухни\":\"9,5\",\"Комнат\":\"1\",\"ВысотаПотолков\":\"2,75\",\"Санузел\":\"Раздельный\",\"ВидИзОкон\":\"-\",\"Ремонт\":\"Евроремонт\"},\"aboutHouse\":{\"ГодПостройки\":\"1961\",\"ТипСтен\":\"Панель\",\"Перекрытия\":\"Железобетонные\",\"Этажность\":\"5\",\"Парковка\":\"ВоДворе\",\"Подъезды\":\"1\",\"Лифт\":\"Пассажирский\",\"Отопление\":\"Центральное\",\"Аварийность\":\"Нет\",\"Газоснабжение\":\"Центральное\"},aboutSeller:{\"Агенство\":\"ЛабецкийНедвижимость\",\"Риэлтор\":{\"Домклик\":\"ДаниилУдовицкий\",\"ЦИАН\":\"НикитаЛабецкий\"},\"Телефон\":{\"Домклик\":\"+79062585011\",\"ЦИАН\":\"+7981226-82-05\"},\"Документы\":{\"Домклик\":\"СберID\",\"ЦИАН\":\"Паспорт\"},\"Профиль\":{\"Домклик\":\"https://agencies.domclick.ru/agent/3716624?region_id=70293348-4ea2-4741-934f-c1e9549d1fba?utm_content=offers.agent\",\"ЦИАН\":\"https://spb.cian.ru/agents/9447844/\"}}}"
    val jsonAP = Gson().fromJson(additionalParam, JsonObject::class.java)
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


            /*
[
            {
   "url":"https://spb.domclick.ru/card/sale__room__2058391139",
   "title":"Комната в 1-комн. квартире, 15 м², 4/5 этаж",
   "travel":"Электросила 14 мин. пешком; Парк Победы 20 мин. пешком; Московские ворота 31 мин. пешком;",
   "location":"Санкт-Петербург, Благодатная улица, 40",
   "price":"2000000",
   "priceInfo":"133333 ₽/м²",
   "caption":"На редкость УЮТНАЯ, ЧИСТАЯ, СВЕТЛАЯ, с прекрасным интерьером, с отличным ремонтом комната. Места общего пользования содержатся в чистоте. В квартире спокойная обстановка, маргинальных личностей в квартире нет. Рядом с домом остановка общественного транспорта. Очень много зелени в большом дворе. Экологически благополучное месторасположение дома. ПРЯМАЯ ПРОДАЖА. СВОБОДНА. НИКТО НЕ ПРОПИСАН. Документы готовы, решены все вопросы с уведомлениями. Моментальный выход на сделку (хоть на следующий после просмотра день). В собственности более 15 лет (приватизация). Сделок с комнатой не было. АГЕНТОВ БЕЗ ПОКУПАТЕЛЕЙ - ПРОСЬБА НЕ ЗВОНИТЬ ! АГЕНТ ЕСТЬ.",
   "img":"https://img.dmclk.ru/s1200x800q80/vitrina/owner/49/20/4920a2547f0949e591e4fb86c5b42148.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/5d/a9/5da9a8d8ff6640aaa0b9e9790781aeda.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp",
   "coordinates":"59.851137 30.028201",
   "publishedDate":"2024-04-15T16:53:16.178316+00:00",
   "updatedDate":"2024-05-17T15:43:06.222219+00:00",
   "hash":"-18060989"
},{
   "url":"https://spb.domclick.ru/card/sale__room__1965656400 https://spb.cian.ru/sale/flat/295533366/",
   "title":"Комната в 1-комн. квартире, 24.7 м², 4/5 этаж",
   "travel":"Ломоносовская 13 мин. пешком; Пролетарская 23 мин. пешком;",
   "location":"Санкт-Петербург, бульвар Красных Зорь, 6",
   "price":"4999999",
   "priceInfo":"Без комиссии, 182 186 ₽/м²",
   "caption":"Добрый день!\n\nПродаем ЕВРО-ДВУХКОМНАТНЫЙ ВАРИАНТ в пешей доступности от станции метро \"ЛОМОНОВСКАЯ\" (по документам доля в праве общей долевой собственности).\nВесь этаж - это одна большая квартира, с одним кадастровым номером.\nЕсть длинный коридор и никаких мест общего пользования на нем.\nУ каждой из комнат - свой отдельный вход и свой санузел внутри.\n\nПо документам - это ДОЛЯ в праве общей долевой собственности на всю КВАРТИРУ, что соответствует определенным помещениям на поэтажном плане.\nТаким образом, фактически, ничем не отличить от квартиры, по документам же - это доля. Подходит под ипотеку Банков (Росбанк и иные, но не сбер).\n\n! Продажа БЕЗ КОМИССИИ !\n\nКстати, если Вы сейчас продаете свою квартиру, то мы можем сразу КУПИТЬ ее.\nБезопасно и с удовольствием юридически сопроводим Вашу сделку, деятельность нашего агентства застрахована на 20 000 000 р.\nБудем рады покупкам с нами и у нас!)\n\nПРО ДОКУМЕНТЫ\n\nСобственники будут присутствовать на сделке ЛИЧНО (а не по доверенности).\nВ собственности более 5 лет.\nБЕЗ ОБРЕМЕНЕНИЙ.\nПодходит под ипотеку Росбанка и иных банков (но не сбер), при необходимости поможем с одобрением на наиболее выгодных условиях.\n\nПо документам это доля в праве общей долевой собственности.\n\nПРО ОБЪЕКТ\n\nДействительно КОМФОРТНАЯ планировка.\n\nОбщая площадь составляет 30.1м2.\nМетраж жилой зоны (комнаты) составляет 15.2 м2.\nМетраж кухни 9.5 м2.\n\nОтделена зона входа, в виде функциональной прихожей, с одной стороны которой оборудован шкаф-купе, с другой санузел и душевая (соответствует плану по ЕГРН).\n\nНаходится на 4 этаже.\nИз окон открывается вид в тихий и зеленый двор.\n\nПРО ДОМ И ИНФРАСТРУКТУРУ\n\nПерекрытия ЖЕЛЕЗОБЕТОННЫЕ.\nПостроен в 1961 году и проверен временем :)\n\nДом находится в НЕВСКОМ РАЙОНЕ.\nВ шаговой доступности МЕТРО ЛОМОНОСОВСКАЯ, остановки общественного наземного транспорта (трамвай, троллейбус, автобус) удобное и быстрое сообщение с ЦЕНТРОМ ГОРОДА.\n\nДо Невского проспекта несколько остановок на метро. Можно добраться в ЛЮБУЮ ТОЧКУ ГОРОДА.\n\nВо дворе всегда можно найти ПАРКОВОЧНОЕ МЕСТО. Развита инфраструктура, есть все необходимое для жизни.\n\nС радостью проконсультируем по сделке.\nТорг уместен, но после просмотра :)\nЗвоните!",
   "img":"https://images.cdn-cian.ru/images/2040069799-1.jpg https://images.cdn-cian.ru/images/2040069824-1.jpg https://images.cdn-cian.ru/images/2040069870-1.jpg https://images.cdn-cian.ru/images/2040069880-1.jpg https://images.cdn-cian.ru/images/2040069886-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg",
   "coordinates":"59.851137 30.028201",
   "publishedDate":"2024-04-15T16:53:16.178316+00:00",
   "updatedDate":"2024-05-17T15:43:06.222219+00:00",
   "hash":"-180609"
},
{
"title":"Комната в 1-комн. квартире, 20 м², 2/5 этаж",
"price":"3500000",
"img":"https://img.dmclk.ru/s1200x800q80/vitrina/owner/b9/6b/b96b9e5c747f4a51a6446692e12641d4.webp",
"caption":"Продается светлая, просторная, теплая и уютная комната площадью 20 кв.м. в малонаселенной трехкомнатной квартире, расположенная на втором этаже 5-ти этажного жилого дома."
},
{
"title":"Комната в 1-комн. квартире, 19.8 м², 5/5 этаж",
"price":"2899999",
"img":"https://img.dmclk.ru/s1200x800q80/vitrina/3a/d1/3ad1d40e5be79bb83a8cc0ffca42e6e7f4e06d39.webp",
"caption":"Добрый день! Продаем СВЕТЛУЮ комнату с ЧИСТОЙ юридической историей в самом сердце Петербурга! Кстати, если Вы сейчас продаёте свою недвижимость, то мы можем сразу КУПИТЬ её:)Безопасно и с удовольствием юридически сопроводим Вашу сделку, деятельность нашего агентства застрахована на 20 000 000 р. Будем рады покупкам с нами и у нас!)"
},
{
"title":"Комната, 11,2 м²",
"price":"1500000",
"img":"https://images.cdn-cian.ru/images/2083734748-1.jpg",
"caption":"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. "
},
{
"title":"Комната, 19/19 м²",
"price":"3800000",
"img":"https://images.cdn-cian.ru/images/2192102279-1.jpg",
"caption":"Продается светлая комната ,с перспективой сделать студию (помещение уже подготовлено для ванной комнаты) , в знаменитом доме им. Полежаева ,где проходили съемки фильма 'Мастер и Маргарита'.Объект культурного наследия региональн"
},
{
"title":"Комната, 24/15,9 м²",
"price":"4890000",
"img":"https://images.cdn-cian.ru/images/2024833861-1.jpg",
"caption":"Предлагаем вашему вниманию уникальное предложение - просторную и уютную комнату в центре Санкт-Петербурга на улице Большая Зеленина, это доля в квартире, в собственность переходит определенная комната. По факту это о"
},
]
            priceInfo


            val location: String? = "indefined_location",
            "

            val address: String? = "undefined_address",
            ""

            name


            description


            */

        AutoSlidingCarousel(
            itemsCount = images.size,//testImg.size,//images.size,
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
            Text(text = /*"4400000"*/(selectedAd.value!!.price!!).toString()  + ' ' + '₽',
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
                    text = /*"Санкт-Петербург, р-н Невский, Рыбацкое, Караваевская ул., 35",*/selectedAd.value!!.address.toString(),//"Звенигородский пр-кт, д 17/1, Санкт-Петербург",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface)
            }
            Column(Modifier.padding(bottom = 16.dp)) {
                //val testSub = "Рыбацкое 10 мин. пешком; Обухово 7 мин. транспортом; Пролетарская 8 мин. транспортом;"
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
                //val testDes = "Продается квартира недалеко от метро Рыбацкое. Квартира теплая, требует ремонта. Парадная  чистая, после ремонта. Хороший зеленый двор с детской площадкой. Район города зеленый, тихий, с хорошей инфраструктурой. Рядом КАД. Один собственник, никто не прописан. Звоните, покажем в удобное для вас время."
                Text(
                    text = selectedAd.value!!.description.toString().replace("n","\n"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,)
            }

            if(jsonAP!= null){
                val dick = mapOf("aboutHouse" to "О доме", "aboutFlat" to "О квартире", "aboutSeller" to "Продавец")
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

                            Text(text = /*"Вторичка"*/aboutFlat["Недвижимость"].toString().toValueFormatt(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }

                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Этаж", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"1"*/aboutFlat["Этаж"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Общая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"25 кв.м"*/aboutFlat["ОбщаяПлощадь"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {

                            Text(text = "Жилая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"17 кв.м"*/aboutFlat["ЖилаяПлощадь"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Площадь кухни", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"5,5 кв.м"*/aboutFlat["ПлощадьКухни"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Комнат", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"1"*/aboutFlat["Комнат"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Высота потолков", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"2,5 м."*/aboutFlat["ВысотаПотолков"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Санузел", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"Совмещенный"*/aboutFlat["Санузел"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Вид из окон", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"Во двор"*/aboutFlat["ВидИзОкон"].toString().toValueFormatt(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    Row(rowModifier){
                        Column(modifier = paramModifier) {
                            Text(text = "Ремонт", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                        Column(modifier = valueModifier) {
                            Text(text = /*"Косметический/Требует ремонта"*/aboutFlat["Ремонт"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                        }
                    }
                    /*

                    "aboutFlat":{
                        "Недвижимость":"",
                        "Этаж":"",
                        "Общая площадь":"",
                        "Жилая площадь":"",
                        "Площадь кухни":"",
                        "Комнат":"",
                        "Высота потолков":"",
                        "Санузел":"",
                        "Вид из окон":"",
                        "Ремонт":"",
                    },
                    "aboutHouse":{
                        "Год постройки":"",
                        "Тип стен":"",
                        "Перекрытия":"",
                        "Этажность":"",
                        "Парковка":"",
                        "Подъезды":"",
                        "Лифт":"",
                        "Отопление":"",
                        "Аварийность":"",
                        "Газоснабжение":"",
                    },
                    aboutSeller:{
                        "<задается на сервере>Агенство":"ИТЕРА", //"Продавец":"Собственник"
                        "<>Риэлтор":"ФИО",
                        "Телефон":"",
                        "Документы":{"ЦИАН":"...", ...},
                        "Профиль":{"ЦИАН":"url", ...}
                    }
                     */
                    /*
                    Row() {
                        Column() {
                            Text(text = "Недвижимость", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Этаж", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Общая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Жилая площадь", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Площадь кухни", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Комнат", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Высота потолков", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Санузел", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Вид из окон", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Ремонт", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                        Column(Modifier.padding(start = 4.dp)){
                            Text(text = "Вторичка", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "1", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "25 кв.м", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "17 кв.м", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "5,5 кв.м", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "1", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "2,5 м.", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Совмещенный", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Во двор", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Косметический/Требует ремонта", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary,)
                        }
                    }
                    */
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
                    /*

                    Row(Modifier
                        .padding(bottom = 60.dp)) {
                        Column() {
                            Text(text = "Год постройки", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Тип стен", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
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
                            Text(text = "Наземная, открытая во дворе", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "3", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Нет", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Центральное", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Нет", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            Text(text = "Центральное", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                    }
                    */
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
                            /*
                            if (seller.size()>1){
                                seller.entrySet().forEach{
                                    Text(text = "${it.key}: ${it.value.toString().toValueFormattUpperCase()}", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                                }
                            }else{
                                Text(text = seller.toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            }
                            */
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
                            /*
                            val phone = aboutSeller["Телефон"].asJsonObject
                            if (phone.size()>1){
                                phone.entrySet().forEach{
                                    Text(text = "${it.key}: ${it.value.toString().toValueFormattUpperCase()}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }else{
                                Text(text = phone.toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            }
                            */
                            //Text(text = aboutFlat["Агенство"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
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
                                //Text(text = aboutSeller["Риэлтор"].toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                                val value = aboutSeller["Документы"].toString()
                                when(value){
                                    "\"-\"" -> Text(text = "(не указано)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary)
                                    "\"нет\"" -> Text(text = "Отсустствуют/Не подтверждены", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error)
                                    else -> Text(text = value.toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            docs?.entrySet()?.forEach{
                                //Text(text = "${it.key}: ${it.value.toString().toValueFormattUpperCase()}", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                                val value = it.value.toString()
                                when(value){
                                    "\"-\"" -> Text(text = "(не указано)", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary)
                                    "\"нет\"" -> Text(text = "Отсустствуют/Не подтверждены", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error)
                                    else -> Text(text = "${it.key}: ${value.toValueFormattUpperCase()}", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            /*
                            val docs = aboutSeller["Документы"].asJsonObject
                            if (docs.size()>1){
                                docs.entrySet().forEach{
                                    //getColoredValue(it)
                                    val value = it.value.toString()
                                    when(value){
                                        "-" -> Text(text = "(не указано)", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary)
                                        "нет" -> Text(text = "Отсустствуют/Не подтверждены", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error)
                                        else -> Text(text = "${it.key}: ${value.toValueFormattUpperCase()}", modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }else{
                                val value = docs.toString()
                                when(value){
                                    "-" -> Text(text = "(не указано)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary)
                                    "нет" -> Text(text = "Отсустствуют/Не подтверждены", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error)
                                    else -> Text(text = value.toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                //Text(text = docs.toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            }
                            */
                            //Text(text = aboutFlat["Агенство"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
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
                            //Text(text = aboutSeller["Агенство"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        }
                    }
                    /*
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
                            Text(text = aboutSeller["Агенство"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                            val seller = aboutSeller["Риэлтор"].asJsonObject
                            if (seller.size()>1){
                                seller.entrySet().forEach{
                                    Text(text = "${it.key}: ${it.value.toString().toValueFormattUpperCase()}", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }else{
                                Text(text = seller.toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            }

                            val phone = aboutSeller["Телефон"].asJsonObject
                            if (phone.size()>1){
                                phone.entrySet().forEach{
                                    Text(text = "${it.key}: ${it.value.toString().toValueFormattUpperCase()}", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }else{
                                Text(text = phone.toString().toValueFormattUpperCase(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            }
                            //Text(text = aboutSeller["Телефон"].toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)

                            Column(Modifier.padding(start = 4.dp)) {
                                aboutSeller["Документы"].asJsonObject.entrySet().forEach {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = it.key, fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                                        TextButton(onClick = {

                                        }) {
                                            Text(text = it.value.toString().toValue(), fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error,)
                                        }
                                    }
                                }
                                /*
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
                                */
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
                    */
                }
            }


        }

    }


}
/*
@Composable
fun getColoredValue(it: MutableMap.MutableEntry<String, JsonElement>) {
    if (it.value.toString() == "-") return Text(text = "(скрыт)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary,)
    else return Text(text = "(скрыт)", fontSize = 16.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.tertiary,)
}
*/
fun String?.toValueFormatt():String{
    try {
        if (this!! == "\"-\"") return this.replace("\"","")
        var str = this!!
        for (match in Regex("[А-Я]").findAll(str)) {
            if(match.range.first != 1){
                str = str.replace(match.value," ${match.value.lowercase()}")
            }else{
                str = str.replace(match.value," ${match.value}")
            }
        }
        //val temp = str.replace("\"","")
        return str.replace("\"","").removeRange(0,1)//.drop(1)

    }catch (e:Exception){
        return "-"
    }
}
fun String?.toValueFormattUpperCase():String{
    try {
        var str = this!!
        for (match in Regex("[А-Я]").findAll(str)) {
            str = str.replace(match.value," ${match.value}")
        }
        //val temp = str.replace("\"","")
        return str.replace("\"","").removeRange(0,1)//.drop(1)

    }catch (e:Exception){
        return "-"
    }
}
fun String?.toValue():String{
    if (this!! == "\"-\"") {
        return this.replace("\"","")
    }
    try {
        return this!!.replace("\"","")
    }catch (e:Exception){
        return ""
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
