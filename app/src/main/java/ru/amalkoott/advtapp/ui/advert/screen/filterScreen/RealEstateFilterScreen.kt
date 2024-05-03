package ru.amalkoott.advtapp.ui.advert.screen.filterScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import ru.amalkoott.advtapp.ui.advert.compose.BinaryFilter
import ru.amalkoott.advtapp.ui.advert.compose.NonnullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.NullableAllFilter
import ru.amalkoott.advtapp.ui.advert.compose.NullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.RangeFilter
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.GeneralSetFilters

@Composable
fun RealEstateFilter(funs: Map<String, (String?) -> Unit>,
                     dealType: MutableState<Boolean>,
                     flatType: MutableState<String>,
                     city: MutableState<String>,
                     travel:MutableState<String?>
                     ) {
// Недвижка
    /// *** основные фильтры ***
    // @TODO снять-купить
    BinaryFilter(firstValue = "Купить", secondValue = "Снять", funs["deal"]!!)

    GeneralSetFilters(functions = funs)
    if (!dealType.value){
        // купить
        SaleFilters(funs["price"]!!)
    }else{
        // снять
        RentFilters(funs["rent"]!!,funs["amenities"]!!,funs["rentFeatures"]!!,)
    }

    GeneralRealEstateFilters(dealType,flatType,travel,funs)

        /*
        funs["living"]!!,funs["minArea"]!!,funs["maxArea"]!!,
        funs["minLArea"]!!,funs["maxLArea"]!!,funs["minKArea"]!!,
        funs["maxKArea"]!!,funs["cell"]!!,funs["minFloors"]!!,
        funs["maxFloors"]!!,funs["roomType"]!!,funs["wall"]!!,
        funs["balcony"]!!,funs["view"]!!,travel,
        funs["travelType"]!!,funs["travelTime"]!!,funs["toilet"]!!,)

         */
}


@Composable
fun GeneralRealEstateFilters(
    dealType: MutableState<Boolean>,
    flatType: MutableState<String>,
    travel: MutableState<String?>,
    funs: Map<String, (String?) -> Unit>
    /*
    setLivingType:(String) -> Unit,
    setMinArea:(String) -> Unit,
    setMaxArea:(String) -> Unit,
    setMinLArea:(String) -> Unit,
    setMaxLArea:(String) -> Unit,
    setMinKArea:(String) -> Unit,
    setMaxKArea:(String) -> Unit,
    setCell:(String) -> Unit,
    setMinFloors:(String) -> Unit,
    setMaxFloors:(String) -> Unit,
    setRoomType:(String) -> Unit,
    setMaterial:(String) -> Unit,
    setBalcony:(String) -> Unit,
    setView:(String) -> Unit,
    travel: MutableState<String?>,
    setTravelType:(String) -> Unit,
    setTravelTime:(String) -> Unit,
    setToilet:(String) -> Unit,
    */
){
    /*
    if (!dealType.value){
        // купить
        SaleFilters(funs["price"]!!)
    }else{
        // снять
        RentFilters(funs["rent"]!!,funs["amenities"]!!,funs["rentFeatures"]!!,)
    }

     */

    when(flatType.value){
        "Вторичка" -> FlatFilters(funs["minFloor"]!!,funs["maxFloor"]!!,funs["floor"]!!,funs["repair"]!!,funs["apart"]!!,funs["parking"]!!,funs["lift"]!!,)
        "Комната" -> FlatFilters(funs["minFloor"]!!,funs["maxFloor"]!!,funs["floor"]!!,funs["repair"]!!,funs["apart"]!!,funs["parking"]!!,funs["lift"]!!,)
        "Новостройка" -> LayoutFilters(funs["finish"]!!,)
        "Дом, дача" -> CountryFilters(funs["repair"]!!,funs["communication"]!!,)
    }



    val livingTypePairs = remember { mutableStateMapOf<String,Boolean>(
        "Вторичка" to true,
        "Новостройка" to false,
        "Комната" to false,
        "Дом, дача" to false,
        "Таунхаус" to false,
        "Участок" to false,
    ) }
// тип: вторичка, новостройка, комната, дом-дача, таунхаус, коттедж, участок (нельзя null)
    NonnullableFilter("Тип недвижимости",livingTypePairs,funs["living"]!!)// setLivingType)

    // метраж - TextField от до, только целые числа
    RangeFilter(name = "Площадь", setMinValue = /*setMinArea*/funs["minArea"]!!, setMaxValue = funs["maxArea"]!!)//setMaxArea)

    // жилая площадь  - TextField от до, только целые числа
    RangeFilter(name = "Жилая площадь", setMinValue =/* setMinLArea*/funs["minLArea"]!!, setMaxValue = funs["maxLArea"]!!)//setMaxLArea)

    // площадь кухни  - TextField от до, только целые числа
    RangeFilter(name = "Площадь кухни", setMinValue = /*setMinKArea*/funs["minKArea"]!!, setMaxValue = funs["maxKArea"]!!)//setMaxKArea)//

    /// *** фильтры "показать еще" ***
    // высота потолков --- non-null

    val cellPairs = remember { mutableStateMapOf<String,Boolean>(
        "2,5" to false,
        "2,7" to false,
        "3" to false,
        "3,5" to false,
        "4" to false
    ) }
    /*
    val cellPairs = remember { mutableStateMapOf<Float,Boolean>(
        2.5f to false,
        2.7f to false,
        3f to false,
        3.5f to false,
        4f to false
    ) }
    */
    NullableFilter("Высота потолков", map = cellPairs, setValue = funs["cell"]!!)//setCell)//

    // этажность дома от-до
    RangeFilter(name = "Этажность дома", setMinValue =/* setMinFloors*/funs["minFloors"]!!, setMaxValue = funs["maxFloors"]!!)//setMaxFloors)//

    // команты: смежные-изолированные non
    val roomType = remember { mutableStateMapOf<String,Boolean>(
        "Смежные" to false,
        "Изолированные" to false
    ) }
    NullableFilter("Тип комнат", map = roomType, setValue = funs["roomType"]!!)// setRoomType)//

    // материал стен: кирпич и тд
    val materialPairs = remember { mutableStateMapOf<String,Boolean>(
        "Кирпичный" to false,
        "Панельный" to false,
        "Блочный" to false,
        "Монолитный" to false,
        "Монолитно-кирпичный" to false,
        "Деревянный" to false,
    ) }
    NullableAllFilter("Материал стен", map = materialPairs, setValue = funs["wall"]!!)//setMaterial)//

    // лоджия-балкон non
    val balcony = remember { mutableStateMapOf<String,Boolean>(
        "Балкон" to false,
        "Лоджия" to false
    ) }
    NullableAllFilter("Балкон или лоджия", map = balcony, setValue =funs["balcony"]!!)// setBalcony)//

    // вид из окон: улица, двор, лес, вода
    val viewPairs = remember { mutableStateMapOf<String,Boolean>(
        "На улицу" to false,
        "Во двор" to false,
        "На парк" to false,
        "На водоем" to false,
    ) }
    NullableAllFilter("Вид из окон", map = viewPairs, setValue = funs["view"]!!)//setView)//

// расстояние до метро  (для спб и мск) - расстояние до центра (остальные города и загород) flat type
    // пешком-транспортом
    val travelType = remember {
        mutableStateMapOf<String,Boolean>(
            "Пешком" to false,
            "Транспортом" to false,
        )
    }
    //val travel = "метро" //@TODO в vm сделать смену Центр/Метро
    NullableFilter("Расстояние до $travel", map = travelType, setValue = funs["travelType"]!!) //setTravelType)//
    val travelTime = remember {
        mutableStateMapOf<String,Boolean>(
            "5" to false,
            "10" to false,
            "15" to false,
            "20" to false,
            "30" to false,
        )
    }
    NonnullableFilter(map = travelTime, setValue =funs["travelTime"]!!) // setTravelTime)//


    //@TODO подвязать туалет под VM
    // санузел: 0-1 (смежный-раздельный, улица-дом) non flat type
    val toilet = remember { mutableStateMapOf<String,Boolean>(
        "Смежный" to false,
        "Раздельный" to false
    ) }
    NullableFilter("Санузел", map = toilet, setValue = funs["toilet"]!!) //setToilet)//

}


@Composable
fun FlatFilters(
    setMinFloor: (String?) -> Unit,
    setMaxFloor: (String?) -> Unit,
    setFloor: (String?) -> Unit,
    setRepair: (String?) -> Unit,
    setApart: (String?) -> Unit,
    setParking: (String?) -> Unit,
    setLift: (String?) -> Unit,

    // эксперимент с аппарт
    //apart: MutableMap<String,Boolean>
)
{
    val floorTypePairs = remember { mutableStateMapOf<String,Boolean>(
        "Не первый" to false,
        "Не последний" to false,
        "Только последний" to false,
    ) }

    // этаж от-до  - TextField от до, только целые числа
    RangeFilter(name = "Этаж", setMinValue = setMinFloor/*funs["minFloor"]!!*/, setMaxValue = setMaxFloor)//funs["maxFloor"]!!)

    // не первый, не последний, только последний (можно null)
    NullableAllFilter("",floorTypePairs,setFloor) //funs["floor"]!!)

    // ремонт: нужен, косметический, евро, дизайн (все, кроме новостроек) (можно null) flat type
    val repairPairs = remember { mutableStateMapOf<String,Boolean>(
        "Косметический" to false,
        "Евро" to false,
        "Дизайнерский" to false,
        "Требуется" to false
    ) }
    NullableAllFilter("Ремонт", map = repairPairs, setValue = setRepair)//funs["repair"]!!)

    // апартаменты: да-нет (только квартиры и комнаты) non flat type
    val apart = remember { mutableStateMapOf<String,Boolean>(
        "Не апартаменты" to false,
        "Только апартаменты" to false
    ) }
    NullableFilter("Тип жилья", map = apart,setValue = setApart)//funs["apart"]!!)

    // парковка: наземка, подземка, многоуровневая flat type
    val parkingPairs = remember { mutableStateMapOf<String,Boolean>(
        "Наземная" to false,
        "Подземная" to false,
        "Многоуровневая" to false,
    ) }
    NullableAllFilter("Парковка", map = parkingPairs, setValue = setParking)//funs["parking"]!!)

    // лифт: легковой-грузовой flat type
    val lift = remember { mutableStateMapOf<String,Boolean>(
        "Пассажирский" to false,
        "Грузовой" to false
    ) }
    NullableAllFilter("Лифт",map = lift, setValue = setLift)//funs["lift"]!!)
}

@Composable
fun CountryFilters(
    setRepair: (String?) -> Unit,
    setCommunication: (String?) -> Unit,
){
    // ремонт: нужен, косметический, евро, дизайн (все, кроме новостроек) (можно null) flat type
    val repairPairs = remember { mutableStateMapOf<String,Boolean>(
        "Косметический" to false,
        "Евро" to false,
        "Дизайнерский" to false,
        "Требуется" to false
    ) }
    NullableAllFilter("Ремонт", map = repairPairs, setValue = setRepair)//funs["repair"]!!)

    // коммуникация: газ, вода, электричество, отопление (только загород) flat type
    val communicationPairs = remember { mutableStateMapOf<String,Boolean>(
        "Газ" to false,
        "Вода" to false,
        "Электричество" to false,
        "Отопление" to false,
    ) }
    NullableAllFilter("Коммуникации", map = communicationPairs, setValue = setCommunication)//funs["communication"]!!)
}

@Composable
fun LayoutFilters(
    setFinish: (String?) -> Unit,
){
    // отделка: нет, черновая, предчистовая, чистовая (только новостройки) (можно null) flat type
    val finishPairs = remember { mutableStateMapOf<String,Boolean>(
        "Черновая" to false,
        "Предчистовая" to false,
        "Чистовая" to false,
        "Нет" to false
    ) }
    NullableAllFilter("Отделка", map = finishPairs, setValue = setFinish)//funs["finish"]!!)
}

@Composable
fun SaleFilters(
    setPriceType:(String?) -> Unit
){
// цена: за все-за м2 (только для покупки) deal type
    BinaryFilter(firstValue = "За все", secondValue = "За м2", setPriceType)//funs["price"]!!)
}

@Composable
fun RentFilters(
    setRentType:(String?) -> Unit,
    setAmenities:(String?) -> Unit,
    setRentFeatures:(String?) -> Unit
){
// съем
    // - город
    // - загород
    // тип съема (посуточно, долго) deal type
    BinaryFilter(firstValue = "Посуточно", secondValue = "Долго", setRentType)// funs["rent"]!!)

    // предоплата, залог, комиссия (только съем) deal type
    val rentFeaturePairs = remember { mutableStateMapOf<String,Boolean>(
        "Без предоплаты" to false,
        "Без залога" to false,
        "Без комиссии" to false,
    ) }
    NullableAllFilter("Особенности аренды", map = rentFeaturePairs, setValue = setRentFeatures)//funs["rentFeatures"]!!)


    // удобства: кондер, холодос и тд (только для съема) deal type
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
    NullableAllFilter("Удобства", map = amenitiesPairs, setValue = setAmenities)//funs["amenities"]!!)

}