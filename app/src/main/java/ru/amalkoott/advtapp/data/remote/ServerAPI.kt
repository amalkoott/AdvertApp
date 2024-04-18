package ru.amalkoott.advtapp.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ServerAPI {
    // получение новой подборки
    @GET("/search")
    suspend fun get(): Response<List<SearchParameters>>

    // обновление заданной подборки
    @PATCH("issues/{number}")
    suspend fun update(@Path("number") number: Long, @Body issue: ServerSet): Response<ServerSet>
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