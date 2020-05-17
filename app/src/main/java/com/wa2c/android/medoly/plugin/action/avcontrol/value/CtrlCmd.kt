package com.wa2c.android.medoly.plugin.action.avcontrol.value

/**
 * Control command on Yamaha AV receiver
 */
enum class CtrlCmd(
    val apiValue: String
) {
    GET("GET"),
    PUT("PUT")
}
