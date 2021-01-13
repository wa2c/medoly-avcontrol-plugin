package com.wa2c.android.medoly.plugin.action.avcontrol

import android.app.Application
import com.wa2c.android.medoly.plugin.action.avcontrol.service.PluginService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Aoo
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Injection
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.INFO)
            androidContext(this@App)
            modules(Module.modules)
        }

        // Create channel
        PluginService.createChannel(this)
    }

}
