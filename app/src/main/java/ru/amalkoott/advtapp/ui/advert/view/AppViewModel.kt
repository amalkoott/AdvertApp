package ru.amalkoott.advtapp.ui.advert.view

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.di.AppModule
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppUseCase
import ru.amalkoott.advtapp.domain.BlackList
import ru.amalkoott.advtapp.domain.Constants
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor (
    private val appUseCase: AppUseCase
): ViewModel() {
    val sets = mutableStateListOf<AdSet>()
    val adsMap: MutableMap<Long?,MutableStateFlow<List<Advert>>> = mutableMapOf()
    val search: MutableMap<String,String> = mutableMapOf()
    var stateFlowSets = MutableStateFlow<List<AdSet>>(emptyList())
    init{
        viewModelScope.launch {
           // appUseCase.fillWithInitialSets(emptyList())
          //  appUseCase.fillWithInitialSets(sets)

            //appUseCase.loadRemoteNotes()
        }

        viewModelScope.launch{
                appUseCase.setsFlow()
                    .collect{
                             set ->
                        adSet.value = set
                        stateFlowSets.value = set
                    }
        }

        viewModelScope.launch{
            appUseCase.favouritesFlow().collect{
                // todo обработка дубликатов в favourites (возможно лучше просто не выдавать в этом DAO объявления с одинаковыми параметрами) ЛИБО указание в favoutires - к какой подборке принадлежит объявление ЛИБО программно собирать favourites (more optimaly)
                favList.value = it
            }
        }

        viewModelScope.launch{
            appUseCase.blackListFlow().collect{
                // todo обработка дубликатов в favourites (возможно лучше просто не выдавать в этом DAO объявления с одинаковыми параметрами) ЛИБО указание в favoutires - к какой подборке принадлежит объявление ЛИБО программно собирать favourites (more optimaly)
                blackLists.value = it
            }
        }

    }
    val blackLists = MutableStateFlow<List<BlackList>>(emptyList())

    var adSet = MutableStateFlow<List<AdSet>>(emptyList())
    var screen_name = mutableStateOf<String>("Подборки")
    var selectedSet = mutableStateOf<AdSet?>(null )
    var selectedAd = mutableStateOf<Advert?>(null )

    var favourites = mutableStateOf(false)
    var settings = mutableStateOf(false)
    var pushes = mutableStateOf(false)
    var howItWork = mutableStateOf(false)
    var loading = mutableStateOf(false)
    var successfulSearch = mutableStateOf<Boolean?>(null)

    var favs = mutableStateListOf<Advert>()
    //var favList = mutableStateMapOf<String,Advert>()
    var favList = MutableStateFlow<List<Advert>>(emptyList())
    var edited_set: AdSet? = null

    var adverts = mutableStateListOf<Advert>()
    var temp_ads = MutableStateFlow<List<Advert>>(emptyList())

    // LOCAL DATABASE USE
    fun onUpdateSet(){
        // для selected
        viewModelScope.launch {
            appUseCase.updateSetByRemote(selectedSet.value!!,context)
        }
    }
    lateinit var context: Context
    fun setContextValue(value: Context){
        context = value
    }
    fun onEditComplete(){
        val set = selectedSet.value
        if (set == null || set.name!!.isBlank()) return
        // todo search = null: сохранение подборки ретюрнит
        // if (searching.value == null) return

        viewModelScope.launch {

            successfulSearch.value = appUseCase.saveSet(set,searching.value,context) != null

                    // cancelSearching()
            if (successfulSearch.value == true) loading.value = false
           // Log.d("VM searching", "riched...")
        }

        screen_name.value = "Подборки"
        selectedSet.value = null
        edited_set = null

        loading.value = true

        //TODO допилить штуку, что при неудачном поиске смена экрана идет на фильтры с указанными раньше параметрами (чтобы челы заново все не вводили)
    }
    fun onDeleteSet(){
        sets.remove(selectedSet.value)
        viewModelScope.launch {
            appUseCase.removeSet(selectedSet.value!!)
        }

        screen_name.value = "Подборки"
        selectedSet.value = null
        edited_set = null
    }


    // для удаления объявлений из списков (когда объявление еще не выбрано)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAdsCountInSet(id: Long): MutableStateFlow<Int>{
        //var count: Int? = null
        var count = MutableStateFlow<Int>(0)

        viewModelScope.launch {
            appUseCase.getAdvertCount(id).collect{
                count.value = it
            }
        }
        return count
    }

    fun onRemoveAd(advert: Advert){
        adverts = selectedSet.value!!.adverts!!.toMutableStateList()// as SnapshotStateList<Advert>

        try {
            adverts.removeAt(findAdinAdverts(advert.id!!))
            favs.removeAt(findAdinFavs(advert.id!!))

            viewModelScope.launch {
                appUseCase.deleteFavourites(advert.id!!)
            }
        }catch (e:Exception){
        //    Log.d("RemoveFromFavs","Element not found")
        }
        selectedSet.value!!.adverts = adverts
        viewModelScope.launch {
            appUseCase.removeAd(advert)
        }
        viewModelScope.launch {
            appUseCase.saveSet(selectedSet.value!!,searching.value,context)
        }
        selectedAd.value = null
    }
    // для удаления объявления из просмотра (есть selectedAd)
    fun onRemoveAd(){
        adverts = selectedSet.value!!.adverts!!.toMutableStateList()// as SnapshotStateList<Advert>
        try {
            adverts.removeAt(findAdinAdverts(selectedAd.value!!.id!!))
            favs.removeAt(findAdinFavs(selectedAd.value!!.id!!))
            viewModelScope.launch {
                appUseCase.deleteFavourites(selectedAd.value!!.id!!)
            }
        }catch (e:Exception){ //Log.d("RemoveFromFavs","Element not found")
            }

        selectedSet.value!!.adverts = adverts
        screen_name.value = selectedSet.value!!.name.toString()
        viewModelScope.launch {
            appUseCase.removeAd(selectedAd.value!!)
        }
        viewModelScope.launch {
            appUseCase.saveSet(selectedSet.value!!,searching.value,context)
        }
        selectedAd.value = null
    }
    private fun findAdinFavs(value: Long): Int{
        var res: Int = -1
        favs.forEach{
            if (it.id!! == value) res = favs.indexOf(it)
        }
        return res
    }
    private fun findAdinAdverts(value: Long): Int{
        var res: Int = -1
        adverts.forEach{
            if (it.id!! == value) res = adverts.indexOf(it)
        }
        return res
    }
    fun onFavouritesAdd(advert: Advert){
        if(favs.contains(advert)) {

        return
        }
        favs.add(advert)

        if (advert.isFavourite){
            viewModelScope.launch {
                appUseCase.deleteFavourites(advert.id!!)
            }
        }else{
            viewModelScope.launch {
                appUseCase.addFavourites(advert.id!!)
            }
        }


        selectedAd.value = null
        setScreenState("sets")
        screen_name.value = selectedSet.value!!.name.toString()
    }
    fun onDeleteFavourites(advert: Advert){
        if(favs.contains(advert)) favs.remove(advert)


        viewModelScope.launch {
            appUseCase.deleteFavourites(advert.id!!)
           // favList.remove(advert.hash!!)
        }
    }

    fun onRemoveFromBlackList(advert: BlackList){
        viewModelScope.launch {
            appUseCase.removeFromBlackList(advert)
        }
    }

    // SEARCH
    // при клике на новую подборку создается SearchParameters
    // при установлении какого-либо параметра его значение добавляется к searchParameters
    var searching = mutableStateOf<RealEstateSearchParameters?>(null )
    var category = mutableStateOf<String?>("")
    var dealType = mutableStateOf<Boolean>(false)
    var flatType = mutableStateOf<String>("")
    var city = mutableStateOf<String>("")
    var travel = mutableStateOf<String?>("")
    var wc = mutableStateOf<Boolean?>(false)
    fun createSearching(){
        searching.value = RealEstateSearchParameters()
    }
    fun cancelSearching(){
       // Log.d("SEARCHING_VALUE",searching.value.toString())

        parameters.restart()

        loading.value = false
        successfulSearch.value = null

        searching.value = null
        category.value = null
        dealType.value = false
        flatType.value = ""
        city.value = ""
        travel.value = null
        wc.value = false

        setScreenState("main")
    }
    fun setCategory(category: String?){
        searching.value!!.category = category
        this.category.value = category
    }
    fun setCity(city: String?){
        searching.value!!.city = city
        if (city != null) {
            this.city.value = city
        }
        travel.value = "метро"
    }
    fun setLivingType(type: String?){ // not null
        searching.value!!.livingType = type
        if (type != null) {
            flatType.value = type
        }
        // parameters.restartWithLivingType(type) // todo делать только на клик в самом фильтре
    }
    fun setDealType(type: String?){ // not null bool
        searching.value!!.dealType = type//.toBoolean()
        dealType.value = type.toBoolean()

        if(type.toBoolean()) searching.value!!.cancelSale() else searching.value!!.cancelRent()
    }
    fun setPriceType(type: String?){ // not null bool
        searching.value!!.priceType = type
    }
    fun setRentType(type: String?){ // not null bool
        searching.value!!.rentType = type
    }
    fun setFloorType(type: String?){ // null three
        if (type == "Только последний") {
            parameters["realEstate"]!!["floor"]!!.keys.forEach{ parameters["realEstate"]!!["floor"]!![it] = false }
            parameters["realEstate"]!!["floor"]!![type] = true
        }
        searching.value!!.setFloorTypeValue(type!!) // todo чуть чуть поправить
    }
    fun setMinPrice(value: String?){ // null float
        searching.value!!.minPrice = value?.toFloatOrNull()
    }
    fun setMaxPrice(value: String?){ // null float
        searching.value!!.maxPrice = value?.toFloatOrNull()
    }
    fun setMinArea(value: String?){ // null int
        searching.value!!.minArea = value?.toIntOrNull()
    }
    fun setMaxArea(value: String?){ // null int
        searching.value!!.maxArea = value?.toIntOrNull()
    }
    fun setMinLArea(value: String?){ // null int
        searching.value!!.minLArea = value?.toIntOrNull()
    }
    fun setMaxLArea(value: String?){ // null int
        searching.value!!.maxLArea = value?.toIntOrNull()
    }
    fun setMinKArea(value: String?){ // null int
        searching.value!!.minKArea = value?.toIntOrNull()
    }
    fun setMaxKArea(value: String?){
        searching.value!!.maxLArea = value?.toIntOrNull()
    }
    fun setMinFloor(value: String?){
        searching.value!!.minFloor = value?.toIntOrNull()
    }
    fun setMaxFloor(value: String?){
        searching.value!!.maxFloor = value?.toIntOrNull()
    }
    fun setMinFloors(value: String?){
        searching.value!!.minFloors = value?.toIntOrNull()
    }
    fun setMaxFloors(value: String?){
        searching.value!!.maxFloors = value?.toIntOrNull()
    }
    fun setRepair(value: String?){ // null many
        searching.value!!.setRepairValue(value!!)
    }
    fun setFinish(value: String?){ // null many
        searching.value!!.setFinishValue(value!!)
    }
    fun setTravelTime(value: String?){ // not null one
        searching.value!!.travelTime = value?.toByte()
    }
    fun setTravelType(value: String?){ // null bool
        //Log.d("VMTravelType","travel type is $value")
        // если отменяем выбор типа как добраться до метро, то и время обнуляем
        if (value == null){
            searching.value!!.travelTime = null
        }else{
            val result = parameters["realEstate"]!!["travelType"]!![value]
            searching.value!!.travelType = result.toString()
        }

    }
    fun setCell(value: String?){ // null many
        searching.value!!.cell = value
    }
    fun setApart(value: String?){ // null bool
        try {
            val temp = parameters["realEstate"]!!["apartment"]!![value]
            searching.value!!.apart= temp
        //    Log.d("VMLoggingApart","$value is $temp")
        }catch (e:Exception){
          //  Log.d("VMErrorApart",e.message.toString())
            searching.value!!.apart = null
           // Log.d("VMLoggingApart","apart is null")
        }
    }
    fun setRoomType(value: String?){ // null bool
        try {
            val temp = parameters["realEstate"]!!["roomType"]!![value]
            searching.value!!.roomType = temp
         //   Log.d("VMLoggingApart","$value is $temp")
        }catch (e:Exception){
          //  Log.d("VMErrorApart",e.message.toString())
            searching.value!!.roomType = null
           // Log.d("VMLoggingApart","apart is null")
        }

    }
    fun setRoom(value: String?){ // int
        //searching.value!!.setRoomCount(value)
        searching.value!!.setRoomValue(value!!)
        //if(result == null) searching.value!!.room = null else searching.value!!.room = result.toUByte()
    }
    fun setCRoom(value: String?){ // int
        searching.value!!.setRoomValue(value!!)
    }
    fun setToiletType(value: String?){ //null bool
        searching.value!!.toiletType = value.toBoolean()
        wc.value = value.toBoolean()

        //todo предусмотреть смену сортирного словаря на загородный туалет
      //  Log.d("VMToilet","toilet is $value")
    }
    fun setWallMaterial(value: String?){ // null many
        searching.value!!.setWallValue(value!!)
    }
    fun setBalconyType(value: String?){ // null many
        searching.value!!.balconyType = value.toBoolean()
    }
    fun setParking(value: String?){ // null many
    searching.value!!.setParkingValue(value!!)
    }
    fun setLiftType(value: String?){ // null many
        searching.value!!.liftType = value.toBoolean()
    }
    fun setAmenities(value: String?){ // null many
        searching.value!!.setAmenitiesValue(value!!)
    }
    fun setView(value: String?){ // null many
        searching.value!!.setViewValue(value!!)
    }
    fun setCommunication(value: String?){ // null many
        searching.value!!.setCommunicationValue(value!!)
    }
    fun setInclude(value: String?){ // null words
        searching.value!!.include = value
    }
    fun setExclude(value: String?){ // null words
        searching.value!!.exclude = value
    }
    fun setRentFeatures(value: String?){ // null many
        searching.value!!.setRentFeatureValue(value!!)
    }
    fun set(){
    }
    // CLICKS
     fun onAddSetClicked(){
        screen_name.value = "Новая подборка"
        selectedSet.value = AdSet(name = "",adverts = getTestSet(), update_interval = 10, caption = null,/* category = null,*/ last_update = null)
        sets.add(selectedSet.value!!)
        setScreenState("add")
    }
    var viewSets: List<AdSet>? = null
    suspend fun onSetSelected(set: AdSet?){
        setScreenState("sets")
        selectedSet.value = set
        screen_name.value = selectedSet.value!!.name.toString()
        viewSets = adSet.value
        if(!adsMap.contains(set!!.id)){
            adsMap?.put(set.id,MutableStateFlow<List<Advert>>(emptyList()))
            val temp = MutableStateFlow<List<Advert>>(emptyList())
            viewModelScope.launch {
                appUseCase.advertsBySetFlow(set.id!!)
                    .collect{
                            ad -> temp.value = ad
                    }
            }
            adsMap?.set(set.id, temp)
        }

        viewModelScope.launch {
            val edited_ads: SnapshotStateList<Advert> = selectedSet.value!!.adverts!!.toMutableStateList()
            edited_set = AdSet(name = selectedSet.value!!.name,
                adverts = edited_ads,
                update_interval = selectedSet.value!!.update_interval, caption = null, /*category = null,*/
                last_update = selectedSet.value!!.last_update)
        }
    }


     fun onSetChange(name: String, update_interval: Int){
        viewModelScope.launch {
            selectedSet.value!!.name = name
            selectedSet.value!!.update_interval = update_interval
            selectedSet.value!!.last_update = LocalDate.now()
        }
    }

    val blackList = mutableStateOf(false)
    val screenState = mutableStateOf("main")
    private val screens = mutableMapOf<String,Boolean>(
        "settings" to false,
        "favourites" to false,
        "how" to false,
        "pushes" to false,
        "main" to false,
        "sets" to false,
        "add" to false,
        "blackList" to false,
        "advert" to false
    )
    private fun setScreenState(screen: String?){
        screens.keys.forEach{
            screens[it] = false
        }
        try {
            screens[screen!!] = true
            screenState.value = screen
        }catch (e:Exception){
           // Log.d("ScreenState","screen value is $screen (main screen)")
        }
    }
    fun onFavouritesClick(){
        screen_name.value = "Избранное"
        favourites.value = !favourites.value
        setScreenState("favourites")
        selectedAd.value = null
    }

    fun onSettingsClick(){
        screen_name.value = "Настройки"
        settings.value = !settings.value
        setScreenState("settings")
    }
    fun onPushesClick(){
        screen_name.value = "История уведомлений"
        pushes.value = !pushes.value
        setScreenState("pushes")
    }
    fun onBlackListClick(){
        screen_name.value = "Черный список"
        blackList.value = !blackList.value
        setScreenState("blackList")
    }

    fun onHowItWorkClick(){
        screen_name.value = "Как это работает?"
        settings.value = !howItWork.value
        setScreenState("how")
    }
    fun onAdSelected(advert: Advert){
        setScreenState("advert")
        selectedAd.value = advert
        screen_name.value = advert.name.toString()
    }
    fun onBackClick(){
        if(favourites.value && selectedAd.value != null){
            selectedAd.value = null
            screen_name.value = "Избранное"
            setScreenState("favourites")
            return
        }
        if(blackList.value){
            screen_name.value = "Настройки"
            setScreenState("settings")
            blackList.value = false
            return
        }
        if(selectedSet.value!=null){
            // выбрана подборка (новая или имеющаяся)
            if(selectedAd.value != null){
                selectedAd.value = null
                screen_name.value = selectedSet.value!!.name!!
                setScreenState("sets")
                return
            }else{
                if(hasChanged()){ // todo чекнуть чтобы бд читалась только при изменениях
                  //  Log.d("CancelUpdating","it has changes, it was canceled")
                    viewModelScope.launch {
                        appUseCase.setsFlow().collect{
                            adSet.value = it
                        }
                    }
                }
            }

            cancelSearching()
            edited_set = null
            selectedSet.value = null
        }
        favourites.value = false
        setScreenState("main")

        screen_name.value = "Подборки"
    }

    private fun hasChanged(): Boolean{
       // return selectedSet.value?.name == "" ||
         return edited_set?.name != selectedSet.value?.name||
                edited_set?.adverts?.size != selectedSet.value?.adverts?.size||
                edited_set?.update_interval != selectedSet.value?.update_interval
    }

    private fun getTestSet(): List<Advert>{
        return mutableStateListOf<Advert>()
    }

    private fun  Map<String, Map<String, SnapshotStateMap<String, Boolean>>>.restart(){
        this.values.forEach{ it ->
            it.values.forEach{params ->
                params.keys.forEach{
                    params[it] = false
                    if(it == "Вторичка") params[it] = true
                }
            }
        }
    }
    private fun  Map<String, Map<String, SnapshotStateMap<String, Boolean>>>.restartWithLivingType(type: String?){
        this.values.forEach{ it ->
            it.values.forEach{params ->
                params.keys.forEach{
                    params[it] = false
                }
            }
        }
        this["realEstate"]!!["livingType"]!![type!!] = true
    }
    // todo обнулять параметры на false после отмены searching
    var parameters = mapOf(
        "realEstate" to mapOf(
            "apartment" to mutableStateMapOf(
                "Не апартаменты" to false,
                "Только апартаменты" to false
        ),
            "lift" to mutableStateMapOf(
                "Пассажирский" to false,
                "Грузовой" to false
            ),
            "countryRoom" to mutableStateMapOf(
                "1" to false,
                "2" to false,
                "3" to false,
                "4" to false,
                "5" to false,
                "6" to false,
                "7" to false,
                "8" to false,
                "9" to false,
              //  "Больше 9" to false,
              //  "Свободная планировка" to false, // todo добавить даты
            ), // todo разобраться с комнатами и лифт тоже через set
            "room" to mutableStateMapOf(
               // "Студия" to false,
                "1" to false,
                "2" to false,
                "3" to false,
                "4+" to false,
            ),
            "repair" to mutableStateMapOf(
                "Косметический" to false,
                "Евро" to false,
                "Дизайнерский" to false,
                "Требуется" to false
            ),
            "parking" to mutableStateMapOf(
                "Наземная" to false,
                "Подземная" to false,
                "Многоуровневая" to false,
            ),
            "floor" to mutableStateMapOf(
                "Не первый" to false,
                "Не последний" to false,
                "Только последний" to false,
            ),
            "finish" to mutableStateMapOf(
                "Черновая" to false,
                "Предчистовая" to false,
                "Чистовая" to false,
                "Нет" to false
            ),
            "rentFeatures" to mutableStateMapOf(
                "Без предоплаты" to false,
                "Без залога" to false,
                "Без комиссии" to false,),
            "ameneties" to mutableStateMapOf(
                "Кондиционер" to false,
                "Холодильник" to false,
                "Плита" to false,
                "Микроволновка" to false,
                "Стиральная машина" to false,
                "Посудомойка" to false,
                "Телевизор" to false,
                "Водонагреватель" to false
            ),
            "communications" to mutableStateMapOf(
                "Газ" to false,
                "Вода" to false,
                "Электричество" to false,
                "Отопление" to false,
            ),
            "toilet" to mutableStateMapOf(
                "Смежный" to false,
                "Раздельный" to false
            ),
            "countryToilet" to mutableStateMapOf(
                "На улице" to false,
                "В доме" to false
            ),
            "travelTime" to mutableStateMapOf(
                "5" to false,
                "10" to false,
                "15" to false,
                "20" to false,
                "30" to false,
            ),
            "travelType" to mutableStateMapOf(
                "Пешком" to false,
                "Транспортом" to false,
            ),
            "view" to mutableStateMapOf(
                "На улицу" to false,
                "Во двор" to false,
                "На парк" to false,
                "На водоем" to false,),
            "balcony" to mutableStateMapOf(
                "Балкон" to false,
                "Лоджия" to false
            ),
            "material" to mutableStateMapOf(
                "Кирпичный" to false,
                "Панельный" to false,
                "Блочный" to false,
                "Монолитный" to false,
                "Монолитно-кирпичный" to false,
                "Деревянный" to false,
            ),
            "roomType" to mutableStateMapOf(
                "Смежные" to false,
                "Изолированные" to false
            ),
            "cell" to mutableStateMapOf(
                "2,5" to false,
                "2,7" to false,
                "3" to false,
                "3,5" to false,
                "4" to false
            ),
            "livingType" to mutableStateMapOf(
                "Вторичка" to true,
                "Новостройка" to false,
                "Комната" to false,
                "Дом, дача" to false,
                "Таунхаус" to false,
                "Участок" to false,
            ),
            "" to mutableStateMapOf()
        )
    )

}