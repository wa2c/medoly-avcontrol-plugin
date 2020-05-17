package com.wa2c.android.medoly.plugin.action.avcontrol.source.network.data

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "YAMAHA_AV")
data class CtrlRes(
    @Attribute(name = "rsp")
    var rsp: String,
    @Attribute(name = "RC")
    var rc: String,
    @Element(name = "Main_Zone")
    var mainZone: MainZoneRes
)

@Xml
data class MainZoneRes(
    @Element(name = "Input")
    val input: InputRes? = null,
    @Element(name = "Config")
    var config: ConfigRes? = null,
    @Element(name = "Basic_Status")
    var basicStatus: BasicStatusRes? = null
)

// Input

@Xml
data class InputRes(
    @PropertyElement(name = "Input_Sel")
    val inputSel: String? = null
)

// Config

@Xml
data class ConfigRes(
    @PropertyElement(name = "Feature_Availability")
    var featureAvailability: String,
    @Element(name = "Name")
    var name: NameRes,
    @PropertyElement(name = "Volume_Existence")
    var volumeExistence: String
)

@Xml
data class NameRes(
    @PropertyElement(name = "Zone")
    var zone: String,

    @Element(name = "Scene")
    var scene: SceneRes
)

@Xml
data class SceneRes(
    @PropertyElement(name = "Scene_1")
    var scene1: String? = null,

    @PropertyElement(name = "Scene_2")
    var scene2: String? = null,

    @PropertyElement(name = "Scene_3")
    var scene3: String? = null,

    @PropertyElement(name = "Scene_4")
    var scene4: String? = null
)

// Basic Status

@Xml
data class BasicStatusRes(
    @Element(name = "Power_Control")
    val powerControl: PowerControlStatusRes,
    @Element(name = "Volume")
    val volume: VolumeStatusRes,
    @Element(name = "Input")
    val input: InputStatusRes
//    @Element(name = "Surround")
//    val surround: SurroundRes,
//    @PropertyElement(name = "Party_Info")
//    val partyInfo: String,
//    @Element(name = "Sound_Video")
//    val soundVideo: SoundVideoRes
)

@Xml
data class PowerControlStatusRes(
    @PropertyElement(name = "Power")
    val power: String,
    @PropertyElement(name = "Sleep")
    val sleep: String
)

@Xml
data class VolumeStatusRes(
    @Element(name = "Lvl")
    val Lvl: VolumeLevelRes,
    @PropertyElement(name = "Mute")
    val mute: String,
    @Element(name = "Subwoofer_Trim")
    val subwooferTrim: VolumeLevelRes,
    @PropertyElement(name = "Scale")
    val scale: String?
)

@Xml
data class VolumeLevelRes(
    @PropertyElement(name = "Val")
    val `val`: Int,
    @PropertyElement(name = "Exp")
    val exp: Int,
    @PropertyElement(name = "Unit")
    val unit: String
)

@Xml
data class InputStatusRes(
    @PropertyElement(name = "Input_Sel")
    val inputSel: String,
    @Element(name = "Input_Sel_Item_Info")
    val inputSelItemInfo: InputSelItemInfoRes
)

@Xml
data class InputSelItemInfoRes(
    @PropertyElement(name = "Param")
    val param: String,
    @PropertyElement(name = "RW")
    val rw: String,
    @PropertyElement(name = "Title")
    val title: String,
    @Element(name = "Icon")
    val icon: InputIconRes,
    @PropertyElement(name = "Src_Name")
    val srcName: String,
    @PropertyElement(name = "Src_Number")
    val srcNumber: Int
)

@Xml
data class InputIconRes(
    @PropertyElement(name = "On")
    val on: String,
    @PropertyElement(name = "Off")
    val off: String
)
