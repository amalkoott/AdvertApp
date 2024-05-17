package ru.amalkoott.advtapp.domain

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.Gson
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
    private val workManager: StartWorker
) {

    suspend fun fillWithInitialSets(initialSets: List<AdSet>){
        // должен очистить содержимое базы
        appRepo.clearDatabase()
        workManager.clearAllWork()
        // затем добавить в базу переданный список заметок
        appRepo.fillDatabase(initialSets)
    }
    fun setsFlow(): Flow<List<AdSet>> {//Flow<List<AdSet>> {
        Log.d("TEST",appApi.toString())
        //var flow : Flow<List<AdSet>>
        return appRepo.loadAllSetsFlow()
    }
    fun favouritesFlow(): Flow<List<Advert>>{
        return appRepo.loadFavourites()
    }
    fun advertsFlow(): Flow<List<Advert>> {
        var flow : Flow<List<Advert>>
        return appRepo.loadAllAdsFlow()
    }
    fun advertsBySetFlow(id: Long): Flow<List<Advert>> {
        var flow : Flow<List<Advert>>
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

    // update by button click
    @SuppressLint("SuspiciousIndentation")
    fun updateSetByRemote(adSet: AdSet, context: Context){
    // удаляем старую задачу и создаем новую
    workManager.removeUpdatingSet(adSet)
    workManager.updateSet(context, adSet, adSet.getSearchParameters(), false)

    // TODO перебросить в Adset

  /*      val parameters = RealEstateSearchParameters()
        val gson = Gson()
        val jelem: JsonElement = gson.fromJson<JsonElement>(adSet.caption, JsonElement::class.java)
        val json = jelem.asJsonObject
*/
    /*
        try {
            /*
            parameters.category = json["category"]?.toString()
            parameters.city = json["city"]?.toString()
            parameters.dealType = json["dealType"]?.toString()
            parameters.livingType = json["livingType"]?.toString()
            parameters.priceType = json["priceType"]?.toString()
*/
            //val parameters = adSet.getSearchParameters()
            val adverts = sendSearching(adSet.getSearchParameters())!!.toMutableList()
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

     */
    }
    suspend fun test(){
        val sets = appRepo.loadAllSets()

        Log.d("SETS_COUNT_IN_APP","${sets.count()}")
    }
    @SuppressLint("RestrictedApi")
    suspend fun saveSet(set: AdSet, search: RealEstateSearchParameters?, context: Context):Long?{
        if (set.id == null){
            val adverts = sendSearching(search)
            // сначала поиск на сервере
            if (adverts.isNullOrEmpty()) return null
            //val test = appApi.checkServer()
            //val response = appApi.get(SearchParameters("Снять"))
            // @TODO собирать set.caption из параметров поиска
            set.adverts = adverts

            set.caption = toCaption(search)

            set.id = appRepo.addSet(set)
            workManager.updateSet(context,set,search!!,true)
        } else
        {
            // обновление без сервака
           appRepo.updateSet(set)
            updateSetByRemote(set,context)
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
        workManager.removeUpdatingSet(set)
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