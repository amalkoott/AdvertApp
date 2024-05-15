package ru.amalkoott.advtapp.domain

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.data.remote.ServerRequestsRepository
import ru.amalkoott.advtapp.domain.worker.StartWorker
import javax.inject.Inject


/*@TODO надо сделать:
*/
class AppUseCase @Inject constructor(
    private val appRepo: AppRepository,
    private val appApi: ServerRequestsRepository,
    //private val workManager: StartWorker
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
    fun setsFlow(): Flow<List<AdSet>> {
        var flow : Flow<List<AdSet>>
        // метод подписки на данные (что бы это ни значило)
        return appRepo.loadAllSetsFlow()
    }
    fun favouritesFlow(): Flow<List<Advert>>{
        return appRepo.loadFavourites()
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
    suspend fun sendSearching(search: RealEstateSearchParameters?): List<Advert>?{
        if (search == null) return null
        val response = appApi.get(search)
        //val response = appApi.get(SearchParameters("Снять"))
        return if (response.isNotEmpty()){
            // response to set и потом сохранять
            //val set = AdSet(name = "",adverts = response, update_interval = 10, caption = null, category = null, last_update = null)
            response
        }else{
            null
        }
        //return response
    }
// todo удаление подборки - удаление black list с ее id
    @SuppressLint("SuspiciousIndentation")
    suspend fun updateSet(adSet: AdSet,context: Context){
   // startOneTimeWorker(context, "(TEST)GET_ADVERTS_FOR_SET", adSet)
  //  workManager.startOneTimeWorker(context, "(TEST)GET_ADVERTS_FOR_SET", adSet)

        val parameters = RealEstateSearchParameters()
        // todo собираем parameters через set.caption (можно в json конвертить строку)
        val gson = Gson()
      /*
        val temp = gson.toJsonTree(adSet.caption)
    val json = temp.asJsonObject
    */
    val jelem: JsonElement = gson.fromJson<JsonElement>(adSet.caption, JsonElement::class.java)
    val json = jelem.asJsonObject

        try {
            parameters.category = json["category"]?.toString()
            parameters.city = json["city"]?.toString()
            parameters.dealType = json["dealType"]?.toString()
            parameters.livingType = json["livingType"]?.toString()
            parameters.priceType = json["priceType"]?.toString()

            val adverts = sendSearching(parameters)!!.toMutableList()
            if (adverts.isEmpty()) return

            val blackList = appRepo.getBlackList(adSet.id!!)
            if (blackList.isNotEmpty()){
                blackList.forEach{
                    blackList -> adverts.toList().forEach {
                        advert -> if (blackList.hash == advert.hash) {
                            // удаляем из adverts
                            adverts.remove(advert)
                            return@forEach
            } } } }
            // удаляем все объявления - оптимальный способ, т.к. объявления на локалке могут устареть
            appRepo.deleteAdvertsBySet(adSet.id!!)

            // вставляем новые объявления в бд
            adverts.forEach {
                it.adSetId = adSet.id
                appRepo.addAdv(it)
            }

        }catch (e:Exception){
            Log.w("RemoteUpdateSetError",e.message!!)
        }
    }
    private fun isEqualAdvert(first: BlackList, second: Advert): Boolean{
        // isFavourite и id смысла сравнивать нет
        return first.hash == second.hash
        /*
        return (first.name == second.name) &&
                (first.description == second.description) &&
                (first.price == second.price) &&
                (first.location == second.location) &&
                (first.address == second.address)

         */
    }
    private fun test(){
        Log.d("WORKER_WITH_USE_CASE","This method has been started from worker...")
    }
    suspend fun saveSet(set: AdSet,search: RealEstateSearchParameters?,context: Context):Long?{
        if (set.id == null){
            val adverts = sendSearching(search)
            // сначала поиск на сервере
            if (adverts.isNullOrEmpty()) return null
            //val test = appApi.checkServer()
            //val response = appApi.get(SearchParameters("Снять"))
            // @TODO собирать set.caption из параметров поиска
            set.adverts = adverts

            set.caption = toCaption(search)

            appRepo.addSet(set)

       //     workManager.startOneTimeWorker(context, "(TEST)GET_ADVERTS_FOR_SET", set)

        }else
        {
            // обновление без сервака
           appRepo.updateSet(set)
        }
        return set.id
    }
    suspend fun getAdvertCount(id: Long): Flow<Int>{
        //val count = appRepo.getAdvertsCount(id)
        return appRepo.getAdvertsCount(id)
    }
    private fun toCaption(search: RealEstateSearchParameters?): String{
        var caption = ""
        val gson = Gson()
        val json = gson.toJsonTree(search)
        caption = json.toString()
        /*
        val map: Map<*, *> = gson.fromJson(json, MutableMap::class.java)

        for (key in map.keys){
            caption += "$key: ${map[key]}\n"
        }
         */
        return caption
    }
    suspend fun removeSet(set: AdSet){
        appRepo.removeSet(set)
    }
    suspend fun saveAd(ad: Advert){

    }
    suspend fun removeAd(ad: Advert){
        val blackAdvert = BlackList(
            id = ad.id,
            name = ad.name,
            description = ad.description,
            price = ad.price,
            location = ad.location,
            address = ad.address,
            url = ad.url,
            imagesURL = ad.imagesURL,
            additionalParam = ad.additionalParam,
            adSetId = ad.adSetId,
            isFavourite = false,
            hash = ad.hash)
        // добавляем в черный список
        appRepo.addToBlackList(blackAdvert)
        appRepo.removeAd(ad)
    }
    suspend fun getSetsWithAd(id: Long): List<AdSetWithAdverts>{
        return appRepo.getAdSetsWithAdverts(id)
    }

    suspend fun addFavourites(id: Long){
        appRepo.setAdAsFavourite(id)
    }
    suspend fun deleteFavourites(id: Long){
        appRepo.setAdAsNonFavourite(id)
    }
}