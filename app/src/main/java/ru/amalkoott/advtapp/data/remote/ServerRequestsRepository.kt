package ru.amalkoott.advtapp.data.remote

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppRemoteRepository
import java.net.SocketTimeoutException

class ServerRequestsRepository(val serverApi: ServerAPI):AppRemoteRepository {

    // получаем список объявлений (подборку) по параметрам
    override suspend fun get(parameters: SearchParameters): List<Advert> = withContext(Dispatchers.IO){
       // val result: JsonArray?
        // todo отправляем на сервак параметры (принимается вродеп норм)
        val result = mutableListOf<Advert>()
        val resultList: JsonArray
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
            resultList = response.body()!!.asJsonArray
            /*
            for (item in resultList){
                result.add(toAdvert(item))
            }
            */
            //result = response.body()!!.asJsonArray
           // val size = result.size()
        }catch (e: Exception){
            Log.w("ServerRequestRepository","Can't get issues", e)
            if(e.message == "timeout"){
                Log.w("ServerRequestRepository","TIMEOUT")
                return@withContext emptyList()
            }
            if(e.message?.contains("failed to connect") == true){
                Log.w("ServerRequestRepository","FAILED TO CONNECT")
                return@withContext emptyList()
            }
            Log.w("ServerRequestRepository","UNDEFINED ERROR")

            return@withContext emptyList()
        }
        val set = resultList.map {
            toAdvert(it)
        } ?: emptyList()
        return@withContext set

       // return@withContext emptyList()
        /*
        val sets = issues?.map{
            toAdvert(it.toString())
        } ?: emptyList()
        sets

         */
    }
    private fun toAdvert(request: JsonElement): Advert {
//      Advert(0,"empty_title", "empty_caption", 1.2f,"undefined_location",null,null, 0)
        val jsonAdvert = request.asJsonObject
        var title: String? = null
        var desc: String? = null
        var price: String? = null
        var address: String? = null
        var loc: String? = null
        var url: String? = null
        var images: String? = null

        try {
            title = jsonAdvert["title"].asString
        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {
            desc = jsonAdvert["description"].asString
        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {
            price = jsonAdvert["price"].asString
        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {
            address = jsonAdvert["address"].asString
        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {
            loc = jsonAdvert["location"].toString()
        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {

        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {

        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {
            url = jsonAdvert["url"].asString
        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }

        try {
            images = jsonAdvert["images"].asString
        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }
/*
        try {

        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }
*/


        //return Advert(0,"empty_title", "empty_caption", 1.2f,"undefined_location",null,null, 0)
        return Advert(
            id = null,
            name = title,
            description = desc,
            price = price,
            location = loc,
            address = address,
            url = url,
            imagesURL = images,
            null, 0
        )
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