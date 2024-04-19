package ru.amalkoott.advtapp.domain

import com.google.gson.JsonObject
import ru.amalkoott.advtapp.data.remote.SearchParameters
import ru.amalkoott.advtapp.data.remote.ServerSet
import java.lang.reflect.Parameter

interface AppRemoteRepository {
    // получение новой подборки
    suspend fun get(parameters: SearchParameters): List<Advert>?//@TODO подумать, как параметры собираться будут
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