package ru.amalkoott.advtapp.domain.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.amalkoott.advtapp.MainActivity
import ru.amalkoott.advtapp.R
import kotlin.properties.Delegates

fun getNotification(applicationContext: Context, title: String, message: String): Notification{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel("CHANNEL_ID", "My Notification Channel", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val mainActivityIntent = Intent(
        applicationContext,
        MainActivity::class.java)

    var pendingIntentFlag by Delegates.notNull<Int>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        pendingIntentFlag = PendingIntent.FLAG_IMMUTABLE
    } else {
        pendingIntentFlag = PendingIntent.FLAG_UPDATE_CURRENT
    }

    val mainActivityPendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        mainActivityIntent,
        pendingIntentFlag)

    val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    return builder.build()
}
fun sendNotification(applicationContext: Context, title: String, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel("CHANNEL_ID", "My Notification Channel", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val mainActivityIntent = Intent(
        applicationContext,
        MainActivity::class.java)

    var pendingIntentFlag by Delegates.notNull<Int>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        pendingIntentFlag = PendingIntent.FLAG_IMMUTABLE
    } else {
        pendingIntentFlag = PendingIntent.FLAG_UPDATE_CURRENT
    }

    val mainActivityPendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        mainActivityIntent,
        pendingIntentFlag)

    val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

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
        notify(1, builder.build()) // посылаем уведомление
    }



    /*
        // Create an explicit intent for an Activity in your app.
        val intent = Intent(applicationContext, Advert::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        createNotificationChannel(applicationContext)
        val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
            .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(1, builder.build())
        }
        */
}


/*
// TODO TEST
@SuppressLint("MissingPermission")
//@Composable
fun sendNotification(applicationContext: Context, title: String, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel("CHANNEL_ID", "My Notification Channel", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

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
        notify(1, builder.build()) // посылаем уведомление
    }



/*
    // Create an explicit intent for an Activity in your app.
    val intent = Intent(applicationContext, Advert::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    createNotificationChannel(applicationContext)
    val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
        .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that fires when the user taps the notification.
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(applicationContext)) {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            //                                        grantResults: IntArray)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return@with
        }
        // notificationId is a unique int for each notification that you must define.
        notify(1, builder.build())
    }
    */
}
*/
private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //val name = getString(R.string.channel_name)
        //val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID"," name", importance).apply {
            description = "descriptionText"
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}