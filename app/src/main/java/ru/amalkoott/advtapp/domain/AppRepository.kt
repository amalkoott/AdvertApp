package ru.amalkoott.advtapp.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.Flow
import java.util.Dictionary

interface AppRepository {
    suspend fun loadAllSets(): List<AdSet>
    fun loadAllSetsFlow(): Flow<List<AdSet>>
    fun loadAllAdsFlow(): Flow<List<Advert>>
    fun loadAllAdsBySetFlow(id: Long): Flow<List<Advert>>
    suspend fun clearDatabase()
    suspend fun fillDatabase(sets: List<AdSet>)


    suspend fun addSet(note: AdSet)
    suspend fun updateSet(note: AdSet)
    suspend fun removeSet(set: AdSet)
/*
    fun byRemoteID(remoteId: Long): AdSet?
    */
    fun byEquals(title: String, category: SetCategory): AdSet?
    suspend fun getAdSetsWithAdverts(id: Long): List<AdSetWithAdverts>

    suspend fun addAdv(ad: Advert)
    suspend fun updateAd(ad: Advert)
    suspend fun removeAd(ad: Advert)
}