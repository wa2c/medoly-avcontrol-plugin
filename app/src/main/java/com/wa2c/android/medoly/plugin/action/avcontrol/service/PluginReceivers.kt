package com.wa2c.android.medoly.plugin.action.avcontrol.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wa2c.android.medoly.library.PluginBroadcastResult
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logD
import com.wa2c.android.medoly.plugin.action.avcontrol.common.showToast
import com.wa2c.android.medoly.plugin.action.avcontrol.repository.YamahaAvRepository
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.value.CtrlInput
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Execute receiver.
 */
abstract class AbstractPluginReceiver : BroadcastReceiver(), KoinComponent {

    private val preferences: AppPreferences by inject()
    private val avRepository: YamahaAvRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        logD("onReceive: %s", this.javaClass.simpleName)
        val result = receive()
        if (result == PluginBroadcastResult.COMPLETE) {
            if (preferences.showSuccessMessage) context.showToast(R.string.action_succeeded_message)
        } else {
            if (preferences.showFailureMessage) context.showToast(R.string.action_failed_message)
        }
        setResult(result.resultCode, null, null)
    }

    /**
     * Receive data.
     */
    private fun receive(): PluginBroadcastResult {
        return when (this) {
            is ExecuteSetBluetoothReceiver -> {
                selectBluetooth()
            }
            is ExecuteResetReceiver -> {
                resetInput()
            }
            else -> {
                PluginBroadcastResult.CANCEL
            }
        }
    }

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
}

// Execution

class ExecuteSetBluetoothReceiver : AbstractPluginReceiver()

class ExecuteResetReceiver : AbstractPluginReceiver()
