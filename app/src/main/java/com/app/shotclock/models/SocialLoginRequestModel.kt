package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class SocialLoginRequestModel(
    @SerializedName("countryCode")
    var countryCode: String, // string
    @SerializedName("device_token")
    var deviceToken: String, // string
    @SerializedName("device_type")
    var deviceType: Int, // 0
    @SerializedName("email")
    var email: String, // string
    @SerializedName("name")
    var name: String, // string
    @SerializedName("phone")
    var phone: String, // string
    @SerializedName("profile_image")
    var profileImage: String, // string
    @SerializedName("social_id")
    var socialId: String, // string
    @SerializedName("social_type")
    var socialType: Int // 0
)