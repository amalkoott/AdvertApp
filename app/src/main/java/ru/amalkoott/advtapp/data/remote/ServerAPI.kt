package ru.amalkoott.advtapp.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerAPI {
    // получение новой подборки
    @GET("/search")
    suspend fun get(@Query("parameters") parameters: JsonObject): Response<JsonArray?>

    @GET("/check")
    suspend fun check(): Response<JsonObject?>

    /*
    // обновление заданной подборки
    @GET("issues/{number}")
    suspend fun update(@Query("parameters") parameters: JsonObject): Response<JsonArray?>
    */
}

/*

package ru.protei.malkovaar.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NotesGitHubApi {

    @GET("issues?state=open")
    suspend fun getList(): Response<List<GitHubIssue>>

    @POST("issues")
    suspend fun add(@Body issue: GitHubIssue): Response<GitHubIssue>

    @PATCH("issues/{number}")
    suspend fun update(@Path("number") number: Long, @Body issue: GitHubIssue): Response<GitHubIssue>
}

 */