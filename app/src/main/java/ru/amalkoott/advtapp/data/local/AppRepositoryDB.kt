package ru.amalkoott.advtapp.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.AppRepository
import ru.amalkoott.advtapp.domain.SetCategory

// здесь методы, которые обращаются к Dao, эти методы могут быть вызваны откуда угодно для работы с БДшкой
class AppRepositoryDB(
    private val appDao: AppDao
): AppRepository {
    override suspend fun loadAllSets(): List<AdSet> = withContext(Dispatchers.IO){
        return@withContext appDao.all()
    }
    override fun loadAllSetsFlow(): Flow<List<AdSet>> = appDao.allFlow()
    override suspend fun clearDatabase(){
        appDao.deleteAll()
    }
    override suspend fun fillDatabase(notes: List<AdSet>){
        notes.forEach{
            appDao.insert(it)
        }
    }

    override suspend fun add(note: AdSet): Unit = withContext(Dispatchers.IO){
        appDao.insert(note)
    }
    override suspend fun update(note: AdSet) = withContext(Dispatchers.IO){
        appDao.update(note)
    }
/*
    override fun byRemoteID(remoteId: Long): AdSet?{
        return appDao.byRemoteID(remoteId)
    }
  */
    override fun byEquals(title: String, category: SetCategory): AdSet?{
        return appDao.byEquals(title, category)
    }

    override fun getAdSetsWithAdverts(id: Long): List<AdSetWithAdverts> {
        return appDao.getAdSetsWithAdverts(id)
    }
}