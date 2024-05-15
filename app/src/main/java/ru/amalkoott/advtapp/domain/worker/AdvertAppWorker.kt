package ru.amalkoott.advtapp.domain.worker

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
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
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.Advert
import ru.amalkoott.advtapp.domain.AppRepository
import ru.amalkoott.advtapp.domain.notification.getNotification
import ru.amalkoott.advtapp.domain.notification.sendNotification
import ru.amalkoott.advtapp.ui.advert.view.AppViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "*** UpdateWorker ***"

class TestUpdateWorker (context: Context, workerParameters: WorkerParameters) : Worker(context,workerParameters){

    @OptIn(DelicateCoroutinesApi::class)
    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        try {
            //TimeUnit.SECONDS.sleep(20)
            //val set = inputData.getString("caption")
            val map = inputData.keyValueMap
            GlobalScope.launch {
                //val test = appRepo.loadAllSets()
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
    //private val appApi: ServerRequestsRepository = MainActivity::class.java.getDeclaredField("api") as ServerRequestsRepository
    //private val appRepo: AppRepository = MainActivity::class.java.getDeclaredField("notesRepo") as AppRepository
/*
    var httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer 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"
                )
                .build()
            chain.proceed(request)
        }
        .build()

    var retrofit = Retrofit.Builder()
        //.baseUrl("http://192.168.56.1:8080")
        .baseUrl("http://10.0.2.2:8080")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var api = ServerRequestsRepository(retrofit.create(ServerAPI::class.java))
    private val database: AppDatabase by lazy{
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    private val notesRepo by lazy { AppRepositoryDB(database.notesDao()) }

    private suspend fun sendSearching(search: RealEstateSearchParameters?): List<Advert>?{

        if (search == null) return null
        val response = api.get(search)
        //val response = appApi.get(SearchParameters("Снять"))
        return if (response.isNotEmpty()){
            // response to set и потом сохранять
            //val set = AdSet(name = "",adverts = response, update_interval = 10, caption = null, category = null, last_update = null)
            response
        }else{
            null
        }
        //return response
    }
    private suspend fun update(adSet: AdSet){
        //val appRepo: AppRepository
        val parameters = RealEstateSearchParameters()
        // todo собираем parameters через set.caption (можно в json конвертить строку)
        val gson = Gson()
        /*
          val temp = gson.toJsonTree(adSet.caption)
      val json = temp.asJsonObject
      */
        val jelem: JsonElement = gson.fromJson<JsonElement>(adSet.caption, JsonElement::class.java)
        val json = jelem.asJsonObject

        try {
            parameters.category = json["category"]?.toString()
            parameters.city = json["city"]?.toString()
            parameters.dealType = json["dealType"]?.toString()
            parameters.livingType = json["livingType"]?.toString()
            parameters.priceType = json["priceType"]?.toString()

            val adverts = sendSearching(parameters)!!.toMutableList()
            if (adverts.isEmpty()) return

            val blackList = notesRepo.getBlackList(adSet.id!!)
            if (blackList.isNotEmpty()){
                blackList.forEach{
                        blackList -> adverts.toList().forEach {
                        advert -> if (blackList.hash == advert.hash) {
                    // удаляем из adverts
                    adverts.remove(advert)
                    return@forEach
                } } } }
            // удаляем все объявления - оптимальный способ, т.к. объявления на локалке могут устареть
            notesRepo.deleteAdvertsBySet(adSet.id!!)

            // вставляем новые объявления в бд
            adverts.forEach {
                it.adSetId = adSet.id
                notesRepo.addAdv(it)
            }

        }catch (e:Exception){
            Log.w("RemoteUpdateSetError",e.message!!)
        }
    }
*/
}
class UpdateWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        try {
            TimeUnit.SECONDS.sleep(20)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Log.d(TAG, "doWork: end")

        return Result.success()
        /*
        try {
            //Ваш код
        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }
        return Result.success()

         */
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

/*
    private suspend fun loadNewCollection(): Collection<Data> {
        // TODO ... Загрузка данных из сети или другой источник
    }
*/
    private suspend fun saveCollection(collection: Collection<Data>) {
        // ... Сохранение данных в локальную память
    }
}
/*
class AdvertAppWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        makeStatusNotification(
            applicationContext.resources.getString(R.string.blurring_image),
            applicationContext
        )

        return try {
            val picture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.android_cupcake
            )

            val output = blurBitmap(picture, 1)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(applicationContext, output)

            makeStatusNotification(
                "Output is $outputUri",
                applicationContext
            )

            Result.success()
        } catch (throwable: Throwable) {
            Log.e(
                TAG,
                applicationContext.resources.getString(R.string.error_applying_blur),
                throwable
            )
            Result.failure()
        }

    }

}
*/