package com.wa2c.android.medoly.plugin.action.avcontrol.common

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.impl.utils.futures.SettableFuture
import com.google.common.util.concurrent.ListenableFuture
import com.wa2c.android.medoly.library.MediaPluginIntent
import com.wa2c.android.medoly.plugin.action.avcontrol.R

private const val INTENT_SRC_PACKAGE = "INTENT_SRC_PACKAGE"
private const val INTENT_SRC_CLASS = "INTENT_SRC_CLASS"
private const val INTENT_ACTION_ID = "INTENT_ACTION_ID"
private const val INTENT_ACTION_LABEL = "INTENT_ACTION_LABEL"
private const val INTENT_ACTION_PRIORITY = "INTENT_ACTION_PRIORITY"
private const val INTENT_ACTION_IS_AUTOMATICALLY = "INTENT_ACTION_IS_AUTOMATICALLY"

/** Notification ID */
private const val NOTIFICATION_ID = 1
/** Notification Channel ID */
private const val NOTIFICATION_CHANNEL_ID = "Notification"

/**
 * Create WorkParams data from plugin intent.
 */
fun MediaPluginIntent.toWorkParams(): Data {
    return Data.Builder().apply {
        putString(INTENT_SRC_PACKAGE, srcPackage)
        putString(INTENT_SRC_CLASS, srcClass)
        putString(INTENT_ACTION_ID, actionId)
        putString(INTENT_ACTION_LABEL, actionLabel)
        putInt(INTENT_ACTION_PRIORITY, actionPriority ?: 0)
        putBoolean(INTENT_ACTION_IS_AUTOMATICALLY, isAutomatically)
        putAll(propertyData?.keys?.mapNotNull {
            (it ?: return@mapNotNull null) to (propertyData?.getFirst(it) ?: return@mapNotNull null)
        }?.toMap() ?: emptyMap())
        putAll(extraData?.keys?.mapNotNull {
            (it ?: return@mapNotNull null) to (propertyData?.getFirst(it) ?: return@mapNotNull null)
        }?.toMap() ?: emptyMap())
    }.build()
}

/**
 * Get worker future.
 */
@SuppressLint("RestrictedApi")
fun createForegroundFuture(context: Context): ListenableFuture<ForegroundInfo> {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
        setContentTitle(context.getString(R.string.app_name))
        setSmallIcon(R.drawable.ic_notification)
    }.build()

    return SettableFuture.create<ForegroundInfo>().apply {
        set(ForegroundInfo(NOTIFICATION_ID, notification))
    }
}
