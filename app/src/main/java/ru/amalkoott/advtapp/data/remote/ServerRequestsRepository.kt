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
import javax.inject.Inject

class ServerRequestsRepository @Inject constructor(val serverApi: ServerAPI):AppRemoteRepository {

    suspend fun testProvide(){
       // Log.d("MASHALA","!!!!!!!!!!!!!!!!!!!!!!!!!!${this.toString()}")
    }
    // получаем список объявлений (подборку) по параметрам
    override suspend fun get(parameters: RealEstateSearchParameters): List<Advert> = withContext(Dispatchers.IO){
        Log.d("TIME_START",System.nanoTime().toString())
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
        Log.d("TIME_END",System.nanoTime().toString())
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
val test = "[{"+
   "\"url\":\"https://spb.domclick.ru/card/rent__house__2058860540\","+
   "\"title\":\"Сдаётся дом, 80 м²\","+
   "\"travel\":\"\","+
   "\"location\":\"Ленинградская область, Всеволожский, Лесколовское сельское поселение, массив Кискелово, садоводческое некоммерческое товарищество Самоцветы\","+
   "\"price\":\"80000\","+
   "\"priceInfo\":\"Без комиссии\","+
   "\"caption\":\"Сдаётся с Сентября месяца! На лето дом уже сдан!!Дом 80 кв. м. Сдаётся вместе с участком 20 соток. Участок изолированный, можно пользоваться, сажать. На участке ручей, плодовый сад, место для костра. В доме три помещения, 5-6 спальных мест, туалет, душ, вода, безлимитный интернет (оптика) . Холодильник, газовая плита (баллон). В доме тепло (тепловой насос), два камина. Две больших террасы. На участке русская баня с душем, (водопровод) стиральная машина. Напротив хвойный лес, тишина. 30 км от города. 80 тысяч в месяц.Адрес: Всеволожский район. Деревня Кискелово, СНТ Самоцветы. Сдаётся с Сентября месяца\","+
   "\"img\":\"https://img.dmclk.ru/s1200x800q80/vitrina/owner/a5/e0/a5e04fd5b528424aa391e992302a67b0.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/28/f0/28f02bb1879f4857a399f96f3f21bb23.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/21/ba/21ba5e25ab3e4120a77580afc1bdb715.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/41/ec/41eca142030e44d6a24ac6317883d807.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/fa/98/fa987595bce44c99b9a2ac9414b053ef.webp\","+
   "\"coordinates\":\"60.268348 30.507083\","+
   "\"publishedDate\":\"1.06.2024\","+
   "\"updatedDate\":\"3.06.2024\","+
   "},"+
"{"+
    "\"url\":\"https://spb.cian.ru/rent/suburban/302981303/\","+
    "\"title\":\"Сдается 2-этажный дом, 170 м²\","+
    "\"travel\":\"\","+
    "\"location\":\"Ленинградская область, Всеволожский район, Заневское городское поселение, Колос СНТ, 215\","+
    "\"price\":\"150000\","+
    "\"priceInfo\":\"Предоплата 100%\","+
    "\"caption\":\"Дом на все лето. Сдам коттедж на период июнь-сентябрь.С возможностью постоянного проживания.Большой, вместительный дом на 170 кв.м. Четыре спальни: Три спальни на втором этаже, одна спальня на первом. Большая гостиная, оборудованная кухня, два санузла, на первом и втором этажах. Действующий камин. Уютная крытая веранда. Газовое отопление.На участке 10 соток. С отдельно стоящей баней (на втором этаже бане - гостевой домик со спальными \","+
    "\"img\":\"https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg \","+
    "\"coordinates\":\"59.919369 30.594795\","+
    "\"publishedDate\":\"\","+
    "\"updatedDate\":\"\","+
   "},"+
        "{"+
        "\"url\":\"https://spb.cian.ru/rent/suburban/302981303/\","+
        "\"title\":\"Сдается 2-этажный дом, 170 м²\","+
        "\"travel\":\"\","+
        "\"location\":\"Ленинградская область, Всеволожский район, Заневское городское поселение, Колос СНТ, 215\","+
        "\"price\":\"150000\","+
        "\"priceInfo\":\"Предоплата 100%\","+
        "\"caption\":\"Дом на все лето. Сдам коттедж на период июнь-сентябрь.С возможностью постоянного проживания.Большой, вместительный дом на 170 кв.м. Четыре спальни: Три спальни на втором этаже, одна спальня на первом. Большая гостиная, оборудованная кухня, два санузла, на первом и втором этажах. Действующий камин. Уютная крытая веранда. Газовое отопление.На участке 10 соток. С отдельно стоящей баней (на втором этаже бане - гостевой домик со спальными \","+
        "\"img\":\"https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg \","+
        "\"coordinates\":\"59.919369 30.594795\","+
        "\"publishedDate\":\"\","+
        "\"updatedDate\":\"\","+
        "},"+"{"+
        "\"url\":\"https://spb.cian.ru/rent/suburban/302981303/\","+
        "\"title\":\"Сдается 2-этажный дом, 170 м²\","+
        "\"travel\":\"\","+
        "\"location\":\"Ленинградская область, Всеволожский район, Заневское городское поселение, Колос СНТ, 215\","+
        "\"price\":\"150000\","+
        "\"priceInfo\":\"Предоплата 100%\","+
        "\"caption\":\"Дом на все лето. Сдам коттедж на период июнь-сентябрь.С возможностью постоянного проживания.Большой, вместительный дом на 170 кв.м. Четыре спальни: Три спальни на втором этаже, одна спальня на первом. Большая гостиная, оборудованная кухня, два санузла, на первом и втором этажах. Действующий камин. Уютная крытая веранда. Газовое отопление.На участке 10 соток. С отдельно стоящей баней (на втором этаже бане - гостевой домик со спальными \","+
        "\"img\":\"https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg \","+
        "\"coordinates\":\"59.919369 30.594795\","+
        "\"publishedDate\":\"\","+
        "\"updatedDate\":\"\","+
        "},"+"{"+
        "\"url\":\"https://spb.cian.ru/rent/suburban/302981303/\","+
        "\"title\":\"Сдается 2-этажный дом, 170 м²\","+
        "\"travel\":\"\","+
        "\"location\":\"Ленинградская область, Всеволожский район, Заневское городское поселение, Колос СНТ, 215\","+
        "\"price\":\"150000\","+
        "\"priceInfo\":\"Предоплата 100%\","+
        "\"caption\":\"Дом на все лето. Сдам коттедж на период июнь-сентябрь.С возможностью постоянного проживания.Большой, вместительный дом на 170 кв.м. Четыре спальни: Три спальни на втором этаже, одна спальня на первом. Большая гостиная, оборудованная кухня, два санузла, на первом и втором этажах. Действующий камин. Уютная крытая веранда. Газовое отопление.На участке 10 соток. С отдельно стоящей баней (на втором этаже бане - гостевой домик со спальными \","+
        "\"img\":\"https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg \","+
        "\"coordinates\":\"59.919369 30.594795\","+
        "\"publishedDate\":\"\","+
        "\"updatedDate\":\"\","+
        "},"+"{"+
        "\"url\":\"https://spb.cian.ru/rent/suburban/302981303/\","+
        "\"title\":\"Сдается 2-этажный дом, 170 м²\","+
        "\"travel\":\"\","+
        "\"location\":\"Ленинградская область, Всеволожский район, Заневское городское поселение, Колос СНТ, 215\","+
        "\"price\":\"150000\","+
        "\"priceInfo\":\"Предоплата 100%\","+
        "\"caption\":\"Дом на все лето. Сдам коттедж на период июнь-сентябрь.С возможностью постоянного проживания.Большой, вместительный дом на 170 кв.м. Четыре спальни: Три спальни на втором этаже, одна спальня на первом. Большая гостиная, оборудованная кухня, два санузла, на первом и втором этажах. Действующий камин. Уютная крытая веранда. Газовое отопление.На участке 10 соток. С отдельно стоящей баней (на втором этаже бане - гостевой домик со спальными \","+
        "\"img\":\"https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg \","+
        "\"coordinates\":\"59.919369 30.594795\","+
        "\"publishedDate\":\"\","+
        "\"updatedDate\":\"\","+
        "},"+"{"+
        "\"url\":\"https://spb.cian.ru/rent/suburban/302981303/\","+
        "\"title\":\"Сдается 2-этажный дом, 170 м²\","+
        "\"travel\":\"\","+
        "\"location\":\"Ленинградская область, Всеволожский район, Заневское городское поселение, Колос СНТ, 215\","+
        "\"price\":\"150000\","+
        "\"priceInfo\":\"Предоплата 100%\","+
        "\"caption\":\"Дом на все лето. Сдам коттедж на период июнь-сентябрь.С возможностью постоянного проживания.Большой, вместительный дом на 170 кв.м. Четыре спальни: Три спальни на втором этаже, одна спальня на первом. Большая гостиная, оборудованная кухня, два санузла, на первом и втором этажах. Действующий камин. Уютная крытая веранда. Газовое отопление.На участке 10 соток. С отдельно стоящей баней (на втором этаже бане - гостевой домик со спальными \","+
        "\"img\":\"https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869157-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185862403-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869169-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg https://images.cdn-cian.ru/images/dom-kolos-2185869148-1.jpg \","+
        "\"coordinates\":\"59.919369 30.594795\","+
        "\"publishedDate\":\"\","+
        "\"updatedDate\":\"\","+
        "}]"

