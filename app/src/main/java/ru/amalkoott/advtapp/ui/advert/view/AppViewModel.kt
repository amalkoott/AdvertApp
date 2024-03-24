package ru.amalkoott.advtapp.ui.advert.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
    init{
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
    }
    var adSet = MutableStateFlow<List<AdSet>>(emptyList())

    var screen_name = mutableStateOf<String>("Подборки")
    var selectedSet = mutableStateOf<AdSet?>(null )
    var selectedAd = mutableStateOf<Advert?>(null )

    var favourites = mutableStateOf(false)
    var settings = mutableStateOf(false)

    var favs = mutableStateListOf<Advert>()


    var edited_set: AdSet? = null

    var adverts = mutableStateListOf<Advert>()
    val sets = mutableStateListOf<AdSet>(
        AdSet(
            "Квартиры",
            mutableStateListOf<Advert>(
                Advert("квартира 1", "супер квартира", 1.2f,null,null,null, 0),
                Advert("квартира 2", "не очень супер квартира", 6.2f,null,null,null, 0),
                Advert("квартира 3", "бомж хата", 0.2f,null,null,null, 0)
            ),
            6,null,null,
            LocalDate.now()

        ),
        AdSet(
            "Машины",
            mutableStateListOf<Advert>(
                Advert("ауди 1", "ну это пушка, это бомба", 3.4f,null,null,null, 1),
                Advert("бэмвэ 1", "аджара гуджу", 6.6f,null,null,null, 1),
                Advert("камри 3.5", "любви достойна только мать", 3.5f,null,null,null, 1)
            ),
            6,null,null,
            LocalDate.now()
        ),
        AdSet(
            "Фрукты",
            mutableStateListOf<Advert>(
                Advert("кило помэло", "покупайте", 0.1f,null,null,null, 2),
                Advert("манго", "желтые манго очень вкусные", 0.2f,null,null,null, 2),
                Advert("личики", "описание личиков", 0.3f,null,null,null, 2)
            ),
            6,null,null,
            LocalDate.now()
        )
    )

    // LOCAL DATABASE USE
    // помечает, что редактирование закончено -> нет выбранных заметок
    fun onEditComplete(){
        val set = selectedSet.value
        if (set == null || set.name!!.isBlank()) return

        /*
        viewModelScope.launch {
            notesUseCase.save(set)
        }
         */
        screen_name.value = "Подборки"
        selectedSet.value = null
        edited_set = null
    }

    fun onSetSelected(set: AdSet?){
        selectedSet.value = set
        screen_name.value = selectedSet.value!!.name.toString()
        //edited_setname = selectedSet.value!!.name

        val edited_ads: SnapshotStateList<Advert> = selectedSet.value!!.adverts!!.toMutableStateList()
        edited_set = AdSet(selectedSet.value!!.name,
            edited_ads,
            selectedSet.value!!.update_interval,null,null,
            selectedSet.value!!.last_update)
    }
    fun onDeleteSet(){
        sets.remove(selectedSet.value)
        //selectedSet.value = null
        onEditComplete()
    }

    // для удаления объявлений из списков (когда объявление еще не выбрано)
    fun onRemoveAd(advert: Advert){
        adverts = selectedSet.value!!.adverts as SnapshotStateList<Advert>
        adverts.remove(advert)
        if(favs.contains(advert)) favs.remove(advert)
        selectedSet.value!!.adverts = adverts
        selectedAd.value = null
    }
    // для удаления объявления из просмотра (есть selectedAd)
    fun onRemoveAd(){
        adverts = selectedSet.value!!.adverts as SnapshotStateList<Advert>
        adverts.remove(selectedAd.value)
        if(favs.contains(selectedAd.value)) favs.remove(selectedAd.value)
        selectedSet.value!!.adverts = adverts
        screen_name.value = selectedSet.value!!.name.toString()
        selectedAd.value = null
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
    fun onAddSetClicked(){
        screen_name.value = "Новая подборка"
        selectedSet.value = AdSet("",getTestSet(),10,null,null,null)
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
            //if(selectedSet.value != null){
            if(selectedSet.value != null){
                screen_name.value = selectedSet.value!!.name.toString()
            }else{
                if (favourites.value) screen_name.value = "Избранное"
                else screen_name.value = "Подборки"
            }
            //favourites.value = false
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
        val lest = mutableStateListOf<Advert>(
            //Advrt("test 1","caption 1", 0.1f,0f,0,0,"",""),
            //Advrt("test 2","caption 2", 0.2f,0f,0,0,"",""),
        )
        return lest
    }

}