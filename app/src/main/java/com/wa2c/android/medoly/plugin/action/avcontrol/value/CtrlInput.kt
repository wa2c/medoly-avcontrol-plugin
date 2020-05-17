package com.wa2c.android.medoly.plugin.action.avcontrol.value

/**
 * Input source on Yamaha AV Receiver
 */
enum class CtrlInput(
    val apiValue: String
) {
    HDMI1("HDMI1"),
    HDMI2("HDMI2"),
    HDMI3("HDMI3"),
    HDMI4("HDMI4"),
    HDMI5("HDMI5"),
    HDMI6("HDMI6"),
    AV1("AV1"),
    AV2("AV2"),
    AV3("AV3"),
    AUDIO1("AUDIO1"),
    AUDIO2("AUDIO2"),
    AUDIO3("AUDIO3"),
    AUX("AUX"),
    SPOTIFY("Spotify"),
    AIR_PLAY("AirPlay"),
    MUSIC_CAST("MusicCast Link"),
    SERVER("SERVER"),
    NET_RADIO("NET RADIO"),
    BLUETOOTH("Bluetooth"),
    USB("USB"),
    IPOD_USB("iPod (USB)"),
    TUNER("TUNER"),
    ;

    companion object {
        fun fromApiValue(value: String?): CtrlInput? {
            return values().firstOrNull { it.apiValue == value }
        }
    }
}
