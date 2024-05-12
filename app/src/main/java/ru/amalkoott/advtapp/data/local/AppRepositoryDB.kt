package ru.amalkoott.advtapp.data.local

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppRepository
import ru.amalkoott.advtapp.domain.BlackList
import ru.amalkoott.advtapp.domain.SetCategory
import java.util.Dictionary

// здесь методы, которые обращаются к Dao, эти методы могут быть вызваны откуда угодно для работы с БДшкой
class AppRepositoryDB(
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
    }
    override suspend fun fillDatabase(set: List<AdSet>){
        set.forEach{adSet ->
            appDao.insertSet(adSet)
            adSet.adverts!!.forEach{
                appDao.insertAd(it)
            }
        }
    }

    override suspend fun addSet(note: AdSet): Unit = withContext(Dispatchers.IO){
        val id = async { appDao.insertSet(note) }
        //println(id.await())
        for (ad in note.adverts!!){
            ad.adSetId = id.await()
            appDao.insertAd(ad)
        }
        note.id = id.await()

        appDao.updateSet(note)

        // добавление черного списка для подборки
       // val list = BlackList(setId = id.await())
       // appDao.createBlackList(list)

    }

    override suspend fun removeSet(set: AdSet) {
        appDao.deleteSetById(set.id!!)
    //    appDao.deleteBlackListBySetId(set.id!!)
    }


    override suspend fun updateSet(note: AdSet) = withContext(Dispatchers.IO){
        appDao.updateSet(note)
    }


    /*
        override fun byRemoteID(remoteId: Long): AdSet?{
            return appDao.byRemoteID(remoteId)
        }
      */
    override fun byEquals(title: String, category: SetCategory): AdSet?{
        return appDao.byEquals(title, category)
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

    override fun getAdvertsCount(id: Long): Flow<Int> {//= withContext(Dispatchers.IO) {
        return appDao.getAdvertsCount(id)
    }

    override suspend fun getBlackList(id: Long): List<BlackList> = withContext(Dispatchers.IO) {
        return@withContext appDao.getBlackList(id)
    }

    override suspend fun deleteAdvertsBySet(id: Long) {
        appDao.deleteAdvertsBySet(id)
    }

}