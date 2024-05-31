package ru.amalkoott.advtapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.BlackList
//import ru.amalkoott.advtapp.domain.BlackListWithAdverts
//import ru.amalkoott.advtapp.domain.SetAndBlackList

@Dao
interface AppDao {
    // тут прописываются запросы к БД на SQL
    @Query("SELECT * FROM AdSet")
    fun all():List<AdSet>

    @Query("SELECT * FROM AdSet ORDER BY id ASC")
    fun allFlow(): Flow<List<AdSet>>
    @Query("SELECT * FROM Advert ORDER BY id ASC")
    fun allAdsFlow(): Flow<List<Advert>>
    @Query("SELECT * FROM Advert WHERE adSetId =:id ORDER BY id ASC")
    fun allAdsBySetFlow(id: Long): Flow<List<Advert>>
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSetWithAd(set: AdSet):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(note: AdSet): Long

    @Update
    suspend fun updateSet(note: AdSet)

    @Query("DELETE FROM AdSet WHERE id = :id")
    suspend fun deleteSetById(id: Long)

    @Query("DELETE FROM AdSet")
    fun deleteAllSets()

    @Query("DELETE FROM Advert")
    fun deleteAllAds()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAd(ad: Advert): Long
    @Update
    suspend fun updateAd(ad: Advert)
    @Query("DELETE FROM Advert WHERE id = :id")
    suspend fun deleteAdById(id: Long)
    @Query("DELETE FROM Advert WHERE adSetId = :id")
    suspend fun removeAdsBySet(id: Long)
    @Query("DELETE FROM BlackList WHERE adSetId = :id")
    suspend fun removeBlackListFor(id: Long)

    @Transaction
    @Query("SELECT * FROM AdSet WHERE id = :id")
    fun getAdSetsWithAdverts(id: Long): List<AdSetWithAdverts>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToBlackList(ad: BlackList): Long

    @Query("UPDATE Advert SET isFavourite = 1 WHERE id = :id")
    suspend fun setFavourite(id: Long)
    @Query("UPDATE Advert SET isFavourite = 0 WHERE id = :id")
    suspend fun removeFavourite(id: Long)

    @Query("SELECT * FROM Advert WHERE isFavourite = 1")
    fun allFavourites(): Flow<List<Advert>>

    @Query("SELECT COUNT(*) FROM Advert WHERE adSetId = :id")
    fun getAdvertsCount(id: Long): Flow<Int>

    @Query("SELECT * FROM BlackList WHERE adSetId = :id")
    fun getBlackList(id: Long): Flow<List<BlackList>>

    @Query("SELECT * FROM BlackList")
    fun getBlackList(): Flow<List<BlackList>>

    @Query("SELECT * FROM AdSet WHERE id = :id")
    fun getSet(id: Long): AdSet


    @Query("DELETE FROM BlackList WHERE adSetId = :id")
    fun deleteBlackListBySetId(id: Long)

    @Query("DELETE FROM BlackList WHERE id = :id")
    fun deleteBlackAdvert(id: Long)

    @Query("DELETE FROM Advert WHERE adSetId = :id")
    fun deleteAdvertsBySet(id: Long)


    @Query("DELETE FROM BlackList")
    fun deleteBlackList()
}