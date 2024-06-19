package ru.amalkoott.advtapp.domain.worker

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import ru.amalkoott.advtapp.data.local.AppRepositoryDB
import ru.amalkoott.advtapp.di.AppModule
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.BlackList
import ru.amalkoott.advtapp.domain.notification.getNotification
import ru.amalkoott.advtapp.domain.notification.sendNotification
import java.time.LocalDate

private const val TAG = "CommonUpdateWorker"

class TestWorker(context: Context,workerParameters: WorkerParameters): CoroutineWorker(context,workerParameters){
    override suspend fun doWork(): Result {
        delay(30000)
        sendNotification(applicationContext,"Это что - Заголовок???","Ура смотри, все работаит уже целых $runAttemptCount раз!!!")
        return Result.retry()
    }
}
class UpdateWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters) {
    private var title = "TITLE_NOTIFICATION"
    private var message = "MESSAGE_NOTIFICATION"
    override suspend fun doWork(): Result {
        try {
            if(tags.contains("OneTimeUpdate")){
                // for periodic work realisation with OneTimeWorker todo демонстрация, потом убрать
                //delay(inputData.getInt("update_interval",5)*6000L)
                Log.d("*** Worker ***", "It's okay")
                delay(inputData.getInt("update_interval",5)*6000L)
            }else{
                // for PeriodicWorker
                if (runAttemptCount > 5) return Result.failure()
            }

            // api - for remote, repo - local database
            val id = inputData.getString("id")
            val repo = AppRepositoryDB(AppModule.provideAppDao(AppModule.provideDatabase(applicationContext)))

            val setId = id!!.toLong()

            val adverts = loadRemoteData()
            if (adverts.isNullOrEmpty()) return Result.retry()

            // проверить дубликаты
            if (isHavingDuplicateById(adverts,repo)) return Result.failure()

            val blackList = repo.getBlackList(setId)
            adverts.removeBlackAdverts(blackList.first())

            repo.deleteAdvertsBySet(setId)

            // insert new adverts
            adverts.forEach {
                it.adSetId = setId
                repo.addAdv(it)
            }

            val set = getSetFromInputData()
            set.adverts = adverts
            repo.updateSet(set)

            successfulSearchNotify()

            if(tags.contains("OneTimeUpdate")){
                return Result.retry()
            } else return Result.success()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            failedSearchNotify()
            return Result.failure()
        }
    }
    private fun successfulSearchNotify(){
        title = "Найдены новые объявления!"
        message = "Подборка ${inputData.getString("name")} обновилась! Нажмите, чтобы посмотреть..."
        sendNotification(applicationContext,title,message)
    }
    private fun failedSearchNotify(){
        title = "Что-то мешает поиску..."
        message = "Сейчас объявления не могут быть найдены. Нажмите для получения информации..." // todo во внутренние пуши подробную инфу
        sendNotification(applicationContext,title,message)
    }
    private fun MutableList<Advert>?.removeBlackAdverts(blackList: List<BlackList>) {
        if (blackList.isNotEmpty()){
            blackList.forEach{
                    blackList -> this!!.toList().forEach {
                    advert -> if (blackList.hash == advert.hash) {
                this.remove(advert)
                return@forEach
            } } } }
    }
    private suspend fun loadRemoteData(): MutableList<Advert>?{
       // val api = AppModule.provideServerApi(AppModule.provideInternetConnection(AppModule.provideHttpClient()))
     //   val adverts = api.get(getSearchFromInputData()).toMutableList()

        val test = "[" +
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
                "},\n" +
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
                "}," +
                "{\n" +
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
                "\"name\":\"Комната в 1-комн. квартире, 19.8 м², 5/5 этаж\",\n" +
                "\"price\":\"2899999\",\n" +
                "\"adSetId\":\"0\",\n" +
                "\"imagesURL\":\"https://img.dmclk.ru/s1200x800q80/vitrina/3a/d1/3ad1d40e5be79bb83a8cc0ffca42e6e7f4e06d39.webp\",\n" +
                "\"description\":\"Добрый день! Продаем СВЕТЛУЮ комнату с ЧИСТОЙ юридической историей в самом сердце Петербурга! Кстати, если Вы сейчас продаёте свою недвижимость, то мы можем сразу КУПИТЬ её:)Безопасно и с удовольствием юридически сопроводим Вашу сделку, деятельность нашего агентства застрахована на 20 000 000 р. Будем рады покупкам с нами и у нас!)\"\n" +
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
                "}\n" +
                "]"

        val adverts = Gson().fromJson(test,Array<Advert>::class.java).toMutableList()

        return if (adverts.isEmpty()) null else adverts
    }
    private suspend fun isHavingDuplicateById(adverts: List<Advert>, repo: AppRepositoryDB): Boolean{
        var result = false
        repo.loadAllAdsBySetFlow(inputData.getString("id")!!.toLong()).collect{
            it.forEach{ oldAdvert ->
                adverts.forEach { newAdvert ->
                    if (oldAdvert.hash == newAdvert.hash) {
                        result = true
                        return@collect
                    }
                }
            }
        }
        return result
    }
    private fun getSearchFromInputData(): JsonElement{
        val gson = Gson()
        val json = gson.fromJson(inputData.getString("search"), JsonElement::class.java)
        return json
    }
    private fun getSetFromInputData(): AdSet{
        val set = AdSet()
        val last_update = LocalDate.now()
        set.id = inputData.getString("id")!!.toLong()
        set.last_update = last_update
        set.update_interval = inputData.getInt("update_interval",15)
        set.name = inputData.getString("name")
        return set
    }
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            0, createNotification()
        )
    }
    private fun createNotification(): Notification{
        return getNotification(applicationContext,title,message)

    }
}
