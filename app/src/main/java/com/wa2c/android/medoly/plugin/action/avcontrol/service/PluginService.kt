package com.wa2c.android.medoly.plugin.action.avcontrol.service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.wa2c.android.medoly.library.MediaPluginIntent
import com.wa2c.android.medoly.library.PluginBroadcastResult
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logD
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logE
import com.wa2c.android.medoly.plugin.action.avcontrol.common.showToast
import com.wa2c.android.medoly.plugin.action.avcontrol.repository.YamahaAvRepository
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.value.CtrlInput
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension
import java.io.InvalidObjectException

class PluginService : IntentService("PluginService") {

    /** Context.  */
    private val context: Context by lazy { applicationContext }
    /** Preferences.  */
    private val prefs: AppPreferences by lazy { AppPreferences(context) }
    /** Notification manager. */
    private val notificationManager : NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /** AV Repository */
    private val avRepository: YamahaAvRepository by inject()

    @KoinApiExtension
    override fun onHandleIntent(intent: Intent?) {
        logD("onHandleIntent")
        val result = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
                    setContentTitle(getString(R.string.app_name))
                    setSmallIcon(R.drawable.ic_notification)
                }.build()
                startForeground(NOTIFICATION_ID, notification)
            }

            if (intent == null)
                throw InvalidObjectException("Null intent")

            when (MediaPluginIntent(intent).getStringExtra(RECEIVED_CLASS_NAME) ?: "") {
                ExecuteSetBluetoothReceiver::class.java.name -> {
                    selectBluetooth()
                    true
                }
                ExecuteResetReceiver::class.java.name -> {
                    resetInput()
                    true
                }
                else -> {
                    PluginBroadcastResult.CANCEL
                    true
                }
            }
        } catch (e: Exception) {
            logE(e)
            false
        }

        // show message
        if (result) {
            if (prefs.showSuccessMessage) showToast(R.string.action_succeeded_message)
        } else {
            if (prefs.showFailureMessage) showToast(R.string.action_failed_message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logD("onDestroy")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancel(NOTIFICATION_ID)
            stopForeground(true)
        }
    }

    /**
     * Select bluetooth
     */
    private fun selectBluetooth(): PluginBroadcastResult {
        return runBlocking {
            try {
                avRepository.selectInput(CtrlInput.BLUETOOTH)
                PluginBroadcastResult.COMPLETE
            } catch (e: Exception) {
                PluginBroadcastResult.CANCEL
            }
        }
    }

    /**
     * Reset input
     */
    private fun resetInput(): PluginBroadcastResult {
        return runBlocking {
            try {
                avRepository.resetInput()
                PluginBroadcastResult.COMPLETE
            } catch (e: Exception) {
                PluginBroadcastResult.CANCEL
            }
        }
    }

    companion object {
        /** Notification ID */
        private const val NOTIFICATION_ID = 1
        /** Notification Channel ID */
        private const val NOTIFICATION_CHANNEL_ID = "Notification"
        /** Received receiver class name.  */
        const val RECEIVED_CLASS_NAME = "RECEIVED_CLASS_NAME"

        /**
         * Create notification
         */
        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                return

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_MIN)
                notificationManager.createNotificationChannel(channel)
            }
        }

    }
}
