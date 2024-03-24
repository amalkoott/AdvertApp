package ru.amalkoott.advtapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AdSetWithAdverts
import ru.amalkoott.advtapp.domain.SetCategory

@Dao
interface AppDao {
    // тут прописываются запросы к БД на SQL
    @Query("SELECT * FROM AdSet")
    fun all():List<AdSet>

    @Query("SELECT * FROM AdSet ORDER BY id ASC")
    fun allFlow(): Flow<List<AdSet>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: AdSet): Long

    @Update
    suspend fun update(note: AdSet)

    @Query("DELETE FROM AdSet WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM AdSet")
    fun deleteAll()

    /*
    @Query("SELECT * FROM AdSet WHERE remoteId = :remoteId")
    fun byRemoteID(remoteId: Long): AdSet
     */
    @Query("SELECT * FROM AdSet WHERE name = :name AND category = :category")
    fun byEquals(name: String, category: SetCategory): AdSet

    @Query("SELECT * FROM AdSet WHERE id = :id")
    fun getAdSetsWithAdverts(id: Long): List<AdSetWithAdverts>
}