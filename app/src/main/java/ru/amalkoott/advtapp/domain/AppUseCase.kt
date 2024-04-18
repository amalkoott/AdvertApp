package ru.amalkoott.advtapp.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.Flow
import ru.amalkoott.advtapp.data.remote.ServerAPI
import ru.amalkoott.advtapp.data.remote.ServerRequestsRepository
import java.util.Dictionary

/*@TODO надо сделать:
*/
class AppUseCase(
    private val appRepo: AppRepository,
    private val appApi: ServerRequestsRepository
) {
    suspend fun fillWithInitialSets(initialSets: List<AdSet>){
        // должен очистить содержимое базы
        appRepo.clearDatabase()
        // затем добавить в базу переданный список заметок
        appRepo.fillDatabase(initialSets)
    }
    /*
    suspend fun loadRemoteNotes(){
        // получаем список всех заметок с сервера
        val listNotes: List<AdSet> = notesRemoteRepo.list()

        listNotes.forEach {
            updateLocalDB(it)
        }
    }

     */
    fun notesFlow(): Flow<List<AdSet>> {
        var flow : Flow<List<AdSet>>
        // метод подписки на данные (что бы это ни значило)
        return appRepo.loadAllSetsFlow()
    }
    fun advertsFlow(): Flow<List<Advert>> {
        var flow : Flow<List<Advert>>
        // метод подписки на данные (что бы это ни значило)
        return appRepo.loadAllAdsFlow()
    }
    fun advertsBySetFlow(id: Long): Flow<List<Advert>> {
        var flow : Flow<List<Advert>>
        // метод подписки на данные (что бы это ни значило)
        return appRepo.loadAllAdsBySetFlow(id)
    }
    suspend fun saveSet(set: AdSet){
        if (set.id == null){
            appRepo.addSet(set)
        // @TODO при добавлении новой подборки надо отправлять на сервер запрос на поиск объявлений и если объявления не найдены, то не добавлять подборку
        // @TODO либо сделать возможность сохранить подборку, чтобы при появлении объявлений подборка обновилась сама
        }else
        {
            appRepo.updateSet(set)
        }
    }
    suspend fun removeSet(set: AdSet){
        appRepo.removeSet(set)
    }
    suspend fun saveAd(ad: Advert){

    }
    suspend fun removeAd(ad: Advert){
        appRepo.removeAd(ad)
    }
    suspend fun getSetsWithAd(id: Long): List<AdSetWithAdverts>{
        return appRepo.getAdSetsWithAdverts(id)
    }
}