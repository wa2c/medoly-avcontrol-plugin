package com.wa2c.android.medoly.plugin.action.avcontrol

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.wa2c.android.medoly.plugin.action.avcontrol.service.PluginService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Aoo
 */
@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Create channel
        PluginService.createChannel(this)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}
