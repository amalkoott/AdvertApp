package ru.amalkoott.advtapp.domain.worker

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
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
import ru.amalkoott.advtapp.domain.AdSet
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("RestrictedApi")
class StartWorker @Inject constructor(private val workManager: WorkManager) {

    @SuppressLint("RestrictedApi")
    fun startOneTimeWorker(context: Context, tag: String, set: AdSet){
        val gson = Gson()
        val data = Data.Builder()
            //.put("set",set)
            .putLong("id", set.id!!)
            .putString("adverts", gson.toJsonTree(set.adverts).toString())
            .putInt("update_interval", set.update_interval!!)
            .putString("caption", set.caption!!)
            .putString("last_update", set.last_update.toString())
            .build()

        val request = OneTimeWorkRequest.Builder(TestUpdateWorker::class.java)
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

        val workManager = WorkManager.getInstance(context).enqueueUniqueWork(tag,
            ExistingWorkPolicy.REPLACE, request)
        Log.d(tag,"$tag is running for one time work...")
    }
    fun startPeriodicWorker(context: Context, repeatInterval: Long, tag: String){

        /*
    val constraints = Constraints.Builder()
        .setRequiresStorageNotLow(false)
        .setRequiresBatteryNotLow(false)
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()
*/
        //@SuppressLint("InvalidPeriodicWorkRequestInterval")
        val request = PeriodicWorkRequestBuilder<UpdateWorker>(repeatInterval, TimeUnit.MINUTES)
            //.setInitialDelay(15,TimeUnit.SECONDS)
            // .addTag("UPDATE_WORKER_TEST")
            //  .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(context).enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.KEEP,request )
        Log.d(tag,"$tag is running successful for periodic work ($repeatInterval)...")
    }

}

