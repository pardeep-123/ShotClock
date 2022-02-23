package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class EditProfileRequestModel(
    @SerializedName("astrologicalSign")
    var astrologicalSign: String,
    @SerializedName("bio")
    var bio: String,
    @SerializedName("countryCode")
    var countryCode: String, // 91
    @SerializedName("dob")
    var dob: String,
    @SerializedName("gender")
    var gender:Int,
    @SerializedName("drinking")
    var drinking: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("height")
    var height: String,
    @SerializedName("images")
    var images: ArrayList<EditProfileImage>,
    @SerializedName("interest")
    var interest: String,
    @SerializedName("location")
    var location: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("pets")
    var pets: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("profile_image")
    var profileImage: String,
    @SerializedName("qualification")
    var qualification: String,
    @SerializedName("sexualOrientation")
    var sexualOrientation: String,
    @SerializedName("smoking")
    var smoking: String
) {
    data class EditProfileImage(
        @SerializedName("image")
        var image: String
    )
}