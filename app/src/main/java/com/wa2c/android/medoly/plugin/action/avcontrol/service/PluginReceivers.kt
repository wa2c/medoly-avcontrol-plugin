package com.wa2c.android.medoly.plugin.action.avcontrol.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.*
import com.wa2c.android.medoly.library.MediaPluginIntent
import com.wa2c.android.medoly.library.PluginBroadcastResult
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logD
import com.wa2c.android.medoly.plugin.action.avcontrol.common.toWorkParams

/**
 * Execute receiver.
 */
abstract class AbstractPluginReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        logD("onReceive: %s", this.javaClass.simpleName)
        val pluginIntent = MediaPluginIntent(intent)
        val result = runPlugin(context, pluginIntent)
        setResult(result.resultCode, null, null)
    }

    abstract fun runPlugin(context: Context, pluginIntent: MediaPluginIntent): PluginBroadcastResult

    /**
     * Launch worker.
     */
    protected inline fun <reified T : Worker> launchWorker(context: Context, params: Data) {
        val workManager = WorkManager.getInstance(context.applicationContext)
        val request = OneTimeWorkRequestBuilder<T>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(params)
            .build()
        workManager.enqueue(request)
    }
}

// Execution

/**
 * Execute set bluetooth.
 */
class ExecuteSetBluetoothReceiver : AbstractPluginReceiver() {
    override fun runPlugin(context: Context, pluginIntent: MediaPluginIntent): PluginBroadcastResult {
        launchWorker<PluginPostSetBluetoothWorker>(context, pluginIntent.toWorkParams())
        return PluginBroadcastResult.COMPLETE
    }
}

/**
 * Execute reset bluetooth.
 */
class ExecuteResetBluetoothReceiver : AbstractPluginReceiver() {
    override fun runPlugin(context: Context, pluginIntent: MediaPluginIntent): PluginBroadcastResult {
        launchWorker<PluginPostResetBluetoothWorker>(context, pluginIntent.toWorkParams())
        return PluginBroadcastResult.COMPLETE
    }
}
