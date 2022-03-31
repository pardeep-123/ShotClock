package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class CompleteProfileResponse(
    @SerializedName("body")
    var body: CompleteProfileBody,
    @SerializedName("code")
    var code: Int, // 200c
    @SerializedName("message")
    var message: String, // Profile completed successfully
    @SerializedName("success")
    var success: Int // 1
) {
    data class CompleteProfileBody(
        @SerializedName("acceptedPettype")
        var acceptedPettype: String,
        @SerializedName("accesstoOutdorr")
        var accesstoOutdorr: Int, // 0
        @SerializedName("address")
        var address: String, // string
        @SerializedName("addshort")
        var addshort: String,
        @SerializedName("astrologicalSign")
        var astrologicalSign: String, // string
        @SerializedName("authKey")
        var authKey: String, // 69bbf4a3af88fa5d37b701927c951554400d1b2f
        @SerializedName("bio")
        var bio: String, // string
        @SerializedName("category")
        var category: Int, // 0
        @SerializedName("city")
        var city: String,
        @SerializedName("country")
        var country: Int, // 0
        @SerializedName("countryCode")
        var countryCode: String, // +91
        @SerializedName("createdAt")
        var createdAt: String, // 2022-02-22T07:51:27.000Z
        @SerializedName("dateofbirth")
        var dateofbirth: String, // string
        @SerializedName("deviceToken")
        var deviceToken: String, // 123456
        @SerializedName("deviceType")
        var deviceType: Int, // 2
        @SerializedName("distanceWillingtravel")
        var distanceWillingtravel: String,
        @SerializedName("dob_timestamp")
        var dobTimestamp: String, // 0
        @SerializedName("drinking")
        var drinking: String, // string
        @SerializedName("email")
        var email: String, // nai@yopmail.com
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
        var gender: Int, // 1
        @SerializedName("googleId")
        var googleId: Any?, // null
        @SerializedName("googleVerified")
        var googleVerified: Int, // 0
        @SerializedName("height")
        var height: String, // string
        @SerializedName("id")
        var id: Int, // 316
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
        var lat: String, // string
        @SerializedName("levelofSupervision")
        var levelofSupervision: String,
        @SerializedName("lng")
        var lng: String, // string
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
        var pets: String, // string
        @SerializedName("phone")
        var phone: String, // 875698746
        @SerializedName("phoneVerified")
        var phoneVerified: Int, // 0
        @SerializedName("profileImage")
        var profileImage: String, // /uploads/1645516287128-file.jpg
        @SerializedName("qualification")
        var qualification: String, // string
        @SerializedName("rewardPoints")
        var rewardPoints: Int, // 0
        @SerializedName("serviceCategory")
        var serviceCategory: Int, // 0
        @SerializedName("sexualOrientation")
        var sexualOrientation: String, // string
        @SerializedName("smoking")
        var smoking: String, // string
        @SerializedName("socialId")
        var socialId: String,
        @SerializedName("social_type")
        var socialType: Any?, // null
        @SerializedName("status")
        var status: Int, // 1
        @SerializedName("typeofHome")
        var typeofHome: String,
        @SerializedName("updatedAt")
        var updatedAt: String, // 2022-02-22T07:52:17.000Z
        @SerializedName("user_images")
        var userImages: ArrayList<CompleteProfileUserImage>,
        @SerializedName("userType")
        var userType: Int, // 1
        @SerializedName("username")
        var username: String, // navi
        @SerializedName("wallet")
        var wallet: Int, // 0
        @SerializedName("whyLoveThis")
        var whyLoveThis: String,
        @SerializedName("whychooseme")
        var whychooseme: String
    ) {
        data class CompleteProfileUserImage(
            @SerializedName("createdAt")
            var createdAt: String, // 2022-02-22T07:52:17.000Z
            @SerializedName("id")
            var id: Int, // 1120
            @SerializedName("image_path")
            var imagePath: String, // string
            @SerializedName("status")
            var status: Boolean, // true
            @SerializedName("updatedAt")
            var updatedAt: String, // 2022-02-22T07:52:17.000Z
            @SerializedName("user_id")
            var userId: Int // 316
        )
    }
}