package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class EditProfileResponse(
    @SerializedName("body")
    var body: EditProfileBody,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // Profile updated successfully
    @SerializedName("success")
    var success: Int // 1
) {
    data class  EditProfileBody(
        @SerializedName("acceptedPettype")
        var acceptedPettype: String,
        @SerializedName("accesstoOutdorr")
        var accesstoOutdorr: Int, // 0
        @SerializedName("address")
        var address: String, // mohali
        @SerializedName("addshort")
        var addshort: String,
        @SerializedName("astrologicalSign")
        var astrologicalSign: String, // berus
        @SerializedName("authKey")
        var authKey: String, // 85f8a42a2b2baf9a95e5bd1ccec240bb86aa1fe2
        @SerializedName("bio")
        var bio: String, // gym
        @SerializedName("category")
        var category: Int, // 0
        @SerializedName("city")
        var city: String,
        @SerializedName("country")
        var country: Int, // 0
        @SerializedName("countryCode")
        var countryCode: String, // 91
        @SerializedName("createdAt")
        var createdAt: String, // 2022-02-23T05:36:47.000Z
        @SerializedName("dateofbirth")
        var dateofbirth: String, // 22/07/1996
        @SerializedName("deviceToken")
        var deviceToken: String,
        @SerializedName("deviceType")
        var deviceType: Int, // 2
        @SerializedName("distanceWillingtravel")
        var distanceWillingtravel: String,
        @SerializedName("dob_timestamp")
        var dobTimestamp: String, // 1658169000
        @SerializedName("drinking")
        var drinking: String, // no
        @SerializedName("email")
        var email: String, // gour@yopmail.com
        @SerializedName("emailVerified")
        var emailVerified: Int, // 0
        @SerializedName("emergencyTransport")
        var emergencyTransport: Int, // 0
        @SerializedName("fbId")
        var fbId: Any?, // null
        @SerializedName("fbVerified")
        var fbVerified: Int, // 0
        @SerializedName("forgotPassword")
        var forgotPassword: String,
        @SerializedName("gender")
        var gender: Int, // 0
        @SerializedName("googleId")
        var googleId: Any?, // null
        @SerializedName("googleVerified")
        var googleVerified: Int, // 0
        @SerializedName("height")
        var height: String, // 5.2
        @SerializedName("id")
        var id: Int, // 320
        @SerializedName("interested")
        var interested: Int, // 0
        @SerializedName("isFeatured")
        var isFeatured: Int, // 0
        @SerializedName("isProfessional")
        var isProfessional: Int, // 0
        @SerializedName("isTermsAccept")
        var isTermsAccept: Int, // 0
        @SerializedName("language")
        var language: Int, // 0
        @SerializedName("lastname")
        var lastname: String,
        @SerializedName("lat")
        var lat: String,
        @SerializedName("levelofSupervision")
        var levelofSupervision: String,
        @SerializedName("lng")
        var lng: String,
        @SerializedName("loginPhase")
        var loginPhase: Int, // 0
        @SerializedName("loginType")
        var loginType: Int, // 1
        @SerializedName("notificationStatus")
        var notificationStatus: Int, // 0
        @SerializedName("numberofwalksPerday")
        var numberofwalksPerday: Int, // 0
        @SerializedName("otp")
        var otp: String, // 1111
        @SerializedName("otpVerify")
        var otpVerify: Int, // 0
        @SerializedName("password")
        var password: String, // 7c4a8d09ca3762af61e59520943dc26494f8941b
        @SerializedName("password_token")
        var passwordToken: Any?, // null
        @SerializedName("pets")
        var pets: String, // no
        @SerializedName("phone")
        var phone: String, // 8855662211
        @SerializedName("phoneVerified")
        var phoneVerified: Int, // 0
        @SerializedName("profileImage")
        var profileImage: String, // string
        @SerializedName("qualification")
        var qualification: String, // bachelor
        @SerializedName("rewardPoints")
        var rewardPoints: Int, // 0
        @SerializedName("serviceCategory")
        var serviceCategory: Int, // 0
        @SerializedName("sexualOrientation")
        var sexualOrientation: String, // straight
        @SerializedName("smoking")
        var smoking: String, // no
        @SerializedName("socialId")
        var socialId: String,
        @SerializedName("social_type")
        var socialType: Any?, // null
        @SerializedName("status")
        var status: Int, // 1
        @SerializedName("typeofHome")
        var typeofHome: String,
        @SerializedName("updatedAt")
        var updatedAt: String, // 2022-02-23T08:14:07.000Z
        @SerializedName("user_images")
        var userImages: ArrayList<EditProfileBodyImages>,
        @SerializedName("userType")
        var userType: Int, // 1
        @SerializedName("username")
        var username: String, // gourav
        @SerializedName("wallet")
        var wallet: Int, // 0
        @SerializedName("whyLoveThis")
        var whyLoveThis: String,
        @SerializedName("whychooseme")
        var whychooseme: String
    ) {
        data class EditProfileBodyImages(
            @SerializedName("createdAt")
            var createdAt: String, // 2022-02-23T08:14:07.000Z
            @SerializedName("id")
            var id: Int, // 1130
            @SerializedName("image_path")
            var imagePath: String, // string
            @SerializedName("status")
            var status: Boolean, // true
            @SerializedName("updatedAt")
            var updatedAt: String, // 2022-02-23T08:14:07.000Z
            @SerializedName("user_id")
            var userId: Int // 320
        )
    }
}