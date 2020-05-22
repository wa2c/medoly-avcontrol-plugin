package com.wa2c.android.medoly.plugin.action.avcontrol

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import com.wa2c.android.medoly.plugin.action.avcontrol.repository.YamahaAvRepository
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.ApiInterceptor
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api.YamahaExtendedControlApi
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api.YamahaRemoteControlApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

// Factory method

/**
 * OkHTTP
 */
private fun createHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(ApiInterceptor())
        .build()
}

/**
 * Retrofit for Yamaha AV receiver API.
 */
private fun createYamahaRemoteControlApi(
    okHttpClient: OkHttpClient,
    preferences: AppPreferences
): YamahaRemoteControlApi {
    val avAddress = preferences.hostAddress ?: "localhost"
    val baseUrl = "http://$avAddress/YamahaRemoteControl/"
    val config = TikXml.Builder().exceptionOnUnreadXml(false).build()
    val converter = TikXmlConverterFactory.create(config)
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converter)
        .build()
    return retrofit.create()
}

/**
 * Retrofit for Yamaha AV receiver extended API.
 */
private fun createYamahaExtendedControlApi(
    okHttpClient: OkHttpClient,
    preferences: AppPreferences
): YamahaExtendedControlApi {
    val avAddress = preferences.hostAddress ?: "localhost"
    val baseUrl = "http://$avAddress/YamahaExtendedControl/v1/system/"
    val config = JsonConfiguration(encodeDefaults = false, ignoreUnknownKeys = true)
    val converter = Json(config).asConverterFactory("application/json".toMediaType())
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converter)
        .build()
    return retrofit.create()
}

// Modules

private val baseModule: Module = module {
    single { AppPreferences(androidContext()) }
    single { createHttpClient() }
}

private val apiModule: Module = module {
    factory { createYamahaRemoteControlApi(get(), get()) }
    factory { createYamahaExtendedControlApi(get(), get()) }
}

private val repositoryModule: Module = module {
    single { YamahaAvRepository(get(), get(), get()) }
}

object Module {
    val modules = listOf(
        baseModule,
        apiModule,
        repositoryModule
    )
}
