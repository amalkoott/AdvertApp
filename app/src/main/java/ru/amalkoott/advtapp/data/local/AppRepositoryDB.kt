package ru.amalkoott.advtapp.data.local

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.entities.AdSet
import ru.amalkoott.advtapp.domain.entities.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.entities.Advert
import ru.amalkoott.advtapp.domain.AppRepository
import ru.amalkoott.advtapp.domain.entities.BlackList
import javax.inject.Inject

class AppRepositoryDB @Inject constructor(
    private val appDao: AppDao
): AppRepository {
    override suspend fun loadAllSets(): List<AdSet> = withContext(Dispatchers.IO){
        return@withContext appDao.all()
    }
    override fun loadAllSetsFlow(): Flow<List<AdSet>> = appDao.allFlow()
    override fun loadAllAdsFlow(): Flow<List<Advert>> = appDao.allAdsFlow()
    override fun loadFavourites(): Flow<List<Advert>> = appDao.allFavourites()
    override fun loadAllAdsBySetFlow(id: Long): Flow<List<Advert>> = appDao.allAdsBySetFlow(id)
    override suspend fun clearDatabase()= withContext(Dispatchers.IO){
        appDao.deleteAllSets()
        appDao.deleteAllAds()
        appDao.deleteBlackList()
    }
    override suspend fun fillDatabase(set: List<AdSet>){
        set.forEach{adSet ->
            appDao.insertSet(adSet)
            adSet.adverts!!.forEach{
                appDao.insertAd(it)
            }
        }
    }
    override suspend fun removeAdsBySet(id: Long) {
        appDao.removeAdsBySet(id)
    }
    override suspend fun removeBlackListFor(id: Long) {
        appDao.removeBlackListFor(id)
    }
    override suspend fun getSet(id: Long): AdSet = withContext(Dispatchers.IO) {
        return@withContext appDao.getSet(id)
    }
    override suspend fun addSet(set: AdSet): Long = withContext(Dispatchers.IO){
        val id = async { appDao.insertSet(set) }
        for (ad in set.adverts!!){
            if(ad != null){
                ad.adSetId = id.await()
                appDao.insertAd(ad)
            }

        }
        set.id = id.await()
        appDao.updateSet(set)
        return@withContext id.await()
    }
    override suspend fun removeSet(set: AdSet) = withContext(Dispatchers.IO) {
        appDao.deleteSetById(set.id!!)
        appDao.deleteAdvertsBySet(set.id!!)
        appDao.deleteBlackListBySetId(set.id!!)
    }
    override suspend fun removeFromBlackList(id: Long) = withContext(Dispatchers.IO) {
        appDao.deleteBlackAdvert(id)
    }
    override suspend fun updateSet(note: AdSet) = withContext(Dispatchers.IO){
           val res = appDao.updateSet(note)
        Log.d("UpdateSet","Successful update for ${note.id}")
        return@withContext res
    }
    override suspend fun getAdSetsWithAdverts(id: Long): List<AdSetWithAdverts> = withContext(Dispatchers.IO) {
        return@withContext appDao.getAdSetsWithAdverts(id)
    }
    override suspend fun addAdv(ad: Advert): Unit = withContext(Dispatchers.IO) {
        appDao.insertAd(ad)
    }
    override suspend fun updateAd(ad: Advert): Unit = withContext(Dispatchers.IO) {
        appDao.updateAd(ad)
    }
    override suspend fun removeAd(ad: Advert) {
        appDao.deleteAdById(ad.id!!)
    }
    override suspend fun setAdAsFavourite(id: Long) {
        appDao.setFavourite(id)
    }
    override suspend fun setAdAsNonFavourite(id: Long) {
        appDao.removeFavourite(id)
    }
    override suspend fun addToBlackList(ad: BlackList) {
        appDao.addToBlackList(ad)
    }
    override fun getAdvertsCount(id: Long): Flow<Int> {
        return appDao.getAdvertsCount(id)
    }
    override fun getBlackList(id: Long): Flow<List<BlackList>> = appDao.getBlackList(id)
    override fun getBlackList(): Flow<List<BlackList>> = appDao.getBlackList()
    override suspend fun deleteAdvertsBySet(id: Long) {
        appDao.deleteAdvertsBySet(id)
    }
}