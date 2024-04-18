package ru.amalkoott.advtapp.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AppRemoteRepository

class ServerRequestsRepository(val serverApi: ServerAPI):AppRemoteRepository {
    // получаем список объявлений (подборку) по параметрам
    override suspend fun get(): List<SearchParameters> = withContext(Dispatchers.IO){
        var issues: List<SearchParameters>?
        try {
            val result = serverApi.get()

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

    override suspend fun update(set: AdSet): Boolean = withContext(Dispatchers.IO) {
        var issue = toGitHubIssue(note)
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
    private fun toNote(issue: GitHubIssue): Note {
        return Note(issue.title, issue.body, issue.number)
    }
    private fun toGitHubIssue(note: Note): GitHubIssue {
        return GitHubIssue(note.remoteId, note.title, note.text)
    }
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