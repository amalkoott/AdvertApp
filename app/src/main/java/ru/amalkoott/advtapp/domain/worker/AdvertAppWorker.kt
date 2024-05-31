package ru.amalkoott.advtapp.domain.worker

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
//import androidx.hilt.work.HiltWorker
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.amalkoott.advtapp.MainActivity
import ru.amalkoott.advtapp.data.local.AppDatabase
import ru.amalkoott.advtapp.data.local.AppRepositoryDB
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.data.remote.ServerAPI
import ru.amalkoott.advtapp.data.remote.ServerRequestsRepository
import ru.amalkoott.advtapp.di.AppModule
import ru.amalkoott.advtapp.di.BindModule
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppRepository
import ru.amalkoott.advtapp.domain.BlackList
import ru.amalkoott.advtapp.domain.notification.getNotification
import ru.amalkoott.advtapp.domain.notification.sendNotification
import ru.amalkoott.advtapp.ui.advert.view.AppViewModel
import java.lang.Math.abs
import java.lang.Thread.sleep
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "CommonUpdateWorker"
/*
class TestUpdateWorker (context: Context, workerParameters: WorkerParameters) : Worker(context,workerParameters){

    val appDao = AppModule.provideAppDao(AppModule.provideDatabase(context))
    val api = AppModule.provideServerApi(AppModule.provideInternetConnection(AppModule.provideHttpClient()))
    @OptIn(DelicateCoroutinesApi::class)
    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        try {
            val map = inputData.keyValueMap
            GlobalScope.launch {
                Log.d("DatabaseID",AppModule.provideDatabase(applicationContext).toString())
                api.testProvide()
            }
            Log.d(TAG+"DATA", "data has been got")

/*
            val set = inputData.keyValueMap
            GlobalScope.launch {
                update(set["set"] as AdSet)
            }
            */
            Log.d(TAG, "doWork: end")

            val notificationManager = NotificationManagerCompat.from(applicationContext)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                // TODO потом ретюрнить если разрешение не выдано return
            }

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(1, createNotification()) // посылаем уведомление
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return Result.retry()
    }

    override fun getForegroundInfo(): ForegroundInfo {
        //return super.getForegroundInfo()
        return ForegroundInfo(
            0, createNotification()
        )
    }

    private fun createNotification(): Notification{
        return getNotification(applicationContext,"","")

    }
}
// Обновление данных:
// 1) запрос на сервер
// 2) получение результата
// 3) результат не пустой -> обновление в БД
// 4) результат пустой -> ничего не делаем
class CreateWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters){

    override suspend fun doWork(): Result {
            try {
                // если тут проблем нет -> спокойно переходим к взаимодействию с api и repo
                // для запроса на сервер
                val api = AppModule.provideServerApi(AppModule.provideInternetConnection(AppModule.provideHttpClient()))
                // для работы с БД
                val repo = AppRepositoryDB(AppModule.provideAppDao(AppModule.provideDatabase(applicationContext)))

                // INSERT
                // перевод параметров и поиск на сервере
                val search = getSearchFromInputData()
                val adverts = api.get(search)

                if (adverts.isEmpty()) return Result.failure()

                val adSet = AdSet()
                // поправляем подборку
                val last_update = LocalDate.parse(inputData.getString("last_update"))

                adSet.name = inputData.getString("name")
                //adSet.category = inputData.getString("category")
                adSet.caption = search.toString()//toCaption(search)
                adSet.adverts = adverts
                adSet.update_interval = inputData.getInt("update_interval",15)
                adSet.last_update = last_update

                // добавляем новую подборку в БД
                repo.addSet(adSet)

                Log.d("UpdateWorker","Update is successful, tag - $tags")

                return if(tags.firstOrNull() == "OneTimeUpdate") Result.retry()
                else Result.success()

            } catch (e: InterruptedException) {
                e.printStackTrace()
                return Result.failure()
            }
    }

    private fun getSearchFromInputData(): JsonElement{
        val gson = Gson()
        val json = gson.fromJson(inputData.getString("search"), JsonElement::class.java)
        return json
    }
}
*/
class TestWorker(context: Context,workerParameters: WorkerParameters): CoroutineWorker(context,workerParameters){
    override suspend fun doWork(): Result {
        delay(30000)
        //Log.d("TEST_WORK","TestWorker is start...")
       // Log.d("TEST_WORK","TestWorker is end...")
        sendNotification(applicationContext,"Это что - Заголовок???","Ура смотри, все работаит уже целых $runAttemptCount раз!!!")
        return Result.retry()
    }
}
class UpdateWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters) {
    private var title = "TITLE_NOTIFICATION"
    private var message = "MESSAGE_NOTIFICATION"
    override suspend fun doWork(): Result {
      //  Log.d(TAG, "update: start")
        try {
            if(tags.contains("OneTimeUpdate")){
                // for periodic work realisation with OneTimeWorker
                delay(inputData.getInt("update_interval",5)*6000L)
            }else{
                // for PeriodicWorker
            //    Log.d("WorkerRepeatCount","$tags $runAttemptCount")
                if (runAttemptCount > 5) return Result.failure() // todo добавить внутренний push что пока не найдено, будет обновление
            }

            // api - for remote, repo - local database
            val id = inputData.getString("id")
            val repo = AppRepositoryDB(AppModule.provideAppDao(AppModule.provideDatabase(applicationContext)))

            val setId = id!!.toLong()

            val adverts = loadRemoteData()
            if (adverts.isNullOrEmpty()) return Result.retry()

            // проверить дубликаты TODO понять надо ли оно вообще
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
        title = "Найдены новые объявления!"//getTitle(kotlin.math.abs(adverts.size - inputData.getInt("advertsCount", 0)))
        message = "Подборка ${inputData.getString("name")} обновилась! Нажмите, чтобы посмотреть..."
        sendNotification(applicationContext,title,message)

      //  Log.d("UpdateWorker","Update is successful, tag - $tags")
    }
    private fun failedSearchNotify(){
     //   Log.d(TAG, "UpdateWorker: error")

        title = "Что-то мешает поиску..."//getTitle(kotlin.math.abs(adverts.size - inputData.getInt("advertsCount", 0)))
        message = "Сейчас объявления не могут быть найдены. Нажмите для получения информации..." // todo во внутренние пуши подробную инфу
        sendNotification(applicationContext,title,message)
    }
    private fun getTitle(count: Int): String{
        val lastDigit = count.toString().last().toString().toInt()
        val word = if (count in 5..20) "объявлений"
        else if (lastDigit == 0 || lastDigit >= 5) "объявлений"
        else if (lastDigit == 1) "объявление"
        else if (lastDigit in 2..4) "объявления"
        else throw IllegalArgumentException("incorrect input")

        return "Найдено $count новых $word!"
    }
    private fun MutableList<Advert>?.removeBlackAdverts(blackList: List<BlackList>) {
        //val blackList = repo.getBlackList(setId)
        if (blackList.isNotEmpty()){
            blackList.forEach{
                    blackList -> this!!.toList().forEach {
                    advert -> if (blackList.hash == advert.hash) {
                this.remove(advert)
                return@forEach
            } } } }
       // repo.deleteAdvertsBySet(setId)
    }
    private suspend fun loadRemoteData(): MutableList<Advert>?{
        val api = AppModule.provideServerApi(AppModule.provideInternetConnection(AppModule.provideHttpClient()))
        val adverts = api.get(getSearchFromInputData()).toMutableList()
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
        // todo собираем set (full идентичный) из input data
        val set = AdSet()

        val last_update = LocalDate.now()//.parse(inputData.getString("last_update"))

        set.id = inputData.getString("id")!!.toLong()
        //set.caption = inputData.getString("caption")!!
        set.last_update = last_update
        set.update_interval = inputData.getInt("update_interval",15)
        set.name = inputData.getString("name")
        return set
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        //return super.getForegroundInfo()
        return ForegroundInfo(
            0, createNotification()
        )
    }

    private fun createNotification(): Notification{
        return getNotification(applicationContext,title,message)

    }
}
