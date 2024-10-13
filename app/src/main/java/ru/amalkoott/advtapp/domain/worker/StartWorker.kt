package ru.amalkoott.advtapp.domain.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.gson.Gson
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.domain.entities.AdSet
import ru.amalkoott.advtapp.domain.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("RestrictedApi")
class StartWorker @Inject constructor(
    private val context: Context,
) {
    fun updateSet(context: Context, set: AdSet, search: RealEstateSearchParameters, isNewSet: Boolean){
        lateinit var request: WorkRequest
        val updateInterval: Long = set.update_interval!!.toLong()
        val data = getData(set,search)

        val isShortInterval = set.update_interval!! < 15

        when(Pair(isNewSet,isShortInterval)){
            Pair(false,false) -> {
                // обновление старой подборки (нормальный интервал)
                request = PeriodicWorkRequestBuilder<UpdateWorker>(updateInterval, TimeUnit.MINUTES)
                    .addTag(Constants.PERIODIC_TAG)
                    .addTag("${Constants.SET_TAG}${set.id}")
                    .setBackoffCriteria(BackoffPolicy.LINEAR,10,TimeUnit.SECONDS)
                    .setInputData(data)
                    .build()
            }
            Pair(false,true) -> {
                // обновление старой подборки (короткий интервал)
                request = OneTimeWorkRequest.Builder(UpdateWorker::class.java)
                    .addTag(Constants.ONE_TIME_TAG)
                    .addTag("${Constants.SET_TAG}${set.id}")
                    .setBackoffCriteria(BackoffPolicy.LINEAR,updateInterval,TimeUnit.MINUTES)
                    .setInputData(data)
                    .build()
            }
            Pair(true,false) -> {
                // обновления для новой подборки (нормальный интервал)
                request = PeriodicWorkRequestBuilder<UpdateWorker>(updateInterval, TimeUnit.MINUTES)
                    .setInitialDelay(updateInterval,TimeUnit.MINUTES)
                    .addTag(Constants.PERIODIC_TAG)
                    .addTag("${Constants.SET_TAG}${set.id}")
                    .setBackoffCriteria(BackoffPolicy.LINEAR,10,TimeUnit.SECONDS)
                    .setInputData(data)
                    .build()
            }
            Pair(true,true) -> {
                // обновления для новой подборки (короткий интервал)
                request = OneTimeWorkRequest.Builder(TestWorker::class.java)
                    .setInitialDelay(updateInterval,TimeUnit.MINUTES)
                    .addTag(Constants.ONE_TIME_TAG)
                    .addTag("${Constants.SET_TAG}${set.id}")
                    .setBackoffCriteria(BackoffPolicy.LINEAR,updateInterval,TimeUnit.MINUTES)
                    .setInputData(data)
                    .build()
            }
        }
        if(!isShortInterval){
            WorkManager.getInstance(context).enqueueUniquePeriodicWork("${Constants.WORK_TAG}${set.id}", ExistingPeriodicWorkPolicy.KEEP,request as PeriodicWorkRequest)
        }
        else{
            WorkManager.getInstance(context).enqueueUniqueWork("${Constants.WORK_TAG}${set.id}", ExistingWorkPolicy.REPLACE, request as OneTimeWorkRequest)
        }
    }

    private fun getData(set: AdSet, search: RealEstateSearchParameters): Data{
        val gson = Gson()
        val id: String = if(set.id == null) "null" else set.id.toString()
        return Data.Builder()
            .putString("id", id)
            .putString("name",set.name)
            .putInt("advertsCount", set.adverts!!.size) // не передаем объявления, т.к. их все равно обновят
            .putInt("update_interval", set.update_interval!!)
            .putString("last_update", set.last_update.toString())
            .putString("search",gson.toJsonTree(search).toString())
            .build()
    }

    fun removeUpdatingSet(set: AdSet){
        WorkManager.getInstance(context).cancelUniqueWork("${Constants.WORK_TAG}${set.id}")
    }

    fun clearAllWork(){
        WorkManager.getInstance(context).cancelAllWork()
        WorkManager.getInstance(context).pruneWork()
    }
}

