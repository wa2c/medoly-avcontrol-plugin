package com.wa2c.android.medoly.plugin.action.avcontrol

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.ApiInterceptor
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api.YamahaExtendedControlApi
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api.YamahaRemoteControlApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** HTTP Client */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor())
            .build()
    }

    /**
     * Retrofit for Yamaha AV receiver API.
     */
    @Singleton
    @Provides
    fun provideYamahaRemoteControlApi(
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
        return retrofit.create(YamahaRemoteControlApi::class.java)
    }

    /**
     * Retrofit for Yamaha AV receiver extended API.
     */
    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideYamahaExtendedControlApi(
        okHttpClient: OkHttpClient,
        preferences: AppPreferences
    ): YamahaExtendedControlApi {
        val avAddress = preferences.hostAddress ?: "localhost"
        val baseUrl = "http://$avAddress/YamahaExtendedControl/v1/system/"
        val json = Json { encodeDefaults = false; ignoreUnknownKeys = true }
        val converter = json.asConverterFactory("application/json".toMediaType())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build()
        return retrofit.create(YamahaExtendedControlApi::class.java)
    }

}