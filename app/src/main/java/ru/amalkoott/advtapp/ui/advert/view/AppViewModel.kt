package ru.amalkoott.advtapp.ui.advert.view

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.amalkoott.advtapp.data.remote.SearchParameters
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppUseCase
import java.time.LocalDate
import java.util.Dictionary

// viewModel для общих нужд:
// - настройки пользователя
// - список избранного
//
class AppViewModel(
    private val appUseCase: AppUseCase
): ViewModel() {



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

    val adsMap: MutableMap<Long?,MutableStateFlow<List<Advert>>> = mutableMapOf()
    val search: MutableMap<String,String> = mutableMapOf()
    init{
        //@TODO не забыть при смене схемы БД поменять тип adsetID (внешний ключ у Advert) на Long?

        viewModelScope.launch {
            //appUseCase.fillWithInitialSets(emptyList())
            appUseCase.fillWithInitialSets(sets)

            //appUseCase.loadRemoteNotes()
        }
        viewModelScope.launch{
            appUseCase.notesFlow()
                .collect{
                        note ->
                    adSet.value = note
                }
        }
        sets.forEach{
            adsMap?.put(it.id,MutableStateFlow<List<Advert>>(emptyList()))
            var temp = MutableStateFlow<List<Advert>>(emptyList())
            viewModelScope.launch {
                appUseCase.advertsBySetFlow(it.id!!)
                    .collect{
                            ad -> temp.value = ad
                    }
            }
            adsMap?.set(it.id, temp)
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

        // сохранение отредактированной подборки
        viewModelScope.launch {
            appUseCase.saveSet(set)
        }

        screen_name.value = "Подборки"
        selectedSet.value = null
        edited_set = null
        cancelSearching()
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
            appUseCase.saveSet(selectedSet.value!!)
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
            appUseCase.saveSet(selectedSet.value!!)
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
        flatType.value = ""
        city.value = ""
        travel.value = null
        wc.value = false
    }
    fun setCategory(category: String){
        searching.value!!.category = category
        this.category.value = category
    }
    fun setCity(city: String){
        searching.value!!.city = city
        this.city.value = city
        travel.value = "метро"
    }
    fun setLivingType(type: String){
        searching.value!!.livingType = type
        flatType.value = type
        Log.d("LIVING_TYPE",searching.value!!.livingType.toString())
    }
    fun setDealType(type: String){
        searching.value!!.dealType = type.toBoolean()
        dealType.value = type.toBoolean()
        Log.d("DEAL_TYPE",searching.value!!.dealType.toString())
    }
    fun setPriceType(type: String){
        searching.value!!.priceType = type
        Log.d("PRICE_TYPE",searching.value!!.priceType.toString())
    }
    fun setRentType(type: String){
        searching.value!!.rentType = type
    }
    fun setFloorType(type: String){
        searching.value!!.floorType = type
    }
    fun setMinPrice(value: String){
        searching.value!!.minPrice = value.toFloatOrNull()
    }
    fun setMaxPrice(value: String){
        searching.value!!.maxPrice = value.toFloatOrNull()
    }
    fun setMinArea(value: String){
        searching.value!!.minArea = value.toInt()
    }
    fun setMaxArea(value: String){
        searching.value!!.maxArea = value.toInt()
    }
    fun setMinLArea(value: String){
        searching.value!!.minLArea = value.toInt()
    }
    fun setMaxLArea(value: String){
        searching.value!!.maxLArea = value.toInt()
    }
    fun setMinKArea(value: String){
        searching.value!!.minKArea = value.toInt()
    }
    fun setMaxKArea(value: String){
        searching.value!!.maxLArea = value.toInt()
    }
    fun setMinFloor(value: String){
        searching.value!!.minFloor = value.toInt()
    }
    fun setMaxFloor(value: String){
        searching.value!!.maxFloor = value.toInt()
    }
    fun setMinFloors(value: String){
        searching.value!!.minFloors = value.toInt()
    }
    fun setMaxFloors(value: String){
        searching.value!!.maxFloors = value.toInt()
    }
    fun setRepair(value: String){
        searching.value!!.repair = value
    }
    fun setFinish(value: String){
        searching.value!!.finish = value
    }
    fun setTravelTime(value: String){
        searching.value!!.travelTime = value.toByte()
    }
    fun setTravelType(value: String){
        searching.value!!.travelType = value
        //travel.value = value
    }
    fun setCell(value: String){
        searching.value!!.cell = value
    }
    fun setApart(value: String){
        searching.value!!.apart= value.toBoolean()
    }
    fun setRoomType(value: String){
        searching.value!!.roomType = value.toBoolean()
    }
    fun setRoom(value: String){
        searching.value!!.room = value.toUByte()
    }
    fun setToiletType(value: String){
        searching.value!!.toiletType = value.toBoolean()
        wc.value = value.toBoolean()
    }
    fun setWallMaterial(value: String){
        searching.value!!.wallMaterial = value
    }
    fun setBalconyType(value: String){
        searching.value!!.balconyType = value.toBoolean()
    }
    fun setParking(value: String){
        searching.value!!.parking = value
    }
    fun setLiftType(value: String){
        searching.value!!.liftType = value.toBoolean()
    }
    fun setAmenities(value: String){
        searching.value!!.amenities = value
    }
    fun setView(value: String){
        searching.value!!.view = value
    }
    fun setCommunication(value: String){
        searching.value!!.communication = value
    }
    fun setInclude(value: String){
        searching.value!!.include = value
    }
    fun setExclude(value: String){
        searching.value!!.exclude = value
    }
    fun setRentFeatures(value: String){
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
    fun onSetSelected(set: AdSet?){
        selectedSet.value = set
        screen_name.value = selectedSet.value!!.name.toString()

        val edited_ads: SnapshotStateList<Advert> = selectedSet.value!!.adverts!!.toMutableStateList()
        edited_set = AdSet(name = selectedSet.value!!.name,
            adverts = edited_ads,
            update_interval = selectedSet.value!!.update_interval, caption = null, category = null,
            last_update = selectedSet.value!!.last_update)
    }

    fun onSetChange(name: String, update_interval: Int){
        selectedSet.value!!.name = name
        selectedSet.value!!.update_interval = update_interval
        selectedSet.value!!.last_update = LocalDate.now()
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

}