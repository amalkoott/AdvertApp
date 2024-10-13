package ru.amalkoott.advtapp.domain.worker

import android.app.Notification
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import ru.amalkoott.advtapp.data.local.AppRepositoryDB
import ru.amalkoott.advtapp.di.AppModule
import ru.amalkoott.advtapp.domain.entities.AdSet
import ru.amalkoott.advtapp.domain.entities.Advert
import ru.amalkoott.advtapp.domain.entities.BlackList
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
    private fun getSetFromInputData(): AdSet {
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
