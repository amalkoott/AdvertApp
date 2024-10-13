package ru.amalkoott.advtapp.ui.advert.screen.filter

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.amalkoott.advtapp.ui.advert.compose.elements.BinaryFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.DatePickerFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.DropdownAllFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.DropdownFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.NonnullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.NullableAllFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.NullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.RangeFilter
import ru.amalkoott.advtapp.ui.advert.compose.elements.ShowMoreButton
import ru.amalkoott.advtapp.ui.advert.compose.screens.GeneralSetFilters

@Composable
fun RealEstateFilter(funs: Map<String, (String?) -> Unit>,
                     dealType: MutableState<Boolean>,
                     flatType: MutableState<String>,
                     city: MutableState<String>,
                     travel:MutableState<String?>,
                     parameters:Map<String, SnapshotStateMap<String, Boolean>>,
                     roomsText:MutableState<String>
                     ) {
// Недвижка
    /// *** основные фильтры ***
    // снять-купить (deal type)
    BinaryFilter(firstValue = "Купить", secondValue = "Снять", funs["deal"]!!)

    // город, цена
    GeneralSetFilters(functions = funs)

    if (!dealType.value){
        // купить: цена за все\кв.м
        SaleFilters(funs["price"]!!)
        GeneralRealEstateFilters(dealType,flatType,travel,funs, parameters, roomsText)//,isShowMore)
    }else{
        // снять: особенности аренды и доп услуги
        GeneralRealEstateFilters(dealType,flatType,travel,funs, parameters, roomsText)//, isShowMore)
    }

}

@Composable
fun GeneralRealEstateFilters(
    dealType: MutableState<Boolean>,
    flatType: MutableState<String>,
    travel: MutableState<String?>,
    funs: Map<String, (String?) -> Unit>,
    parameters: Map<String, SnapshotStateMap<String, Boolean>>,
    roomsText: MutableState<String>
){
    val isShowMore = remember { mutableStateOf(false) }
    var toiletSet = parameters["toilet"]!!
    var roomSet = parameters["room"]!!
    // тип: вторичка, новостройка, комната, дом-дача, таунхаус, коттедж, участок (нельзя null)
    NonnullableFilter("Тип недвижимости",parameters["livingType"]!!,funs["living"]!!)// setLivingType)

    // метраж - TextField от до, только целые числа
    RangeFilter(name = "Площадь", setMinValue = /*setMinArea*/funs["minArea"]!!, setMaxValue = funs["maxArea"]!!)//setMaxArea)

    when(flatType.value){
        "Вторичка" -> {
            // количество комнат
            DropdownAllFilter(items = roomSet, name = "Комнат в квартире", setCategory = funs["room"]!!, text = roomsText)
            FlatFilters(
                funs["minFloor"]!!,
                funs["maxFloor"]!!,
                funs["floor"]!!,
                funs["repair"]!!,
                funs["apart"]!!,
                funs["parking"]!!,
                funs["lift"]!!,
                parameters["apartment"]!!,
                parameters["repair"]!!,
                parameters["parking"]!!,
                parameters["lift"]!!,
                parameters["floor"]!!,
                isShowMore
            )
        }
        "Комната" -> {
            // количество комнат
            DropdownFilter(items = roomSet.keys.toTypedArray(), name = "Комнат в квартире", setCategory = funs["room"]!!)

            FlatFilters(
                funs["minFloor"]!!,
                funs["maxFloor"]!!,
                funs["floor"]!!,
                funs["repair"]!!,
                funs["apart"]!!,
                funs["parking"]!!,
                funs["lift"]!!,
                parameters["apartment"]!!,
                parameters["repair"]!!,
                parameters["parking"]!!,
                parameters["lift"]!!,
                parameters["floor"]!!,
                isShowMore
            )
        }
        "Новостройка" -> {
            // количество комнат
            DropdownFilter(items = roomSet.keys.toTypedArray(), name = "Комнат в квартире", setCategory = funs["room"]!!)

            LayoutFilters(
                funs["finish"]!!,
                parameters["finish"]!!)
        }
        "Дом, дача" -> {
            toiletSet = parameters["countryToilet"]!!
            roomSet = parameters["countryRoom"]!!
            // количество комнат

            DropdownFilter(items = roomSet.keys.toTypedArray(), name = "Комнат в доме", setCategory = funs["room"]!!)

            CountryFilters(
                funs["repair"]!!,
                funs["communication"]!!,
                parameters["repair"]!!,
                parameters["communications"]!!)
        }
    }

    if (dealType.value){
        RentFilters(
            funs["rent"]!!,
            funs["amenities"]!!,
            funs["rentFeatures"]!!,
            parameters["rentFeatures"]!!,
            parameters["ameneties"]!!,
            isShowMore)
    }

    if (isShowMore.value){
        /// *** фильтры "показать еще" ***
        // жилая площадь  - TextField от до, только целые числа
        RangeFilter(name = "Жилая площадь", setMinValue =/* setMinLArea*/funs["minLArea"]!!, setMaxValue = funs["maxLArea"]!!)//setMaxLArea)

        // площадь кухни  - TextField от до, только целые числа
        RangeFilter(name = "Площадь кухни", setMinValue = /*setMinKArea*/funs["minKArea"]!!, setMaxValue = funs["maxKArea"]!!)//setMaxKArea)//

        // высота потолков --- non-null
        NullableFilter("Высота потолков", map = parameters["cell"]!!, setValue = funs["cell"]!!)//setCell)//

        // этажность дома от-до
        RangeFilter(name = "Этажность дома", setMinValue =/* setMinFloors*/funs["minFloors"]!!, setMaxValue = funs["maxFloors"]!!)//setMaxFloors)//

        // команты: смежные-изолированные non
        NullableFilter("Тип комнат", map = parameters["roomType"]!!, setValue = funs["roomType"]!!)// setRoomType)//

        // материал стен: кирпич и тд
        NullableAllFilter("Материал стен", map = parameters["material"]!!, setValue = funs["wall"]!!)//setMaterial)//

        // лоджия-балкон non
        NullableAllFilter("Балкон или лоджия", map = parameters["balcony"]!!, setValue =funs["balcony"]!!)// setBalcony)//

        // вид из окон: улица, двор, лес, вода
        NullableAllFilter("Вид из окон", map = parameters["view"]!!, setValue = funs["view"]!!)//setView)//

        // расстояние до метро  (для спб и мск) - расстояние до центра (остальные города и загород) flat type
        // пешком-транспортом
        NullableFilter("Расстояние до ${travel.value}", map = parameters["travelType"]!!, setValue = funs["travelType"]!!, Modifier.padding(top = 16.dp)) //setTravelType)//

        NonnullableFilter(map = parameters["travelTime"]!!, setValue =funs["travelTime"]!!, Modifier.padding(bottom = 12.dp)) // setTravelTime)//

        NullableFilter("Санузел", map = toiletSet/*parameters["toilet"]!!*/, setValue = funs["toilet"]!!) //setToilet)//
    }
    ShowMoreButton(isShowMore)
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
    // параметры
    apartment: MutableMap<String,Boolean>,
    repair: MutableMap<String,Boolean>,
    parking: MutableMap<String,Boolean>,
    lifts: MutableMap<String,Boolean>,
    floors: MutableMap<String,Boolean>,
    isShowMore: MutableState<Boolean>
)
{
    // этаж от-до  - TextField от до, только целые числа
    RangeFilter(name = "Этаж", setMinValue = setMinFloor/*funs["minFloor"]!!*/, setMaxValue = setMaxFloor, modifier = Modifier.padding(top = 16.dp))//funs["maxFloor"]!!)

    if (isShowMore.value){
        val floorTypePairs = remember { mutableStateOf(floors) }
        // не первый, не последний, только последний (можно null)
        NullableAllFilter(floorTypePairs,setFloor, Modifier.padding(top = 8.dp, bottom = 16.dp)) //funs["floor"]!!)

        val repairPairs = remember { mutableStateOf(repair) }
        NullableAllFilter("Ремонт", map = repairPairs, setValue = setRepair)//funs["repair"]!!)

        val apart = remember { mutableStateOf(apartment) }
        NullableFilter(name = "Тип жилья", map = apart,setValue = setApart)//funs["apart"]!!)

        val parkingPairs = remember { mutableStateOf(parking) }
        NullableAllFilter("Парковка", map = parkingPairs, setValue = setParking)//funs["parking"]!!)

        val lift = remember { mutableStateOf(lifts)}
        NullableAllFilter("Лифт",map = lift, setValue = setLift)//funs["lift"]!!)
    }
}

@Composable
fun CountryFilters(
    setRepair: (String?) -> Unit,
    setCommunication: (String?) -> Unit,
    repair: MutableMap<String, Boolean>,
    communications: MutableMap<String,Boolean>,
){

    val repairPairs = remember { mutableStateOf(repair) }
    NullableAllFilter("Ремонт", map = repairPairs, setValue = setRepair)//funs["repair"]!!)

    val communicationPairs = remember { mutableStateOf(communications) }
    NullableAllFilter("Коммуникации", map = communicationPairs, setValue = setCommunication)//funs["communication"]!!)
}

@Composable
fun LayoutFilters(
    setFinish: (String?) -> Unit,
    finish: MutableMap<String,Boolean>,
){
    val finishPairs = remember { mutableStateOf(finish) }
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
    setRentFeatures:(String?) -> Unit,
    rentFeatures: MutableMap<String,Boolean>,
    ameneties: MutableMap<String,Boolean>,
    isShowMore: MutableState<Boolean>
){
// съем
    // - город
    // - загород
    // тип съема (посуточно, долго) deal type
    BinaryFilter(firstValue = "Посуточно", secondValue = "Долго", setRentType)// funs["rent"]!!)

    if(true){
        // посуточно
        DatePickerFilter()
    }

    if (isShowMore.value){
        val rentFeaturePairs = remember { mutableStateOf(rentFeatures)    }
        NullableAllFilter("Особенности аренды", map = rentFeaturePairs, setValue = setRentFeatures)//funs["rentFeatures"]!!)

        val amenitiesPairs = remember { mutableStateOf(ameneties) }
        NullableAllFilter("Удобства", map = amenitiesPairs, setValue = setAmenities)//funs["amenities"]!!)
    }

}