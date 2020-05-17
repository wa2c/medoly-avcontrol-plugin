package com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api

import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data.CtrlReq
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data.CtrlRes
import retrofit2.http.Body
import retrofit2.http.POST

interface YamahaRemoteControlApi {

    @POST("ctrl")
    suspend fun postCtrl(@Body ctrlReq: CtrlReq): CtrlRes

}
