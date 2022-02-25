package com.wa2c.android.medoly.plugin.action.avcontrol

import android.app.Application
import com.wa2c.android.medoly.plugin.action.avcontrol.service.PluginService
import dagger.hilt.android.HiltAndroidApp

/**
 * Aoo
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Create channel
        PluginService.createChannel(this)
    }

}
