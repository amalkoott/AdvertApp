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

    @GET("/")
    suspend fun check(): Response<JsonObject?>

}
