package com.wa2c.android.medoly.plugin.action.avcontrol.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.medoly.plugin.action.avcontrol.common.createForegroundFuture
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logE
import com.wa2c.android.medoly.plugin.action.avcontrol.common.showToast
import com.wa2c.android.medoly.plugin.action.avcontrol.repository.YamahaAvRepository
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

/**
 * Reset Bluetooth worker
 */
@HiltWorker
class PluginPostResetBluetoothWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val avRepository: YamahaAvRepository,
    private val prefs: AppPreferences,
) : Worker(context, params) {

    override fun getForegroundInfoAsync(): ListenableFuture<ForegroundInfo> {
        return createForegroundFuture(context)
    }

    override fun doWork(): Result {
        runBlocking {
            try {
                avRepository.resetInput()
                if (prefs.showSuccessMessage) context.showToast(R.string.action_succeeded_message)
            } catch (e: Exception) {
                logE(e)
                if (prefs.showFailureMessage) context.showToast(R.string.action_failed_message)
            }
        }
        return Result.success()
    }
}