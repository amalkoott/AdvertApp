package ru.amalkoott.advtapp.data.local

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppRepository
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
    override suspend fun clearDatabase()= withContext(Dispatchers.IO){
        appDao.deleteAllSets()
        appDao.deleteAllAds()
    }
    override suspend fun fillDatabase(set: List<AdSet>){
        //val set = AdSet(id = 1, adverts = listOf(Advert(id = 1, setId = 1, name = "Advert 1")))

        set.forEach{adSet ->
            appDao.insertSet(adSet)
            adSet.adverts!!.forEach{
                appDao.insertAd(it)
            }

        }



            /*
        var i = 0

            ids!!.forEach {
                id->
                run {
                    adverts?.get(i)?.forEach {
                        it.adSetId = ids!![i]
                        appDao.insertAd(it)
                    }
                }
                i++
            }

             */
        /*
            set.forEach{adSet ->

                run {adverts?.get(i)?.forEach {
                    it.adSetId = ids!![i]
                    appDao.insertAd(it)
                }}

                adSet.adverts = adverts?.get(i)
                i++
            }
*/

    /*notes.forEach{
            appDao.insertSet(it)
            it.adverts!!.forEach {
                appDao.insertAd(it)
            }
        }
        */
    }

    override suspend fun addSet(note: AdSet): Unit = withContext(Dispatchers.IO){
        appDao.insertSet(note)
    }
    override suspend fun updateSet(note: AdSet) = withContext(Dispatchers.IO){
        appDao.updateSet(note)
    }

    override suspend fun removeSet(set: AdSet) {
        appDao.deleteSetById(set.id!!)
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
}