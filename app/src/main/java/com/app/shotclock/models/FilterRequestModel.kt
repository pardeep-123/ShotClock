package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class FilterRequestModel(
    @SerializedName("astrologicalSign")
    var astrologicalSign: String, // string
    @SerializedName("drinking")
    var drinking: String, // string
    @SerializedName("gender")
    var gender: Int, // 0
    @SerializedName("height")
    var height: String, // string
    @SerializedName("latitude")
    var latitude: String, // string
    @SerializedName("longitude")
    var longitude: String, // string
    @SerializedName("lowerAge")
    var lowerAge: String, // string
    @SerializedName("lowerDistance")
    var lowerDistance: Int, // 0
    @SerializedName("pets")
    var pets: String, // string
    @SerializedName("qualification")
    var qualification: String, // string
    @SerializedName("sexualOrientation")
    var sexualOrientation: String, // string
    @SerializedName("smoking")
    var smoking: String, // string
    @SerializedName("upperAge")
    var upperAge: String, // string
    @SerializedName("upperDistance")
    var upperDistance: Int // 0
)