package com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api

import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data.GetDeviceInfoRes
import retrofit2.http.GET

interface YamahaExtendedControlApi {

    @GET("getDeviceInfo")
    suspend fun getDeviceInfo(): GetDeviceInfoRes

}
