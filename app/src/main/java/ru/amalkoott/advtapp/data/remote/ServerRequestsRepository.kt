package ru.amalkoott.advtapp.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppRemoteRepository

class ServerRequestsRepository(val serverApi: ServerAPI):AppRemoteRepository {

    // получаем список объявлений (подборку) по параметрам
    override suspend fun get(parameters: SearchParameters): List<Advert> = withContext(Dispatchers.IO){
        val result: JsonArray?
        // делаем get запрос - получаем Json-строку
        // конвертим json-строку в подборку
        try {
            val gson = Gson()
            val json = gson.toJsonTree(parameters)
            val response = serverApi.get(json as JsonObject)

            if(!response.isSuccessful){
                Log.w("AppRepositoryApi","Can't get issues $response")
                return@withContext emptyList()
            }
            result = response.body()!!.asJsonArray
            val size = result.size()
        } catch (e: Exception){
            Log.w("ServerRequestRepository","Can't get issues", e)
            return@withContext emptyList()
        }

        return@withContext emptyList()
        /*
        val sets = issues?.map{
            toAdvert(it.toString())
        } ?: emptyList()
        sets

         */
    }
    private fun toAdvert(request: String): Advert {
//      Advert(0,"empty_title", "empty_caption", 1.2f,"undefined_location",null,null, 0)

        return Advert(0,"empty_title", "empty_caption", 1.2f,"undefined_location",null,null, 0)
    }
    override suspend fun checkServer(): JsonObject? = withContext(Dispatchers.IO){
        val result: JsonObject?
        try {
            val response = serverApi.check()

            if (!response.isSuccessful){
                Log.w("AppRepositoryApi","Can't get server $response")
                return@withContext null
            }
            result = response.body()
            Log.w("Response from server","$result")
        }catch (e:Exception){
            Log.w("AppRepositoryApi","$e")
            return@withContext null
        }
        return@withContext  result
    }
    /*
    override suspend fun update(set: AdSet): Boolean = withContext(Dispatchers.IO) {
        var receive = toReceiveSet(set)
        try{
            // пробуем апдейтнуть имеющуюся заметку
            var result = serverApi.update(issue.number!!,issue)

            if(!result.isSuccessful){
                Log.w("NotesRepositoryApi","Can't add issues $result")
                return@withContext false
            }

        }catch (e: Exception){
            // описываем почему не получилось
            Log.w("NotesGitHubRepository","Can't get issues", e)
            return@withContext false
        }
        return@withContext true // return в try catch
    }
    */

    /*
    private fun toReceiveSet(set: AdSet): ServerSet {
        return ServerSet(note.remoteId, note.title, note.text)
    }

     */
}
/*

package ru.protei.malkovaar.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.protei.malkovaar.domain.Note
import ru.protei.malkovaar.domain.NotesRemoteRepository
import javax.inject.Inject

class NotesGitHubRepository @Inject constructor(
    private val notesApi: NotesGitHubApi
): NotesRemoteRepository {
    override suspend fun list(): List<Note> = withContext(Dispatchers.IO){
        var issues: List<GitHubIssue>?
        try {
            val result = notesApi.getList()

            if(!result.isSuccessful){
                Log.w("NotesRepositoryApi","Can't get issues $result")
                return@withContext emptyList()
            }
            issues = result.body()
        } catch (e: Exception){
            Log.w("NotesGitHubRepository","Can't get issues", e)
            return@withContext emptyList()
        }
        val notes = issues?.map{
            toNote(it)
        } ?: emptyList()
        notes
    }

    override suspend fun add(note: Note): Long?  = withContext(Dispatchers.IO){
        var newIssue: GitHubIssue = toGitHubIssue(note)
        try{
            // пробуем добавить на сервер новый issue
            var result = notesApi.add(newIssue)

            if(!result.isSuccessful){
                Log.w("NotesRepositoryApi","Can't add issues $result")
                return@withContext null
            }
            newIssue = result.body()!!

        }catch (e: Exception){
            // описываем почему не получилось
            Log.w("NotesGitHubRepository","Can't get issues", e)
            return@withContext null
        }
        return@withContext newIssue.number // заменить на remoteId новой заметки
    }

    override suspend fun update(note: Note): Boolean = withContext(Dispatchers.IO) {
        var issue = toGitHubIssue(note)
        try{
            // пробуем апдейтнуть имеющуюся заметку
            var result = notesApi.update(issue.number!!,issue)

            if(!result.isSuccessful){
                Log.w("NotesRepositoryApi","Can't add issues $result")
                return@withContext false
            }

        }catch (e: Exception){
            // описываем почему не получилось
            Log.w("NotesGitHubRepository","Can't get issues", e)
            return@withContext false
        }
        return@withContext true // return в try catch
    }

    override suspend fun delete(note: Note): Boolean {
        TODO("Not yet implemented")
    }
    private fun toNote(issue: GitHubIssue): Note {
        return Note(issue.title, issue.body, issue.number)
    }
    private fun toGitHubIssue(note: Note): GitHubIssue {
        return GitHubIssue(note.remoteId, note.title, note.text)
    }
}

 */