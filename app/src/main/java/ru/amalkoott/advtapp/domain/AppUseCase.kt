package ru.amalkoott.advtapp.domain

import kotlinx.coroutines.flow.Flow

class AppUseCase(
    private val appRepo: AppRepository
) {
    suspend fun fillWithInitialSets(initialNotes: List<AdSet>){
        // должен очистить содержимое базы
        appRepo.clearDatabase()
        // затем добавить в базу переданный список заметок
        appRepo.fillDatabase(initialNotes)
    }
    /*
    suspend fun loadRemoteNotes(){
        // получаем список всех заметок с сервера
        val listNotes: List<AdSet> = notesRemoteRepo.list()

        listNotes.forEach {
            updateLocalDB(it)
        }
    }

     */
    fun notesFlow(): Flow<List<AdSet>> {
        var flow : Flow<List<AdSet>>
        // метод подписки на данные (что бы это ни значило)
        return appRepo.loadAllSetsFlow()
    }
}