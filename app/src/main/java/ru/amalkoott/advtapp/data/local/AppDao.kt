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
import ru.amalkoott.advtapp.domain.SetCategory

@Dao
interface AppDao {
    // тут прописываются запросы к БД на SQL
    @Query("SELECT * FROM AdSet")
    fun all():List<AdSet>

    @Query("SELECT * FROM AdSet ORDER BY id ASC")
    fun allFlow(): Flow<List<AdSet>>

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
    /*
    @Query("SELECT * FROM AdSet WHERE remoteId = :remoteId")
    fun byRemoteID(remoteId: Long): AdSet
     */
    @Query("SELECT * FROM AdSet WHERE name = :name AND category = :category")
    fun byEquals(name: String, category: SetCategory): AdSet

    @Transaction
    @Query("SELECT * FROM AdSet WHERE id = :id")
    fun getAdSetsWithAdverts(id: Long): List<AdSetWithAdverts>
}