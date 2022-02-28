package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class AllRequestResponseModel(
    @SerializedName("body")
    var body: ArrayList<ArrayList<AllRequestBody>>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // data get successfully
    @SerializedName("success")
    var success: Int // 1
) {
    data class AllRequestBody(
        @SerializedName("callStart")
        var callStart: Int, // 0
        @SerializedName("callStatus")
        var callStatus: Int, // 0
        @SerializedName("createdAt")
        var createdAt: String, // 2021-10-28T05:39:17.000Z
        @SerializedName("groupName")
        var groupName: String, // eb741b4e1d1d4438363fffc38e65ce6078db5729
        @SerializedName("id")
        var id: Int, // 1068
        @SerializedName("isCancelledByAdmin")
        var isCancelledByAdmin: Int, // 0
        @SerializedName("request_to")
        var request_to: String, // 271
        @SerializedName("requestTo")
        var requestTo: RequestTo,
        @SerializedName("status")
        var status: Int, // 2
        @SerializedName("updatedAt")
        var updatedAt: String, // 2021-10-28T05:39:17.000Z
        @SerializedName("userId")
        var userId: Int // 295
    ) {
        data class RequestTo(
            @SerializedName("acceptedPettype")
            var acceptedPettype: String,
            @SerializedName("accesstoOutdorr")
            var accesstoOutdorr: Int, // 0
            @SerializedName("address")
            var address: String,
            @SerializedName("addshort")
            var addshort: String,
            @SerializedName("astrologicalSign")
            var astrologicalSign: Any?, // null
            @SerializedName("authKey")
            var authKey: String,
            @SerializedName("bio")
            var bio: String, // 0
            @SerializedName("category")
            var category: Int, // 0
            @SerializedName("city")
            var city: String,
            @SerializedName("country")
            var country: Int, // 0
            @SerializedName("countryCode")
            var countryCode: String, // 0
            @SerializedName("createdAt")
            var createdAt: String, // 2021-10-26T07:09:35.000Z
            @SerializedName("dateofbirth")
            var dateofbirth: String,
            @SerializedName("deviceToken")
            var deviceToken: String,
            @SerializedName("deviceType")
            var deviceType: Int, // 0
            @SerializedName("distanceWillingtravel")
            var distanceWillingtravel: String,
            @SerializedName("dob_timestamp")
            var dobTimestamp: String,
            @SerializedName("drinking")
            var drinking: Any?, // null
            @SerializedName("email")
            var email: String, // cqlsystechnologiespvtltd@gmail.com
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
            var height: String,
            @SerializedName("id")
            var id: Int, // 271
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
            var password: String,
            @SerializedName("password_token")
            var passwordToken: Any?, // null
            @SerializedName("pets")
            var pets: Any?, // null
            @SerializedName("phone")
            var phone: String, // 0
            @SerializedName("phoneVerified")
            var phoneVerified: Int, // 0
            @SerializedName("profileImage")
            var profileImage: String, // https://lh3.googleusercontent.com/a/AATXAJyLQurHA4NHH9DFB1kc4NgPzO2LXo5E6mA6j-yJ=s1000
            @SerializedName("qualification")
            var qualification: String,
            @SerializedName("rewardPoints")
            var rewardPoints: Int, // 0
            @SerializedName("serviceCategory")
            var serviceCategory: Int, // 0
            @SerializedName("sexualOrientation")
            var sexualOrientation: Any?, // null
            @SerializedName("smoking")
            var smoking: Any?, // null
            @SerializedName("socialId")
            var socialId: String, // 116674435921134947260
            @SerializedName("social_type")
            var socialType: Int?, // 1
            @SerializedName("status")
            var status: Int, // 1
            @SerializedName("typeofHome")
            var typeofHome: String,
            @SerializedName("updatedAt")
            var updatedAt: String, // 2021-10-29T05:16:11.000Z
            @SerializedName("userType")
            var userType: Int, // 1
            @SerializedName("username")
            var username: String, // Cqlsys Technologies Pvt Ltd
            @SerializedName("wallet")
            var wallet: Int, // 0
            @SerializedName("whyLoveThis")
            var whyLoveThis: String,
            @SerializedName("whychooseme")
            var whychooseme: String
        )
    }
}