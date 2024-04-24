package ru.amalkoott.advtapp.ui.advert.screen.filterScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import ru.amalkoott.advtapp.ui.advert.compose.BinaryFilter
import ru.amalkoott.advtapp.ui.advert.compose.NonnullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.NullableAllFilter
import ru.amalkoott.advtapp.ui.advert.compose.NullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.RangeFilter

@Composable
fun RealEstateFilter(funs: Map<String, (String) -> Unit>) {
// Недвижка
    /// *** основные фильтры ***
    // снять-купить

    val livingTypePairs = remember { mutableStateMapOf<String,Boolean>(
        "Вторичка" to true,
        "Новостройка" to false,
        "Комната" to false,
        "Дом, дача" to false,
        "Таунхаус" to false,
        "Участок" to false,
    ) }
    val floorTypePairs = remember { mutableStateMapOf<String,Boolean>(
        "Не первый" to false,
        "Не последний" to false,
        "Только последний" to false,
    ) }

    BinaryFilter(firstValue = "Купить", secondValue = "Снять", funs["deal"]!!)
    // тип: вторичка, новостройка, комната, дом-дача, таунхаус, коттедж, участок (нельзя null)
    NonnullableFilter("Тип недвижимости",livingTypePairs, funs["living"]!!)


    //@TODO прописать реализации в VM
    // метраж - TextField от до, только целые числа
    RangeFilter(name = "Площадь", setMinValue = funs["minArea"]!!, setMaxValue = funs["maxArea"]!!)

    // жилая площадь  - TextField от до, только целые числа
    RangeFilter(name = "Жилая площадь", setMinValue = funs["minLArea"]!!, setMaxValue = funs["maxLArea"]!!)

    // площадь кухни  - TextField от до, только целые числа
    RangeFilter(name = "Площадь кухни", setMinValue = funs["minKArea"]!!, setMaxValue = funs["maxKArea"]!!)

    // этаж от-до  - TextField от до, только целые числа
    RangeFilter(name = "Этаж", setMinValue = funs["minFloor"]!!, setMaxValue = funs["maxFloor"]!!)

    // не первый, не последний, только последний (можно null)
    NullableAllFilter("",floorTypePairs, funs["floor"]!!)


    // тип съема (посуточно, долго)
    BinaryFilter(firstValue = "Посуточно", secondValue = "Долго", funs["rent"]!!)

    // цена: за все-за м2 (только для покупки)
    BinaryFilter(firstValue = "За все", secondValue = "За м2", funs["price"]!!)


    // ремонт: нужен, косметический, евро, дизайн (все, кроме новостроек) (можно null)
    val repairPairs = remember { mutableStateMapOf<String,Boolean>(
        "Косметический" to false,
        "Евро" to false,
        "Дизайнерский" to false,
        "Требуется" to false
    ) }
    NullableAllFilter("Ремонт", map = repairPairs, setValue = funs["repair"]!!)

    // отделка: нет, черновая, предчистовая, чистовая (только новостройки) (можно null)
    val finishPairs = remember { mutableStateMapOf<String,Boolean>(
        "Черновая" to false,
        "Предчистовая" to false,
        "Чистовая" to false,
        "Нет" to false
    ) }
    NullableAllFilter("Отделка", map = finishPairs, setValue = funs["finish"]!!)

    // расстояние до метро  (для спб и мск) - расстояние до центра (остальные города и загород)
    // пешком-транспортом
    val travelType = remember {
        mutableStateMapOf<String,Boolean>(
            "Пешком" to false,
            "Транспортом" to false,
        )
    }
    val travel = "метро"
    NullableFilter("Расстояние до $travel", map = travelType, setValue = funs["travelType"]!!)
    val travelTime = remember {
        mutableStateMapOf<String,Boolean>(
            "5" to false,
            "10" to false,
            "15" to false,
            "20" to false,
            "30" to false,
        )
    }
    NonnullableFilter(map = travelTime, setValue = funs["travelTime"]!!)

    /// *** фильтры "показать еще" ***
    // высота потолков --- non-null
    val cellPairs = remember { mutableStateMapOf<String,Boolean>(
        "2,5" to false,
        "2,7" to false,
        "3" to false,
        "3,5" to false,
        "4" to false
    ) }
    NullableFilter("Высота потолков", map = cellPairs, setValue = funs["cell"]!!)

    // этажность дома от-до
    RangeFilter(name = "Этажность дома", setMinValue = funs["minFloors"]!!, setMaxValue = funs["maxFloors"]!!)

    // апартаменты: да-нет (только квартиры и комнаты) non
    val apart = remember { mutableStateMapOf<String,Boolean>(
        "Не апартаменты" to false,
        "Только апартаменты" to false
    ) }
    NullableFilter("Тип жилья", map = apart,setValue = funs["apart"]!!)

    // команты: смежные-изолированные non
    val roomType = remember { mutableStateMapOf<String,Boolean>(
        "Смежные" to false,
        "Изолированные" to false
    ) }
    NullableFilter("Тип комнат", map = roomType, setValue = funs["roomType"]!!)

    // санузел: 0-1 (смежный-раздельный, улица-дом) non
    val toilet = remember { mutableStateMapOf<String,Boolean>(
        "Смежный" to false,
        "Раздельный" to false
    ) }
    NullableFilter("Санузел", map = toilet, setValue = funs["toilet"]!!)

    // материал стен: кирпич и тд
    val materialPairs = remember { mutableStateMapOf<String,Boolean>(
        "Кирпичный" to false,
        "Панельный" to false,
        "Блочный" to false,
        "Монолитный" to false,
        "Монолитно-кирпичный" to false,
        "Деревянный" to false,
    ) }
    NullableAllFilter("Материал стен", map = materialPairs, setValue = funs["wall"]!!)

    // лоджия-балкон non
    val balcony = remember { mutableStateMapOf<String,Boolean>(
        "Балкон" to false,
        "Лоджия" to false
    ) }
    NullableAllFilter("Балкон или лоджия", map = balcony, setValue = funs["balcony"]!!)

    // парковка: наземка, подземка, многоуровневая
    val parkingPairs = remember { mutableStateMapOf<String,Boolean>(
        "Наземная" to false,
        "Подземная" to false,
        "Многоуровневая" to false,
    ) }
    NullableAllFilter("Парковка", map = parkingPairs, setValue = funs["parking"]!!)

    // лифт: легковой-грузовой
    val lift = remember { mutableStateMapOf<String,Boolean>(
        "Пассажирский" to false,
        "Грузовой" to false
    ) }
    NullableAllFilter("Лифт",map = lift, setValue = funs["lift"]!!)

    // удобства: кондер, холодос и тд (только для съема)
    val amenitiesPairs = remember { mutableStateMapOf<String,Boolean>(
        "Кондиционер" to false,
        "Холодильник" to false,
        "Плита" to false,
        "Микроволновка" to false,
        "Стиральная машина" to false,
        "Посудомойка" to false,
        "Телевизор" to false,
        "Водонагреватель" to false
    ) }
    NullableAllFilter("Удобства", map = amenitiesPairs, setValue = funs["amenities"]!!)

    // вид из окон: улица, двор, лес, вода
    val viewPairs = remember { mutableStateMapOf<String,Boolean>(
        "На улицу" to false,
        "Во двор" to false,
        "На парк" to false,
        "На водоем" to false,
    ) }
    NullableAllFilter("Вид из окон", map = viewPairs, setValue = funs["view"]!!)

    // коммуникация: газ, вода, электричество, отопление (только загород)
    val communicationPairs = remember { mutableStateMapOf<String,Boolean>(
        "Газ" to false,
        "Вода" to false,
        "Электричество" to false,
        "Отопление" to false,
    ) }
    NullableAllFilter("Коммуникации", map = communicationPairs, setValue = funs["communication"]!!)

    // предоплата, залог, комиссия (только съем)
    val rentFeaturePairs = remember { mutableStateMapOf<String,Boolean>(
        "Без предоплаты" to false,
        "Без залога" to false,
        "Без комиссии" to false,
    ) }
    NullableAllFilter("Особенности аренды", map = rentFeaturePairs, setValue = funs["rentFeatures"]!!)

}