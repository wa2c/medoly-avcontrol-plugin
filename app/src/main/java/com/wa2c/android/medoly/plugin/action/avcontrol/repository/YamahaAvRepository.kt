package com.wa2c.android.medoly.plugin.action.avcontrol.repository

import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api.YamahaExtendedControlApi
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.api.YamahaRemoteControlApi
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data.CtrlReq
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data.CtrlRes
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data.InputReq
import com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data.MainZoneReq
import com.wa2c.android.medoly.plugin.action.avcontrol.value.CtrlCmd
import com.wa2c.android.medoly.plugin.action.avcontrol.value.CtrlInput

class YamahaAvRepository(
    private val remoteApi: YamahaRemoteControlApi,
    private val extendedApi: YamahaExtendedControlApi,
    private val preferences: AppPreferences
) {

//    /**
//     * Discover devices
//     */
//    suspend fun discoverDevices(): Map<String, String> {
//        return suspendCoroutine { continuation ->
//            val networkStorageDevice = SsdpRequest.builder()
//                .serviceType("urn:schemas-upnp-org:device:MediaRenderer:1")
//                .build()
//            SsdpClient.create().discoverServices(networkStorageDevice, object : DiscoveryListener {
//                override fun onFailed(e: Exception) {
//                    logE(e)
//                    continuation.resume(emptyMap())
//                }
//
//                override fun onServiceDiscovered(service: SsdpService) {
//                    println("Found service: $service")
//                    val header = service.originalResponse.headers.get("X-MODELNAME")
//                    logD(service)
//                    continuation.resume(emptyMap())
//                }
//
//                override fun onServiceAnnouncement(announcement: SsdpServiceAnnouncement) {
//                    println("Service announced something: $announcement")
//                    logD(announcement)
//                }
//            })
//            return@suspendCoroutine
//        }
//    }

    /**
     * Select input
     */
    suspend fun selectInput(input: CtrlInput) {
        val status = getBasicStatus() // Get status
        val currentInput = status.mainZone.basicStatus?.input?.inputSel
        if (currentInput == input.apiValue) return
        val req = CtrlReq(
            CtrlCmd.PUT.apiValue,
            MainZoneReq(input = InputReq(input.apiValue))
        )
        remoteApi.postCtrl(req)
        preferences.previousAddress =
            status.mainZone.basicStatus?.input?.inputSel // Save previous input
    }

    /**
     * Reset input to previous input
     */
    suspend fun resetInput() {
        val input = preferences.previousAddress
        val req = CtrlReq(
            CtrlCmd.PUT.apiValue,
            MainZoneReq(input = InputReq(input))
        )
        remoteApi.postCtrl(req)
        preferences.previousAddress = null
    }

    /**
     * Get basic status
     */
    private suspend fun getBasicStatus(): CtrlRes {
        val req = CtrlReq(
            CtrlCmd.GET.apiValue,
            MainZoneReq(basicStatus = GET_PARAM)
        )
        return remoteApi.postCtrl(req)
    }

    companion object {
        private const val GET_PARAM = "GetParam"
    }
}
