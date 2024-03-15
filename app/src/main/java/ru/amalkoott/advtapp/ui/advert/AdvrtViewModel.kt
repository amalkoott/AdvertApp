package ru.amalkoott.advtapp.ui.advert

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advrt
import java.time.LocalDate

class AdvrtViewModel: ViewModel() {

    var screen_name = mutableStateOf<String>("Подборки")
    var selectedSet = mutableStateOf<AdSet?>(null )
    var selectedAd = mutableStateOf<Advrt?>(null )

    var menu = mutableStateOf<String>("Main")
    var favourites = mutableStateOf(false)
    var settings = mutableStateOf(false)

    var edited_set: AdSet? = null
    //val notes = MutableStateFlow<List<AdSet>>(emptyList())
    var adverts = mutableStateListOf<Advrt>()
    val sets = mutableStateListOf<AdSet>(
        AdSet(
            "Квартиры",
            mutableStateListOf<Advrt>(
                Advrt("квартира 1", "супер квартира", 1.2f,0f,0,0,"",""),
                Advrt("квартира 2", "не очень супер квартира", 6.2f,0f,0,0,"",""),
                Advrt("квартира 3", "бомж хата", 0.2f,0f,0,0,"","")
            ),
            6,
            LocalDate.now()

        ),
        AdSet(
            "Машины",
            mutableStateListOf<Advrt>(
                Advrt("ауди 1", "ну это пушка, это бомба", 3.4f,0f,0,0,"",""),
                Advrt("бэмвэ 1", "аджара гуджу", 6.6f,0f,0,0,"",""),
                Advrt("камри 3.5", "любви достойна только мать", 3.5f,0f,0,0,"","")
            ),
            6,
            LocalDate.now()
        ),
        AdSet(
            "Фрукты",
            mutableStateListOf<Advrt>(
                Advrt("кило помэло", "покупайте", 0.1f,0f,0,0,"",""),
                Advrt("манго", "желтые манго очень вкусные", 0.2f,0f,0,0,"",""),
                Advrt("личики", "описание личиков", 0.3f,0f,0,0,"","")
            ),
            6,
            LocalDate.now()
        )
    )



    //  в выбранную заметку заносит новые значения
    fun onSetChange(name: String, update_interval: Int){
        selectedSet.value!!.name = name
        selectedSet.value!!.update_interval = update_interval
        selectedSet.value!!.last_update = LocalDate.now()
    }
    // помечает, что редактирование закончено -> нет выбранных заметок
    fun onEditComplete(){
        val set = selectedSet.value
        if (set == null || set.name.isBlank()) return

        /*
        viewModelScope.launch {
            notesUseCase.save(set)
        }
         */
        screen_name.value = "Подборки"
        selectedSet.value = null
        edited_set = null
    }
    fun onAddNoteClicked(){
        screen_name.value = "Новая подборка"
        selectedSet.value = AdSet("",getTestSet(),10,null)
        sets.add(selectedSet.value!!)
    }

    fun onSetSelected(set: AdSet?){
        selectedSet.value = set
        screen_name.value = selectedSet.value!!.name
        //edited_setname = selectedSet.value!!.name

        val edited_ads: SnapshotStateList<Advrt> = selectedSet.value!!.adverts.toMutableStateList()
        edited_set = AdSet(selectedSet.value!!.name,
            edited_ads,
            selectedSet.value!!.update_interval,
            selectedSet.value!!.last_update)
    }

    fun onFavouritesClick(){
        screen_name.value = "Избранное"
        favourites.value = !favourites.value

    }

    fun onSettingsClick(){
        screen_name.value = "Настройки"
        settings.value = !settings.value
    }
    fun onDeleteSet(){
        sets.remove(selectedSet.value)
        //selectedSet.value = null
        onEditComplete()
    }
    fun onAdSelected(advrt: Advrt){
        selectedAd.value = advrt
        adverts = selectedSet.value!!.adverts as SnapshotStateList<Advrt>
    }
    fun onRemoveAd(){
        adverts.remove(selectedAd.value)
        selectedSet.value!!.adverts = adverts
        selectedAd.value = null
    }
    fun onBackClick(){
        if (selectedAd.value != null) {
            selectedAd.value = null
            screen_name.value = selectedSet.value!!.name
        }else{
            // 2 случая возврата:
            // начали создавать новую подборку и решили вернуться
//            if(selectedSet.value!!.name == ""){
//                sets.remove(selectedSet.value)
//            }
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
            // selectedSet.value = null
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

    fun getTestSet(): List<Advrt>{
        val lest = mutableStateListOf<Advrt>(
            Advrt("test 1","caption 1", 0.1f,0f,0,0,"",""),
            Advrt("test 2","caption 2", 0.2f,0f,0,0,"",""),
        )
        return lest
    }
    /*
    fun getSets(): List<AdSet>{
        val sets = mutableStateListOf<AdSet>(
            AdSet(
                "Квартиры",
                mutableStateListOf<Advrt>(
                    Advrt("квартира 1", "супер квартира", 1.2f),
                    Advrt("квартира 2", "не очень супер квартира", 6.2f),
                    Advrt("квартира 3", "бомж хата", 0.2f)
                ),
                6,
                LocalDate.now()

            ),
            AdSet(
                "Машины",
                mutableStateListOf<Advrt>(
                    Advrt("ауди 1", "ну это пушка, это бомба", 3.4f),
                    Advrt("бэмвэ 1", "аджара гуджу", 6.6f),
                    Advrt("камри 3.5", "любви достойна только мать", 3.5f)
                ),
                6,
                LocalDate.now()
            ),
            AdSet(
                "Фрукты",
                mutableStateListOf<Advrt>(
                    Advrt("кило помэло", "покупайте", 0.1f),
                    Advrt("манго", "желтые манго очень вкусные", 0.2f),
                    Advrt("личики", "описание личиков", 0.3f)
                ),
                6,
                LocalDate.now()
            )
        )
        return sets
    }
    */
}