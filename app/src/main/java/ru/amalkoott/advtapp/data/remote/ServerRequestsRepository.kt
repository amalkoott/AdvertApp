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
            response.body()!!.asJsonArray
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

/*
        val test = "[{\n" +
                "   \"url\":\"https://spb.domclick.ru/card/sale__room__1965656400 https://spb.cian.ru/sale/flat/295533366/\",\n" +
                "   \"name\":\"Комната в 1-комн. квартире, 24.7 м², 4/5 этаж\",\n" +
                "   \"address\":\"Ломоносовская 13 мин. пешком; Пролетарская 23 мин. пешком;\",\n" +
                "   \"location\":\"Санкт-Петербург, бульвар Красных Зорь, 6\",\n" +
                "   \"price\":\"4999999\",\n" +
                "\"adSetId\":\"0\",\n" +
                "   \"priceInfo\":\"Без комиссии, 182 186 ₽/м²\",\n" +
                "   \"description\":\"Добрый день!\\n\\nПродаем ЕВРО-ДВУХКОМНАТНЫЙ ВАРИАНТ в пешей доступности от станции метро \\\"ЛОМОНОВСКАЯ\\\" (по документам доля в праве общей долевой собственности).\\nВесь этаж - это одна большая квартира, с одним кадастровым номером.\\nЕсть длинный коридор и никаких мест общего пользования на нем.\\nУ каждой из комнат - свой отдельный вход и свой санузел внутри.\\n\\nПо документам - это ДОЛЯ в праве общей долевой собственности на всю КВАРТИРУ, что соответствует определенным помещениям на поэтажном плане.\\nТаким образом, фактически, ничем не отличить от квартиры, по документам же - это доля. Подходит под ипотеку Банков (Росбанк и иные, но не сбер).\\n\\n! Продажа БЕЗ КОМИССИИ !\\n\\nКстати, если Вы сейчас продаете свою квартиру, то мы можем сразу КУПИТЬ ее.\\nБезопасно и с удовольствием юридически сопроводим Вашу сделку, деятельность нашего агентства застрахована на 20 000 000 р.\\nБудем рады покупкам с нами и у нас!)\\n\\nПРО ДОКУМЕНТЫ\\n\\nСобственники будут присутствовать на сделке ЛИЧНО (а не по доверенности).\\nВ собственности более 5 лет.\\nБЕЗ ОБРЕМЕНЕНИЙ.\\nПодходит под ипотеку Росбанка и иных банков (но не сбер), при необходимости поможем с одобрением на наиболее выгодных условиях.\\n\\nПо документам это доля в праве общей долевой собственности.\\n\\nПРО ОБЪЕКТ\\n\\nДействительно КОМФОРТНАЯ планировка.\\n\\nОбщая площадь составляет 30.1м2.\\nМетраж жилой зоны (комнаты) составляет 15.2 м2.\\nМетраж кухни 9.5 м2.\\n\\nОтделена зона входа, в виде функциональной прихожей, с одной стороны которой оборудован шкаф-купе, с другой санузел и душевая (соответствует плану по ЕГРН).\\n\\nНаходится на 4 этаже.\\nИз окон открывается вид в тихий и зеленый двор.\\n\\nПРО ДОМ И ИНФРАСТРУКТУРУ\\n\\nПерекрытия ЖЕЛЕЗОБЕТОННЫЕ.\\nПостроен в 1961 году и проверен временем :)\\n\\nДом находится в НЕВСКОМ РАЙОНЕ.\\nВ шаговой доступности МЕТРО ЛОМОНОСОВСКАЯ, остановки общественного наземного транспорта (трамвай, троллейбус, автобус) удобное и быстрое сообщение с ЦЕНТРОМ ГОРОДА.\\n\\nДо Невского проспекта несколько остановок на метро. Можно добраться в ЛЮБУЮ ТОЧКУ ГОРОДА.\\n\\nВо дворе всегда можно найти ПАРКОВОЧНОЕ МЕСТО. Развита инфраструктура, есть все необходимое для жизни.\\n\\nС радостью проконсультируем по сделке.\\nТорг уместен, но после просмотра :)\\nЗвоните!\",\n" +
                "   \"imagesURL\":\"https://images.cdn-cian.ru/images/2040069799-1.jpg https://images.cdn-cian.ru/images/2040069824-1.jpg https://images.cdn-cian.ru/images/2040069870-1.jpg https://images.cdn-cian.ru/images/2040069880-1.jpg https://images.cdn-cian.ru/images/2040069886-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg https://images.cdn-cian.ru/images/2040069897-1.jpg\",\n" +
                "   \"coordinates\":\"59.851137 30.028201\",\n" +
                "   \"publishedDate\":\"2024-04-15T16:53:16.178316+00:00\",\n" +
                "   \"updatedDate\":\"2024-05-17T15:43:06.222219+00:00\",\n" +
                "   \"additionalParam\":\"{\\\"aboutFlat\\\":{\\\"Недвижимость\\\":\\\"Комната\\\",\\\"Этаж\\\":\\\"4\\\",\\\"ОбщаяПлощадь\\\":\\\"650,1\\\",\\\"ЖилаяПлощадь\\\":\\\"24,7\\\",\\\"ПлощадьКухни\\\":\\\"9,5\\\",\\\"Комнат\\\":\\\"1\\\",\\\"ВысотаПотолков\\\":\\\"2,75\\\",\\\"Санузел\\\":\\\"Раздельный\\\",\\\"ВидИзОкон\\\":\\\"-\\\",\\\"Ремонт\\\":\\\"Евроремонт\\\"},\\\"aboutHouse\\\":{\\\"ГодПостройки\\\":\\\"1961\\\",\\\"ТипСтен\\\":\\\"Панель\\\",\\\"Перекрытия\\\":\\\"Железобетонные\\\",\\\"Этажность\\\":\\\"5\\\",\\\"Парковка\\\":\\\"ВоДворе\\\",\\\"Подъезды\\\":\\\"1\\\",\\\"Лифт\\\":\\\"Пассажирский\\\",\\\"Отопление\\\":\\\"Центральное\\\",\\\"Аварийность\\\":\\\"Нет\\\",\\\"Газоснабжение\\\":\\\"Центральное\\\"},aboutSeller:{\\\"Агенство\\\":\\\"ЛабецкийНедвижимость\\\",\\\"Риэлтор\\\":{\\\"Домклик\\\":\\\"ДаниилУдовицкий\\\",\\\"ЦИАН\\\":\\\"НикитаЛабецкий\\\"},\\\"Телефон\\\":{\\\"Домклик\\\":\\\"+79062585011\\\",\\\"ЦИАН\\\":\\\"+7981226-82-05\\\"},\\\"Документы\\\":{\\\"Домклик\\\":\\\"СберID\\\",\\\"ЦИАН\\\":\\\"Паспорт\\\"},\\\"Профиль\\\":{\\\"Домклик\\\":\\\"https://agencies.domclick.ru/agent/3716624?region_id=70293348-4ea2-4741-934f-c1e9549d1fba?utm_content=offers.agent\\\",\\\"ЦИАН\\\":\\\"https://spb.cian.ru/agents/9447844/\\\"}}}\",\n" +
                "   \"hash\":\"-180609\"\n" +
                "},\n" +
                "{\n" +
                "\"name\":\"Комната в 1-комн. квартире, 20 м², 2/5 этаж\",\n" +
                "\"price\":\"3500000\",\n" +
                "   \"address\":\"Звездная 13 мин. пешком; Московская 15 мин. транспортом;\",\n" +
                "   \"location\":\"Санкт-Петербург, пр-кт Юрия Гагарина, 42\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://img.dmclk.ru/s1200x800q80/vitrina/owner/b9/6b/b96b9e5c747f4a51a6446692e12641d4.webp\",\n" +
                "\"description\":\"Продается светлая, просторная, теплая и уютная комната площадью 20 кв.м. в малонаселенной трехкомнатной квартире, расположенная на втором этаже 5-ти этажного жилого дома.\"\n" +
                "},\n" +

                "{\n" +
                "   \"url\":\"https://spb.cian.ru/sale/flat/297647181/\",\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната в 1-комн. квартире, 19.8 м², 5/5 этаж\",\n" +
                "\"price\":\"2899999\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://img.dmclk.ru/s1200x800q80/vitrina/3a/d1/3ad1d40e5be79bb83a8cc0ffca42e6e7f4e06d39.webp\",\n" +
                "\"description\":\"Добрый день! Продаем СВЕТЛУЮ комнату с ЧИСТОЙ юридической историей в самом сердце Петербурга! Кстати, если Вы сейчас продаёте свою недвижимость, то мы можем сразу КУПИТЬ её:)Безопасно и с удовольствием юридически сопроводим Вашу сделку, деятельность нашего агентства застрахована на 20 000 000 р. Будем рады покупкам с нами и у нас!)\"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 15 м²\",\n" +
                "\"price\":\"1999000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://img.dmclk.ru/s1200x800q80/vitrina/owner/f1/d9/f1d9a76b7f87426c8a780c20da1e4a1c.webp\",\n" +
                "\"description\":\"Отличное предложение!\n" +
                "Комната 14.7 метров ( комната номер 3)\n" +
                "в 7 квартире с двумя большими окнами квадратной формы.\n" +
                "Места общего пользования содержатся в чистоте .\n" +
                "Высота потолков 3.34 \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 18 м², 6/7 этаж\",\n" +
                "\"price\":\"2100000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://img.dmclk.ru/vitrina/owner/05/70/05705278c28b4df6bf6bcf31f388e5ff.webp\",\n" +
                "\"description\":\"Шикарное место , пешая доступность до метро Василеостровская .\n" +
                "\n" +
                "Один взрослый собственник,\n" +
                "Более 5 лет владения. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната в 4-комн. квартире, 10.2 м², 2/7 этаж\",\n" +
                "\"price\":\"3000000 ₽\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://img.dmclk.ru/s1200x800q80/vitrina/owner/0b/57/0b571bf5f0124a2ca32b7ffaef0060ce.webp\",\n" +
                "\"description\":\"Вашему вниманию представлена уютная комната в самом центре Санкт-Петербурга площадью 10,2 м2 в 4-х комнатной квартире. Квартира расположена на 2 этаже старинного дома 1865 года постройки. В комнате сделан косметически \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 11,2 м²\",\n" +
                "\"price\":\"1500000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2083734748-1.jpg\",\n" +
                "\"description\":\"Федеральная компания ЖИЛФОНД предлагает вам компактную, тихую комнату с лоджией и хорошим видом из окна. Подходит под ипотеку Сбербанк. Все отказы собраны. Продается уютная комната на проспекте Маршала Жукова, дом 20. Общая площадь комнаты составляет 11,2 кв.м. Комната находится на 5 этаже 15-этажного дома. \"\n" +
                "},\n" +

                "{\n" +
                "\"name\":\"Комната, 19/19 м²\",\n" +
                "\"price\":\"3800000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2192102279-1.jpg\",\n" +
                "\"description\":\"Продается светлая комната ,с перспективой сделать студию (помещение уже подготовлено для ванной комнаты) , в знаменитом доме им. Полежаева ,где проходили съемки фильма 'Мастер и Маргарита'.Объект культурного наследия региональн\"\n" +
                "},\n" +
                "{\n" +
                "\"name\":\"Комната, 24/15,9 м²\",\n" +
                "\"price\":\"4890000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://images.cdn-cian.ru/images/2024833861-1.jpg\",\n" +
                "\"description\":\"Предлагаем вашему вниманию уникальное предложение - просторную и уютную комнату в центре Санкт-Петербурга на улице Большая Зеленина, это доля в квартире, в собственность переходит определенная комната. По факту это о\"\n" +
                "}\n" +

                */
                /*
                "{\"url\":\"https://spb.domclick.ru/card/sale__room__2058391139\",\"name\":\"Комната в 1-комн. квартире, 15 м², 4/5 этаж\",\n" +
                "   \"address\":\"Электросила 14 мин. пешком; Парк Победы 20 мин. пешком; Московские ворота 31 мин. пешком;\",\n" +
                "   \"location\":\"Санкт-Петербург, Благодатная улица, 40\",\n" +
                "   \"price\":\"2000000\",\n" +
                "\"adSetId\":\"0\",\n" +
                "   \"priceInfo\":\"133333 ₽/м²\",\n" +
                "   \"description\":\"На редкость УЮТНАЯ, ЧИСТАЯ, СВЕТЛАЯ, с прекрасным интерьером, с отличным ремонтом комната. Места общего пользования содержатся в чистоте. В квартире спокойная обстановка, маргинальных личностей в квартире нет. Рядом с домом остановка общественного транспорта. Очень много зелени в большом дворе. Экологически благополучное месторасположение дома. ПРЯМАЯ ПРОДАЖА. СВОБОДНА. НИКТО НЕ ПРОПИСАН. Документы готовы, решены все вопросы с уведомлениями. Моментальный выход на сделку (хоть на следующий после просмотра день). В собственности более 15 лет (приватизация). Сделок с комнатой не было. АГЕНТОВ БЕЗ ПОКУПАТЕЛЕЙ - ПРОСЬБА НЕ ЗВОНИТЬ ! АГЕНТ ЕСТЬ.\",\n" +
                "   \"imagesURL\":\"https://img.dmclk.ru/s1200x800q80/vitrina/owner/49/20/4920a2547f0949e591e4fb86c5b42148.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/5d/a9/5da9a8d8ff6640aaa0b9e9790781aeda.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp https://img.dmclk.ru/s1200x800q80/vitrina/owner/cd/13/cd1363bf4fcf42a08a7acc09425ff1be.webp\",\n" +
                "   \"coordinates\":\"59.851137 30.028201\",\n" +
                "   \"publishedDate\":\"2024-04-15T16:53:16.178316+00:00\",\n" +
                "   \"updatedDate\":\"2024-05-17T15:43:06.222219+00:00\",\n" +
                "   \"additionalParam\":\"{\\\"aboutFlat\\\":{\\\"Недвижимость\\\":\\\"Комната\\\",\\\"Этаж\\\":\\\"4\\\",\\\"ОбщаяПлощадь\\\":\\\"988\\\",\\\"ЖилаяПлощадь\\\":\\\"15\\\",\\\"ПлощадьКухни\\\":\\\"29\\\",\\\"Комнат\\\":\\\"1\\\",\\\"ВысотаПотолков\\\":\\\"3\\\",\\\"Санузел\\\":\\\"Раздельный\\\",\\\"ВидИзОкон\\\":\\\"НаУлицу\\\",\\\"Ремонт\\\":\\\"Евроремонт\\\"},\\\"aboutHouse\\\":{\\\"ГодПостройки\\\":\\\"1951\\\",\\\"ТипСтен\\\":\\\"Блочный\\\",\\\"Перекрытия\\\":\\\"Железобетонные\\\",\\\"Этажность\\\":\\\"5\\\",\\\"Парковка\\\":\\\"ВоДворе\\\",\\\"Подъезды\\\":\\\"-\\\",\\\"Лифт\\\":\\\"Пассажирский\\\",\\\"Отопление\\\":\\\"Котел\\\",\\\"Аварийность\\\":\\\"-\\\",\\\"Газоснабжение\\\":\\\"Центральное\\\"},aboutSeller:{\\\"Агенство\\\":\\\"ООО'Панорама'\\\",\\\"Риэлтор\\\":\\\"МаринаДербикова\\\",\\\"Телефон\\\":\\\"+79221245860\\\",\\\"Документы\\\":{\\\"Домклик\\\":\\\"-\\\"},\\\"Профиль\\\":{\\\"Домклик\\\":\\\"https://agencies.domclick.ru/agent/2928847?region_id=58d15e6b-f489-4e5f-9a98-7036a86f05ab?utm_content=offers.agent\\\"}}}\",\n" +
                "   \"hash\":\"-18060989\"\n" +
                "}" +
*/

                "]"
        val array = """[{"item":"one"},{"item":"two"},{"item":"three"}]"""
        val json = Gson().fromJson(test,Array<Advert>::class.java).toList()
        resultList = Gson().toJsonTree(json).asJsonArray

        val set = resultList.map {
            toAdvert(it)
        } ?: emptyList()

        Log.d("TIME_END",System.nanoTime().toString())
        return@withContext json//set
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

