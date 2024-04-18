package ru.amalkoott.advtapp.domain

import ru.amalkoott.advtapp.data.remote.SearchParameters
import java.lang.reflect.Parameter

interface AppRemoteRepository {
    // получение новой подборки
    suspend fun get(): List<SearchParameters>//@TODO подумать, как параметры собираться будут

    // обновление заданной подборки
    suspend fun update(set: AdSet): Boolean
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