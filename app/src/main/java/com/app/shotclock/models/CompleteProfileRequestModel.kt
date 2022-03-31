package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class CompleteProfileRequestModel(
    @SerializedName("astrologicalSign")
    var astrologicalSign: String, // string
    @SerializedName("bio")
    var bio: String, // string
    @SerializedName("dob")
    var dob: String, // string
    @SerializedName("drinking")
    var drinking: String, // string
    @SerializedName("gender")
    var gender: Int, // 1
    @SerializedName("height")
    var height: String, // string
    @SerializedName("images")
    var images: ArrayList<Image>,
    @SerializedName("interest")
    var interest: String, // string
    @SerializedName("latitude")
    var latitude: String, // string
    @SerializedName("location")
    var location: String, // string
    @SerializedName("longitude")
    var longitude: String, // string
    @SerializedName("pets")
    var pets: String, // string
    @SerializedName("qualification")
    var qualification: String, // string
    @SerializedName("sexualOrientation")
    var sexualOrientation: String, // string
    @SerializedName("smoking")
    var smoking: String // string
) {
    data class Image(
        @SerializedName("image")
        var image: String // string
    )
}