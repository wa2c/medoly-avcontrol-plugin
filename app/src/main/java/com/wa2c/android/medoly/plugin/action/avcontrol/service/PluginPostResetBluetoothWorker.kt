package com.wa2c.android.medoly.plugin.action.avcontrol.service

import android.content.Context
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.wa2c.android.medoly.plugin.action.avcontrol.common.createForegroundFuture
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logE
import com.wa2c.android.medoly.plugin.action.avcontrol.repository.YamahaAvRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

/**
 * Reset Bluetooth worker
 */
class PluginPostResetBluetoothWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val avRepository: YamahaAvRepository,
) : Worker(context, params) {

    override fun getForegroundInfoAsync(): ListenableFuture<ForegroundInfo> {
        return createForegroundFuture(context)
    }

    override fun doWork(): Result {
        runBlocking {
            try {
                avRepository.resetInput()
            } catch (e: Exception) {
                logE(e)
            }
        }
        return Result.success()
    }
}