package ru.amalkoott.advtapp.domain

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.data.remote.ServerRequestsRepository
import ru.amalkoott.advtapp.domain.entities.AdSet
import ru.amalkoott.advtapp.domain.entities.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.entities.Advert
import ru.amalkoott.advtapp.domain.entities.BlackList
import ru.amalkoott.advtapp.domain.worker.StartWorker
import javax.inject.Inject

class AppUseCase @Inject constructor(
    private val appRepo: AppRepository,
    private val appApi: ServerRequestsRepository,
    private val workManager: StartWorker
) {

    suspend fun fillWithInitialSets(initialSets: List<AdSet>){
        appRepo.clearDatabase()
        workManager.clearAllWork()
        appRepo.fillDatabase(initialSets)
    }
    fun setsFlow(): Flow<List<AdSet>> {//Flow<List<AdSet>> {
        return appRepo.loadAllSetsFlow()
    }
    fun favouritesFlow(): Flow<List<Advert>>{
        return appRepo.loadFavourites()
    }
    fun blackListFlow(): Flow<List<BlackList>>{
        return appRepo.getBlackList()
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
        return if (response.isNotEmpty()){
            response
        }else{
            null
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun updateSetByRemote(adSet: AdSet, context: Context){
    // удаляем старую задачу и создаем новую
    workManager.removeUpdatingSet(adSet)
    workManager.updateSet(context, adSet, adSet.getSearchParameters(), false)
    }

    @SuppressLint("RestrictedApi")
    suspend fun saveSet(set: AdSet, search: RealEstateSearchParameters?, context: Context):Long?{
        if (set.id == null){
            val adverts = sendSearching(search)
            // сначала поиск на сервере
            if (adverts.isNullOrEmpty()) return null
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
    fun getAdvertCount(id: Long): Flow<Int>{
        return appRepo.getAdvertsCount(id)
    }
    private fun toCaption(search: RealEstateSearchParameters?): String{
        var caption = ""
        val gson = Gson()
        val json = gson.toJsonTree(search)
        caption = json.toString()
        return caption
    }
    suspend fun removeSet(set: AdSet){
        appRepo.removeSet(set)
        appRepo.removeAdsBySet(set.id!!)
        appRepo.removeBlackListFor(set.id!!)
        workManager.removeUpdatingSet(set)
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

    suspend fun removeFromBlackList(ad: BlackList){
        val newAd = Advert(
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

        // получаем подборку объявления
        val set = appRepo.getSet(newAd.adSetId!!)
        // формируем новый список объявлений и добавляем
        val adverts = set.adverts!!.toMutableList()
        adverts.add(newAd)
        set.adverts = adverts
        // обновляем подборку
        appRepo.updateSet(set)

        // возвращаем объявление в базу
        appRepo.addAdv(newAd)
        // удаляем из ЧС
        appRepo.removeFromBlackList(ad.id!!)
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