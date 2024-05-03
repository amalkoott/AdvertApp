package ru.amalkoott.advtapp.ui.advert.view

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.amalkoott.advtapp.data.remote.SearchParameters
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppUseCase
import java.time.LocalDate

// viewModel для общих нужд:
// - настройки пользователя
// - список избранного
//
class AppViewModel(
    private val appUseCase: AppUseCase
): ViewModel() {

    val sets = mutableStateListOf<AdSet>()
/*
    val sets = mutableStateListOf<AdSet>(
        AdSet(
            0,
            "Квартиры",
            mutableStateListOf<Advert>(
                Advert(0,"Квартира 1-комн", "Эта однокомнатная квартира идеально подходит для тех, кто ценит удобство и функциональность. Просторная гостиная, уютная спальня и функциональная кухня обеспечивают комфортное проживание для одного человека или молодой семьи.", 1.2f,"Звенигородский пр-кт, д.16, Санкт-Петербург",null,null, 0),
                Advert(1,"квартира-студия", "Современная студия с оптимальным использованием пространства, идеально подходящая для одинокого жителя или молодой пары. Открытая планировка создает ощущение простора, а большие окна пропускают массу естественного света, делая проживание комфортным и уютным.", 6.2f,"ул. Мебельная, д. 24, Санкт-Петербург",null,null, 0),
                Advert(2,"Просторная квартира 3-комн", "Срочная продажа! Продаю трехкомнатную квартиру в центре спального района. Состояние хорошее, как на фото. По всем остальным вопросам - в чат.", 0.2f,"Российский пр-кт, д.14, Санкт-Петербург",null,null, 0)
            ),
            1,null,null,
            LocalDate.now()

        ),
        AdSet(
            1,
            "Машины",
            mutableStateListOf<Advert>(
                Advert(3,"Audi A5 2009", "Ласточка Audi A5, уход был лучше, чем за собственным ребенком. Состояние на фото, покрасок не было. Продаю в связи покупкой нового автомобиля", 3.4f,null,null,null, 1),
                Advert(4,"BMW 5 2.5T", "Косяки на фото. Двигатель и акпп в норме. Ошибки по абс и асц. Торг.", 6.6f,null,null,null, 1),
                Advert(5,"Chevrolet Lacetti", "Продaм CНЕVROLЕТ LАСЕTТI XЕЧБЕК 2008 г.э., 2 xoзяинa, ПТС OPИГИHAЛ, БEЗ ДTП в родной краcке, c POДНЫМ ПРОБЕГОМ. Машина в отличном состоянии, салон ухоженный, в машине не курили. До 2017 года без зимней эксплуатации. ", 3.5f,null,null,null, 1),
                Advert(5,"Chevrolet НИВА", "Вездеходная НИВА-спорт 2014 года,  БEЗ ДTП, в родной краcке, один хозяин, ПТС OPИГИHAЛ, c POДНЫМ ПРОБЕГОМ. Машина в отличном состоянии, салон ухоженный, в машине не курили. До 2017 года без зимней эксплуатации. ", 3.5f,null,null,null, 1)
            ),
            12,null,null,
            LocalDate.now()
        ),
        AdSet(
            2,
            "Фрукты",
            mutableStateListOf<Advert>(
                Advert(6,"Помело 10 кг", "Спелые сочные помело прямиком из Азии! Привоз - середина марта 2024 года.", 0.1f,null,null,null, 2),
                Advert(7,"Фрукты с доставкой", "В наличии свежие Фрукты, Ягоды, Овощи. Доставка по Саратову и Энгельсу / самовывоз Можете заказать на caйтe (видно на фото). Оплаты там нет. Вы просто отправляете нам заявку и мы связываемся с Вами.", 0.2f,null,null,null, 2),
                Advert(8,"Личи оптом", "описание личиков", 0.3f,null,null,null, 2)
            ),
            3,null,null,
            LocalDate.now()
        )
    )
    */
    val adsMap: MutableMap<Long?,MutableStateFlow<List<Advert>>> = mutableMapOf()
    val search: MutableMap<String,String> = mutableMapOf()
    init{
        //@TODO не забыть при смене схемы БД поменять тип adsetID (внешний ключ у Advert) на Long?

        viewModelScope.launch {
            appUseCase.fillWithInitialSets(emptyList())
          //  appUseCase.fillWithInitialSets(sets)



            //appUseCase.loadRemoteNotes()
        }
        viewModelScope.launch{
            appUseCase.notesFlow()
                .collect{
                        note ->
                    adSet.value = note
                }
        }

        if (sets.isNotEmpty()){
            sets.forEach{
                adsMap?.put(it.id,MutableStateFlow<List<Advert>>(emptyList()))
                val temp = MutableStateFlow<List<Advert>>(emptyList())
                viewModelScope.launch {
                    appUseCase.advertsBySetFlow(it.id!!)
                        .collect{
                                ad -> temp.value = ad
                        }
                }
                adsMap?.set(it.id, temp)
            }
        }
    }
    var adSet = MutableStateFlow<List<AdSet>>(emptyList())
    var screen_name = mutableStateOf<String>("Подборки")
    var selectedSet = mutableStateOf<AdSet?>(null )
    var selectedAd = mutableStateOf<Advert?>(null )

    var favourites = mutableStateOf(false)
    var settings = mutableStateOf(false)

    var favs = mutableStateListOf<Advert>()

    var edited_set: AdSet? = null

    //@TODO здесь было просто мутбл стэйт
    var adverts = mutableStateListOf<Advert>()
    var temp_ads = MutableStateFlow<List<Advert>>(emptyList())

    // LOCAL DATABASE USE
    // помечает, что редактирование закончено -> нет выбранных заметок
    fun onEditComplete(){
        //@TODO сначала передать searching на сервер,а затем обнулить его
        val set = selectedSet.value
        if (set == null || set.name!!.isBlank()) return
        if (searching.value == null) return

        // сохранение отредактированной подборки ** было launch


        viewModelScope.launch {
            appUseCase.saveSet(set,searching.value)
        }

        // Обновлять объявления???


        screen_name.value = "Подборки"
        selectedSet.value = null
        edited_set = null
        cancelSearching()
    }
    private suspend fun test(set: AdSet) = coroutineScope {
        val id = async { appUseCase.saveSet(set,searching.value) }
        println("message: ${id.await()}")
        println("Program has finished with id")

        val message: Deferred<String> = async{ getMessage()}
        println("message: ${message.await()}")
        println("Program has finished")
    }
    suspend fun getMessage() : String{
        delay(500L)  // имитация продолжительной работы
        return "Hello"
    }
    private suspend fun saveSetAndGetID(set: AdSet): Long? = coroutineScope{
        val id = async {   appUseCase.saveSet(set, searching.value) }
        return@coroutineScope id.await()
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
    fun onRemoveAd(advert: Advert){
        adverts = selectedSet.value!!.adverts!!.toMutableStateList()// as SnapshotStateList<Advert>

        try {
            adverts.removeAt(findAdinAdverts(advert.id!!))
            favs.removeAt(findAdinFavs(advert.id!!))
        }catch (e:Exception){ Log.d("RemoveFromFavs","Element not found")}

        selectedSet.value!!.adverts = adverts
        viewModelScope.launch {
            appUseCase.removeAd(advert)
        }
        viewModelScope.launch {
            appUseCase.saveSet(selectedSet.value!!,searching.value)
        }
        selectedAd.value = null
    }
    // для удаления объявления из просмотра (есть selectedAd)
    fun onRemoveAd(){
        adverts = selectedSet.value!!.adverts!!.toMutableStateList()// as SnapshotStateList<Advert>
        try {
            adverts.removeAt(findAdinAdverts(selectedAd.value!!.id!!))
            favs.removeAt(findAdinFavs(selectedAd.value!!.id!!))
        }catch (e:Exception){ Log.d("RemoveFromFavs","Element not found")}

        selectedSet.value!!.adverts = adverts
        screen_name.value = selectedSet.value!!.name.toString()
        viewModelScope.launch {
            appUseCase.removeAd(selectedAd.value!!)
        }
        viewModelScope.launch {
            appUseCase.saveSet(selectedSet.value!!,searching.value)
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
        if(favs.contains(advert)) return
        favs.add(advert)
        selectedAd.value = null
        screen_name.value = selectedSet.value!!.name.toString()
    }
    fun onDeleteFavourites(advert: Advert){
        if(favs.contains(advert)) favs.remove(advert)
    }

    // SEARCH
    // при клике на новую подборку создается SearchParameters
    // при установлении какого-либо параметра его значение добавляется к searchParameters
    var searching = mutableStateOf<SearchParameters?>(null )
    var category = mutableStateOf<String?>("")
    var dealType = mutableStateOf<Boolean>(false)
   // var dealType = mutableStateOf<String>("")
    var flatType = mutableStateOf<String>("")
    var city = mutableStateOf<String>("")
    var travel = mutableStateOf<String?>("")
    var wc = mutableStateOf<Boolean?>(false)
    fun createSearching(){
        searching.value = SearchParameters()
    }
    fun cancelSearching(){
        searching.value = null
        category.value = null
        dealType.value = false
        //dealType.value = ""
        flatType.value = ""
        city.value = ""
        travel.value = null
        wc.value = false
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
        //Log.d("LIVING_TYPE",searching.value!!.livingType.toString())
    }
    fun setDealType(type: String?){ // not null bool
        //var result: Boolean? = null
        //if (type != null) result = type.toBoolean()

        searching.value!!.dealType = type//.toBoolean()
        dealType.value = type.toBoolean()

       // Log.d("DEAL_TYPE",searching.value!!.dealType.toString())
    }
    fun setPriceType(type: String?){ // not null bool
        searching.value!!.priceType = type
       // Log.d("PRICE_TYPE",searching.value!!.priceType.toString())
    }
    fun setRentType(type: String?){ // not null bool
        searching.value!!.rentType = type
    }
    fun setFloorType(type: String?){ // null three
        searching.value!!.floorType = type
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
        searching.value!!.repair = value
    }
    fun setFinish(value: String?){ // null many
        searching.value!!.finish = value
    }
    fun setTravelTime(value: String?){ // not null one
        searching.value!!.travelTime = value?.toByte()
        /*
        if (value == null){
            searching.value!!.travelType = null
            Log.d("VMTravelTypeFromTime","type is ${searching.value!!.travelType}")
        }

         */
    }
    fun setTravelType(value: String?){ // null bool
        //travel.value = value
        Log.d("VMTravelType","travel type is $value")
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
            Log.d("VMLoggingApart","$value is $temp")
        }catch (e:Exception){
            Log.d("VMErrorApart",e.message.toString())
            searching.value!!.apart = null
            Log.d("VMLoggingApart","apart is null")
        }
    }
    fun setRoomType(value: String?){ // null bool
        try {
            //searching.value!!.roomType = value.toBoolean()
            val temp = parameters["realEstate"]!!["roomType"]!![value]
            searching.value!!.roomType = temp
            Log.d("VMLoggingApart","$value is $temp")
        }catch (e:Exception){
            Log.d("VMErrorApart",e.message.toString())
            //searching.value!!.apart = null
            searching.value!!.roomType = null
            Log.d("VMLoggingApart","apart is null")
        }

    }
    fun setRoom(value: String?){ // int
        searching.value!!.room = value?.toUByte()
    }
    fun setToiletType(value: String?){ //null bool
        searching.value!!.toiletType = value.toBoolean()
        wc.value = value.toBoolean()

        /*//todo предусмотреть смену сортирного словаря на загородный туалет
        try {
            //searching.value!!.roomType = value.toBoolean()
            val temp = parameters["realEstate"]!!["roomType"]!![value]
            searching.value!!.roomType = temp
            Log.d("VMLoggingApart","$value is $temp")
        }catch (e:Exception){
            Log.d("VMErrorApart",e.message.toString())
            //searching.value!!.apart = null
            searching.value!!.roomType = null
            Log.d("VMLoggingApart","apart is null")
        }

         */

        Log.d("VMToilet","toilet is $value")
    }
    fun setWallMaterial(value: String?){ // null many
        searching.value!!.wallMaterial = value
    }
    fun setBalconyType(value: String?){ // null many
        searching.value!!.balconyType = value.toBoolean()
    }
    fun setParking(value: String?){ // null many
        searching.value!!.parking = value
    }
    fun setLiftType(value: String?){ // null many
        searching.value!!.liftType = value.toBoolean()
    }
    fun setAmenities(value: String?){ // null many
        searching.value!!.amenities = value
    }
    fun setView(value: String?){ // null many
        searching.value!!.view = value
    }
    fun setCommunication(value: String?){ // null many
        searching.value!!.communication = value
    }
    fun setInclude(value: String?){ // null words
        searching.value!!.include = value
    }
    fun setExclude(value: String?){ // null words
        searching.value!!.exclude = value
    }
    fun setRentFeatures(value: String?){ // null many
        searching.value!!.rentFeature = value
    }
    fun set(){
    }
    // CLICKS
     fun onAddSetClicked(){
        screen_name.value = "Новая подборка"
        selectedSet.value = AdSet(name = "",adverts = getTestSet(), update_interval = 10, caption = null, category = null, last_update = null)
        sets.add(selectedSet.value!!)
        //searching = SearchParameters()
    }
    suspend fun onSetSelected(set: AdSet?){
        selectedSet.value = set
        screen_name.value = selectedSet.value!!.name.toString()

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
                update_interval = selectedSet.value!!.update_interval, caption = null, category = null,
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
    fun onFavouritesClick(){
        screen_name.value = "Избранное"
        favourites.value = !favourites.value
    }

    fun onSettingsClick(){
        screen_name.value = "Настройки"
        settings.value = !settings.value
    }
    fun onAdSelected(advert: Advert){
        selectedAd.value = advert
        screen_name.value = advert.name.toString()
    }
    fun onBackClick(){
        if (selectedAd.value != null) {
            selectedAd.value = null
            if(selectedSet.value != null){
                screen_name.value = selectedSet.value!!.name.toString()
            }else{
                if (favourites.value) screen_name.value = "Избранное"
                else screen_name.value = "Подборки"
            }
        }else{
            // 2 случая возврата:
            // начали создавать новую подборку и решили вернуться
            // при удалении объявлений, если мы нажимаем назад, то мы НЕ СОХРАНЯЕМ ИЗМЕНЕНИЯ!!!
            // чтобы сохранить изменения, нужно нажать галочку!!!

            if(!likeBefore()){
                sets.remove(selectedSet.value)
                if (edited_set != null){
                    sets.add(edited_set!!)
                }
            }

            // если edited_set (то, что было при выборе) отличается от selected_set (текущее состояние)
            // то изменения не сохраняются -> remove(selected_set) и add(edited_set)
            edited_set = null
            selectedSet.value = null
            favourites.value = false
            cancelSearching()

            settings.value = false
            screen_name.value = "Подборки"
        }
    }

    private fun likeBefore(): Boolean{
        if(selectedSet.value?.name == "" ||
            edited_set?.name != selectedSet.value?.name||
            edited_set?.adverts?.size != selectedSet.value?.adverts?.size||
            edited_set?.update_interval != selectedSet.value?.update_interval
        )
            return false
        else return true
    }
    private fun getTestSet(): List<Advert>{
        return mutableStateListOf<Advert>()
    }

    val parameters = mapOf(
        "realEstate" to mapOf(
            "apartment" to mutableStateMapOf(
                "Не апартаменты" to false,
                "Только апартаменты" to false
        ),
            "lift" to mutableStateMapOf(
                "Пассажирский" to false,
                "Грузовой" to false
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