package ru.amalkoott.advtapp.ui.advert.screen.filterScreen

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import ru.amalkoott.advtapp.ui.advert.compose.BinaryFilter
import ru.amalkoott.advtapp.ui.advert.compose.NonnullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.NullableAllFilter
import ru.amalkoott.advtapp.ui.advert.compose.NullableFilter
import ru.amalkoott.advtapp.ui.advert.compose.RangeFilter
import ru.amalkoott.advtapp.ui.advert.compose.ShowMoreButton
import ru.amalkoott.advtapp.ui.advert.compose.screenCompose.GeneralSetFilters

@Composable
fun RealEstateFilter(funs: Map<String, (String?) -> Unit>,
                     dealType: MutableState<Boolean>,
                     flatType: MutableState<String>,
                     city: MutableState<String>,
                     travel:MutableState<String?>,
                     parameters:Map<String, SnapshotStateMap<String, Boolean>>
                     ) {
// Недвижка
    /// *** основные фильтры ***
    // снять-купить (deal type)
    BinaryFilter(firstValue = "Купить", secondValue = "Снять", funs["deal"]!!)

    // город, цена
    GeneralSetFilters(functions = funs)

    //val isShowMore = remember { mutableStateOf(false) }
    if (!dealType.value){
        // купить: цена за все\кв.м
        SaleFilters(funs["price"]!!)
        GeneralRealEstateFilters(dealType,flatType,travel,funs, parameters)//,isShowMore)
    }else{
        // снять: особенности аренды и доп услуги
        GeneralRealEstateFilters(dealType,flatType,travel,funs, parameters)//, isShowMore)
    }

 //   GeneralRealEstateFilters(flatType,travel,funs, parameters)
}

@Composable
fun GeneralRealEstateFilters(
    dealType: MutableState<Boolean>,
    flatType: MutableState<String>,
    travel: MutableState<String?>,
    funs: Map<String, (String?) -> Unit>,
    parameters: Map<String, SnapshotStateMap<String, Boolean>>,
   // isShowMore: MutableState<Boolean>
){
    // тип: вторичка, новостройка, комната, дом-дача, таунхаус, коттедж, участок (нельзя null)
    NonnullableFilter("Тип недвижимости",parameters["livingType"]!!,funs["living"]!!)// setLivingType)

    // метраж - TextField от до, только целые числа
    RangeFilter(name = "Площадь", setMinValue = /*setMinArea*/funs["minArea"]!!, setMaxValue = funs["maxArea"]!!)//setMaxArea)

    val isShowMore = remember { mutableStateOf(false) }

    when(flatType.value){
        "Вторичка" -> FlatFilters(
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
        "Комната" -> FlatFilters(
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
        "Новостройка" -> LayoutFilters(
            funs["finish"]!!,
            parameters["finish"]!!,
            isShowMore)
        "Дом, дача" -> CountryFilters(
            funs["repair"]!!,
            funs["communication"]!!,
            parameters["repair"]!!,
            parameters["communications"]!!,
            isShowMore)
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
        NullableFilter("Расстояние до ${travel.value}", map = parameters["travelType"]!!, setValue = funs["travelType"]!!) //setTravelType)//

        NonnullableFilter(map = parameters["travelTime"]!!, setValue =funs["travelTime"]!!) // setTravelTime)//

        NullableFilter("Санузел", map = parameters["toilet"]!!, setValue = funs["toilet"]!!) //setToilet)//
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
    RangeFilter(name = "Этаж", setMinValue = setMinFloor/*funs["minFloor"]!!*/, setMaxValue = setMaxFloor)//funs["maxFloor"]!!)

    if (isShowMore.value){
        val floorTypePairs = remember { mutableStateOf(floors) }
        // не первый, не последний, только последний (можно null)
        NullableAllFilter("",floorTypePairs,setFloor) //funs["floor"]!!)

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
    isShowMore: MutableState<Boolean>
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
    isShowMore: MutableState<Boolean>
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

    //val isShowMore = remember { mutableStateOf(false) }

   // ShowMoreButton(isShowMore)

    if (isShowMore.value){
        val rentFeaturePairs = remember { mutableStateOf(rentFeatures)    }
        NullableAllFilter("Особенности аренды", map = rentFeaturePairs, setValue = setRentFeatures)//funs["rentFeatures"]!!)

        val amenitiesPairs = remember { mutableStateOf(ameneties) }
        NullableAllFilter("Удобства", map = amenitiesPairs, setValue = setAmenities)//funs["amenities"]!!)
    }

}