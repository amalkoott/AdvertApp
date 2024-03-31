package ru.amalkoott.advtapp.ui.advert.view

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
    /*
    val ads = mutableStateListOf<Advert>(
        Advert(0,"квартира 1", "супер квартира", 1.2f,null,null,null, 0),
        Advert(1,"квартира 2", "не очень супер квартира", 6.2f,null,null,null, 0),
        Advert(2,"квартира 3", "бомж хата", 0.2f,null,null,null, 0),
        Advert(3,"ауди 1", "ну это пушка, это бомба", 3.4f,null,null,null, 1),
        Advert(4,"бэмвэ 1", "аджара гуджу", 6.6f,null,null,null, 1),
        Advert(5,"камри 3.5", "любви достойна только мать", 3.5f,null,null,null, 1),
        Advert(6,"кило помэло", "покупайте", 0.1f,null,null,null, 2),
        Advert(7,"манго", "желтые манго очень вкусные", 0.2f,null,null,null, 2),
        Advert(8,"личики", "описание личиков", 0.3f,null,null,null, 2)
    )
    */
    val adsMap: MutableMap<Long?,MutableStateFlow<List<Advert>>> = mutableMapOf()

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
        val set = selectedSet.value
        if (set == null || set.name!!.isBlank()) return

        // сохранение отредактированной подборки
        viewModelScope.launch {
            appUseCase.saveSet(set)
        }

        screen_name.value = "Подборки"
        selectedSet.value = null
        edited_set = null
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

    // CLICKS
    /*
    fun testView(set: AdSet?){
        viewModelScope.launch {
            appUseCase.advertsBySetFlow(set?.id!!)
                .collect{
                        ad ->
                    temp_ads.value = ad
                }
        }
    }
    
     */
    fun onSetSelected(set: AdSet?){
        selectedSet.value = set
        screen_name.value = selectedSet.value!!.name.toString()

        val edited_ads: SnapshotStateList<Advert> = selectedSet.value!!.adverts!!.toMutableStateList()
        edited_set = AdSet(name = selectedSet.value!!.name,
            adverts = edited_ads,
            update_interval = selectedSet.value!!.update_interval, caption = null, category = null,
            last_update = selectedSet.value!!.last_update)
    }
    fun onAddSetClicked(){
        screen_name.value = "Новая подборка"
        selectedSet.value = AdSet(name = "",adverts = getTestSet(), update_interval = 10, caption = null, category = null, last_update = null)
        sets.add(selectedSet.value!!)
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