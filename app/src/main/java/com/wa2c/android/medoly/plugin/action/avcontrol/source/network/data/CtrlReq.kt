package com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "YAMAHA_AV")
data class CtrlReq(
    @Attribute(name = "cmd")
    val cmd: String,
    @Element(name = "Main_Zone")
    val mainZone: MainZoneReq
)

@Xml
data class MainZoneReq(
    @Element(name = "Input")
    val input: InputReq? = null,
    @PropertyElement(name = "Config")
    val config: String? = null,
    @PropertyElement(name = "Basic_Status")
    val basicStatus: String? = null
)

@Xml
data class InputReq(
    /**
     * Select input ( HDMIx, BLUETOOTH, ... )
     */
    @PropertyElement(name = "Input_Sel")
    val inputSel: String? = null
)

