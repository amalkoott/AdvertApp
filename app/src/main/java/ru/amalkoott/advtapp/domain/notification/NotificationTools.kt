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
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    return builder.build()
}
fun sendGeoNotification(applicationContext: Context) {
    val title = "Вы рядом с объявлением!"
    val message = "Объявление \"Квартира-студия, 35 кв.м. 7/10 этаж\" находится совсем рядом с вами! Возможно, стоит связаться с хозяином?" // todo во внутренние пуши подробную инфу

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
        .setSmallIcon(R.drawable.ic_geo_search)
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

}
fun sendBadNotification(applicationContext: Context) {
    val title = "Что-то мешает поиску..."
    val message = "Сейчас объявления не могут быть найдены. Нажмите для получения подробной информации..." // todo во внутренние пуши подробную инфу

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
        .setSmallIcon(R.drawable.ic_bad_search)
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
        .setSmallIcon(R.drawable.ic_successful_search)
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

}
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