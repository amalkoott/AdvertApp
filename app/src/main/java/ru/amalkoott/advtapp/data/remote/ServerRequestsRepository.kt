package ru.amalkoott.advtapp.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppRemoteRepository
import java.time.LocalDate
import javax.inject.Inject

class ServerRequestsRepository @Inject constructor(val serverApi: ServerAPI):AppRemoteRepository {

    suspend fun testProvide(){
       // Log.d("MASHALA","!!!!!!!!!!!!!!!!!!!!!!!!!!${this.toString()}")
    }
    // получаем список объявлений (подборку) по параметрам
    override suspend fun get(parameters: RealEstateSearchParameters): List<Advert> = withContext(Dispatchers.IO){
        // отправляем на сервак параметры
        val result = mutableListOf<Advert>()
        val resultList: JsonArray
        // делаем get запрос - получаем Json-строку
        // конвертим json-строку в подборку
        try {
            val gson = Gson()
            val json = gson.toJsonTree(parameters)

            val response = serverApi.get(json as JsonObject)

            if(!response.isSuccessful){
                //Log.w("AppRepositoryApi","Can't get issues $response")
                return@withContext emptyList()
            }
            resultList = response.body()!!.asJsonArray
        }catch (e: Exception){
            //Log.w("ServerRequestRepository","Can't get issues", e)
            if(e.message == "timeout"){
               // Log.w("ServerRequestRepository","TIMEOUT")
                return@withContext emptyList()
            }
            if(e.message?.contains("failed to connect") == true){
                //Log.w("ServerRequestRepository","FAILED TO CONNECT")
                return@withContext emptyList()
            }
            //Log.w("ServerRequestRepository","UNDEFINED ERROR")

            return@withContext emptyList()
        }
        val set = resultList.map {
            toAdvert(it)
        } ?: emptyList()
        return@withContext set
    }

    override suspend fun get(json: JsonElement): List<Advert> = withContext(Dispatchers.IO){
        // val result: JsonArray?
        // todo отправляем на сервак параметры (принимается вродеп норм)
        val result = mutableListOf<Advert>()
        val resultList: JsonArray
        // делаем get запрос - получаем Json-строку
        // конвертим json-строку в подборку
        try {
            val response = serverApi.get(json as JsonObject)

            if(!response.isSuccessful){
               // Log.w("AppRepositoryApi","Can't get issues $response")
                return@withContext emptyList()
            }
            resultList = response.body()!!.asJsonArray
        }catch (e: Exception){
            //Log.w("ServerRequestRepository","Can't get issues", e)
            if(e.message == "timeout"){
              //  Log.w("ServerRequestRepository","TIMEOUT")
                return@withContext emptyList()
            }
            if(e.message?.contains("failed to connect") == true){
               // Log.w("ServerRequestRepository","FAILED TO CONNECT")
                return@withContext emptyList()
            }
           // Log.w("ServerRequestRepository","UNDEFINED ERROR")

            return@withContext emptyList()
        }
        val set = resultList.map {
            toAdvert(it)
        } ?: emptyList()
        return@withContext set

    }

    private fun toAdvert(request: JsonElement): Advert {
//      Advert(0,"empty_title", "empty_caption", 1.2f,"undefined_location",null,null, 0)
        val jsonAdvert = request.asJsonObject
        var hash: String? = null
        var title: String? = null
        var desc: String? = null
        var price: String? = null
        var priceInfo: String? = null
        var address: String? = null
        var lat: Double? = null
        var lon: Double? = null
        var published: String? = null
        var updated: String? = null
        var loc: String? = null
        var url: String? = null
        var images: String? = null
        try {
            hash = jsonAdvert["hash"].asString.replace("\"","")
        }catch (e: NullPointerException){
            //Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            //Log.d("ServerRequestException", e.toString())
        }
        try {
            title = jsonAdvert["title"].asString.replace("\"","")
        }catch (e: NullPointerException){
           // Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
           // Log.d("ServerRequestException", e.toString())
        }

        try {
            desc = jsonAdvert["caption"].asString.replace("\"","")
        }catch (e: NullPointerException){
           // Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            //Log.d("ServerRequestException", e.toString())
        }

        try {
            price = jsonAdvert["price"].asString.replace("\"","")
        }catch (e: NullPointerException){
           // Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            //Log.d("ServerRequestException", e.toString())
        }

        try {
            priceInfo = jsonAdvert["priceInfo"].asString.replace("\"","")
        }catch (e: NullPointerException){
          //  Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
           // Log.d("ServerRequestException", e.toString())
        }

        try {
            val coord = jsonAdvert["coordinates"].asString.split(" ")
            lat = coord[0].toDoubleOrNull()
            lon = coord[1].toDoubleOrNull()
        }catch (e: NullPointerException){
           // Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
        //    Log.d("ServerRequestException", e.toString())
        }

        try {
            address = jsonAdvert["location"].asString.replace("\"","")
        }catch (e: NullPointerException){
          //  Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
           // Log.d("ServerRequestException", e.toString())
        }

        try {
            loc = jsonAdvert["travel"].toString().replace("\"","")
        }catch (e: NullPointerException){
          //  Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
           // Log.d("ServerRequestException", e.toString())
        }

        try {
            published = jsonAdvert["publishedDate"].toString().replace("\"","")
        }catch (e: NullPointerException){
           // Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
           // Log.d("ServerRequestException", e.toString())
        }

        try {
            updated = jsonAdvert["updatedDate"].toString().replace("\"","")
        }catch (e: NullPointerException){
         //   Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
          //  Log.d("ServerRequestException", e.toString())
        }

        try {
            url = jsonAdvert["url"].asString.replace("\"","")
        }catch (e: NullPointerException){
           // Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
           // Log.d("ServerRequestException", e.toString())
        }

        try {
            images = jsonAdvert["img"].asString.replace("\"","")
        }catch (e: NullPointerException){
           // Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
           // Log.d("ServerRequestException", e.toString())
        }
/*
        try {

        }catch (e: NullPointerException){
            Log.d("ConvertingToAdvert","")
        }catch (e:Exception){
            Log.d("ServerRequestException", e.toString())
        }
*/

        return Advert(
            hash = hash,
            id = null,
            name = title,
            description = desc,
            price = price,
            location = loc,
            address = address,
            url = url,
            imagesURL = images,
            additionalParam = null,
            adSetId = 0,

            lat = lat,
            lon = lon,
            updated = updated,
            published = published,
            priceInfo = priceInfo
        )
    }
    override suspend fun checkServer(): JsonObject? = withContext(Dispatchers.IO){
        val result: JsonObject?
        try {
            val response = serverApi.check()

            if (!response.isSuccessful){
                //Log.w("AppRepositoryApi","Can't get server $response")
                return@withContext null
            }
            result = response.body()
            //Log.w("Response from server","$result")
        }catch (e:Exception){
            //Log.w("AppRepositoryApi","$e")
            return@withContext null
        }
        return@withContext  result
    }
}