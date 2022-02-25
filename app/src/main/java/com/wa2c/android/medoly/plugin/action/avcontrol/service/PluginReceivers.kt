package com.wa2c.android.medoly.plugin.action.avcontrol.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.wa2c.android.medoly.library.MediaPluginIntent
import com.wa2c.android.medoly.library.PluginBroadcastResult
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logD

/**
 * Execute receiver.
 */
abstract class AbstractPluginReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        logD("onReceive: %s", this.javaClass.simpleName)
        val pluginIntent = MediaPluginIntent(intent).also {
            it.setClass(context, PluginService::class.java)
            it.putExtra(PluginService.RECEIVED_CLASS_NAME, this.javaClass.name)
        }
        ContextCompat.startForegroundService(context, pluginIntent)
        setResult(PluginBroadcastResult.COMPLETE.resultCode, null, null)
    }

}

// Execution

class ExecuteSetBluetoothReceiver : AbstractPluginReceiver()

class ExecuteResetReceiver : AbstractPluginReceiver()
