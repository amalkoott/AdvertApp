package ru.amalkoott.advtapp.domain

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters

interface AppRemoteRepository {
    // получение новой подборки
    suspend fun get(parameters: RealEstateSearchParameters): List<Advert>?

    suspend fun get(json: JsonElement): List<Advert>?
    suspend fun checkServer(): JsonObject?

    // обновление заданной подборки
   // suspend fun update(set: AdSet): Boolean
}

/*

package ru.protei.malkovaar.domain

interface NotesRemoteRepository {
    suspend fun list(): List<Note>
    suspend fun add(note: Note): Long?
    suspend fun update(note: Note): Boolean
    suspend fun delete(note: Note): Boolean
}

 */