package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class HomeResponseModel(
    @SerializedName("body")
    var body: ArrayList<HomeBody>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // homelist get successfully
    @SerializedName("success")
    var success: Int // 1
) {
    data class HomeBody(
        @SerializedName("accepted_pettype")
        var acceptedPettype: String,
        @SerializedName("accessto_outdorr")
        var accesstoOutdorr: Int, // 0
        @SerializedName("address")
        var address: String, // Mohali Railway Station Road
        @SerializedName("addshort")
        var addshort: String,
        @SerializedName("astrologicalSign")
        var astrologicalSign: String?, // Aquarius
        @SerializedName("auth_key")
        var authKey: String,
        @SerializedName("bio")
        var bio: String, // BBnNnabBbnNmmM
        @SerializedName("category")
        var category: Int, // 0
        @SerializedName("city")
        var city: String,
        @SerializedName("country")
        var country: Int, // 0
        @SerializedName("country_code")
        var countryCode: String, // +91
        @SerializedName("created_at")
        var createdAt: String, // 2021-10-26T06:02:10.000Z
        @SerializedName("dateofbirth")
        var dateofbirth: String, // 26-Oct-2004
        @SerializedName("device_token")
        var deviceToken: String,
        @SerializedName("device_type")
        var deviceType: Int, // 1
        @SerializedName("distance")
        var distance: Double, // 2.8974593392486034
        @SerializedName("distance_willingtravel")
        var distanceWillingtravel: String,
        @SerializedName("drinking")
        var drinking: String?, // Yes
        @SerializedName("email")
        var email: String, // digi@yopmail.com
        @SerializedName("email_verified")
        var emailVerified: Int, // 0
        @SerializedName("emergency_transport")
        var emergencyTransport: Int, // 0
        @SerializedName("fb_id")
        var fbId: Any?, // null
        @SerializedName("fb_verified")
        var fbVerified: Int, // 0
        @SerializedName("forgotPassword")
        var forgotPassword: String,
        @SerializedName("gender")
        var gender: Int, // 1
        @SerializedName("google_id")
        var googleId: Any?, // null
        @SerializedName("google_verified")
        var googleVerified: Int, // 0
        @SerializedName("height")
        var height: String, // 139.7
        @SerializedName("id")
        var id: Int, // 261
        @SerializedName("interested")
        var interested: Int, // 2
        @SerializedName("is_featured")
        var isFeatured: Int, // 0
        @SerializedName("is_online")
        var isOnline: String, // 1
        @SerializedName("is_professional")
        var isProfessional: Int, // 0
        @SerializedName("is_terms_accept")
        var isTermsAccept: Int, // 0
        @SerializedName("language")
        var language: Int, // 0
        @SerializedName("lastname")
        var lastname: String,
        @SerializedName("lat")
        var lat: String, // 30.6834677
        @SerializedName("levelof_supervision")
        var levelofSupervision: String,
        @SerializedName("lng")
        var lng: String, // 76.735629
        @SerializedName("login_phase")
        var loginPhase: Int, // 0
        @SerializedName("login_type")
        var loginType: Int, // 1
        @SerializedName("notification_status")
        var notificationStatus: Int, // 0
        @SerializedName("numberofwalks_perday")
        var numberofwalksPerday: Int, // 0
        @SerializedName("otp")
        var otp: String, // 1111
        @SerializedName("otpVerify")
        var otpVerify: Int, // 0
        @SerializedName("password")
        var password: String, // 7c222fb2927d828af22f592134e8932480637c0d
        @SerializedName("password_token")
        var passwordToken: Any?, // null
        @SerializedName("pets")
        var pets: String?, // Yes
        @SerializedName("phone")
        var phone: String, // 8523697412
        @SerializedName("phone_verified")
        var phoneVerified: Int, // 0
        @SerializedName("profile_image")
        var profileImage: String, // /uploads/user_images/1635228050356-file.jpeg
        @SerializedName("qualification")
        var qualification: String, // Bachelor's Degree
        @SerializedName("reward_points")
        var rewardPoints: Int, // 0
        @SerializedName("service_category")
        var serviceCategory: Int, // 0
        @SerializedName("sexualOrientation")
        var sexualOrientation: String?, // Gay
        @SerializedName("smoking")
        var smoking: String?, // Yes
        @SerializedName("social_id")
        var socialId: String,
        @SerializedName("status")
        var status: Int, // 1
        @SerializedName("typeof_home")
        var typeofHome: String,
        @SerializedName("updated_at")
        var updatedAt: String, // 2021-10-26T06:09:46.000Z
        @SerializedName("user_images")
        var userImages: ArrayList<HomeUserImage>,
        @SerializedName("user_type")
        var userType: Int, // 1
        @SerializedName("username")
        var username: String, // digi
        @SerializedName("wallet")
        var wallet: Int, // 0
        @SerializedName("why_Love_this")
        var whyLoveThis: String,
        @SerializedName("whychooseme")
        var whychooseme: String,
        @SerializedName("is_select")
        var isSelect: Boolean = false
    ) {
        data class HomeUserImage(
            @SerializedName("createdAt")
            var createdAt: String, // 2021-10-26T06:06:42.000Z
            @SerializedName("id")
            var id: Int, // 1046
            @SerializedName("image_path")
            var imagePath: String, // /uploads/user_images/1635228401985-file.file10_26_21_11:36:3981
            @SerializedName("status")
            var status: Boolean, // true
            @SerializedName("updatedAt")
            var updatedAt: String, // 2021-10-26T06:06:42.000Z
            @SerializedName("user_id")
            var userId: Int // 261
        )
    }
}