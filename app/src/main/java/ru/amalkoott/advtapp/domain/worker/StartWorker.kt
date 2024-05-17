package ru.amalkoott.advtapp.domain.worker

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkContinuation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import ru.amalkoott.advtapp.data.local.AppRepositoryDB
import ru.amalkoott.advtapp.data.remote.RealEstateSearchParameters
import ru.amalkoott.advtapp.domain.AdSet
import ru.amalkoott.advtapp.domain.AppRepository
import ru.amalkoott.advtapp.domain.AppUseCase
import ru.amalkoott.advtapp.domain.Constants
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("RestrictedApi")
class StartWorker @Inject constructor(
    private val context: Context,

  //  private val workManager: WorkManager,
//    private val appRepo: AppRepository
) {


    /*

    @SuppressLint("RestrictedApi")
    fun createSet(context: Context, set: AdSet, search: RealEstateSearchParameters): Operation.State{
        val gson = Gson()
        val id: String = if(set.id == null) "null" else set.id.toString()
        val data = Data.Builder()
            //.put("set",set)
            .putString("id", id)
            .putString("adverts", gson.toJsonTree(set.adverts).toString())
            .putInt("update_interval", set.update_interval!!)
            .putString("name", set.name)
            .putString("last_update", set.last_update.toString())
            .putString("search",gson.toJsonTree(search).toString())
            .build()

        val request = OneTimeWorkRequest.Builder(CreateWorker::class.java)
            .setInputData(data)
            .build()

        /*
        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(false)
            .setRequiresBatteryNotLow(false)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
    */
        //@SuppressLint("InvalidPeriodicWorkRequestInterval")
        //val request = PeriodicWorkRequestBuilder<UpdateWorker>(15, TimeUnit.SECONDS)
        //.setInitialDelay(15,TimeUnit.SECONDS)
        // .addTag("UPDATE_WORKER_TEST")
        //  .setConstraints(constraints)
        //     .build()

        val tag = "TEST_INSERT"
        val result = workManager.enqueueUniqueWork(tag,
            ExistingWorkPolicy.REPLACE, request).result.get()
        when(result){
            Operation.SUCCESS -> Log.d(tag,"$tag is SUCCESSFUL")
            else -> Log.d(tag,"$tag is strange")
        }
        return result
    }
*/
    //private val workManager = WorkManager.getInstance(context)
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
            Log.d("${Constants.SET_TAG}${set.id}","${set.name} is running successful for periodic work with one time realisation in ($updateInterval) minutes...")
        }
        else{
            WorkManager.getInstance(context).enqueueUniqueWork("${Constants.WORK_TAG}${set.id}", ExistingWorkPolicy.REPLACE, request as OneTimeWorkRequest)
            Log.d("${Constants.SET_TAG}${set.id}","${set.name} is running successful for periodic work in ($updateInterval) minutes...")
        }

    }
    private fun getData(set: AdSet, search: RealEstateSearchParameters): Data{
        // TODO собирать inputData из set и search
        val gson = Gson()
        val id: String = if(set.id == null) "null" else set.id.toString()
        return Data.Builder()
            //.put("set",set)
            .putString("id", id)
            .putString("name",set.name)
            .putInt("advertsCount", set.adverts!!.size) // не передаем объявления, т.к. их все равно обновят
            .putInt("update_interval", set.update_interval!!)
            //.putString("caption", set.caption)
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

