package com.wa2c.android.medoly.plugin.action.avcontrol.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.wa2c.android.medoly.plugin.action.avcontrol.common.createForegroundFuture
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logE
import com.wa2c.android.medoly.plugin.action.avcontrol.repository.YamahaAvRepository
import com.wa2c.android.medoly.plugin.action.avcontrol.value.CtrlInput
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

/**
 * Set Bluetooth worker
 */
@HiltWorker
class PluginPostSetBluetoothWorker @AssistedInject constructor(
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
                avRepository.selectInput(CtrlInput.BLUETOOTH)
            } catch (e: Exception) {
                logE(e)
            }
        }
        return Result.success()
    }
}