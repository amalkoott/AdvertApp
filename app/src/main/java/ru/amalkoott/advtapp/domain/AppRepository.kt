package ru.amalkoott.advtapp.domain

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun loadAllSets(): List<AdSet>
    fun loadAllSetsFlow(): Flow<List<AdSet>>
    suspend fun clearDatabase()
    suspend fun fillDatabase(notes: List<AdSet>)

    suspend fun add(note: AdSet)
    suspend fun update(note: AdSet)
/*
    fun byRemoteID(remoteId: Long): AdSet?
    */
    fun byEquals(title: String, category: SetCategory): AdSet?
    fun getAdSetsWithAdverts(id: Long): List<AdSetWithAdverts>
}