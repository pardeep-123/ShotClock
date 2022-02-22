package com.app.shotclock.retrofit

import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.http.Body

interface ApiService {

    // user login
    @POST(ApiConstants.USER_LOGIN)
    suspend fun loginUser(@Body body: LoginRequestModel): LoginResponseModel

    // forgot password
    @POST(ApiConstants.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body data: ForgotPasswordRequest): BaseResponseModel

    // user signup
    @Multipart
    @POST(ApiConstants.USER_SIGNUP)
    suspend fun userSignUp(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>
    ): SignUpResponseModel

    //change password
    @POST(ApiConstants.CHANGE_PASSWORD)
    suspend fun changePassword(@Body data: ChangePassRequestModel): BaseResponseModel

    // terms and conditions
    @GET(ApiConstants.TERMS_AND_CONDITIONS)
    suspend fun termsConditions(): TermsAndConditionResponse

    // copy right notice
    @GET(ApiConstants.COPY_RIGHT_NOTICE)
    suspend fun copyRightNotice(): CopyRightNoticeResponse

    // safe dating policy
    @GET(ApiConstants.SAFE_DATING_POLICY)
    suspend fun safeDatingPolicy(): SafeDatingPolicyResponse

    // privacy policy
    @GET(ApiConstants.PRIVACY_POLICY)
    suspend fun privacyPolicy(): PrivacyPolicyResponse

    @GET(ApiConstants.COOKIE_POLICY)
    suspend fun cookiePolicy(): CookiePolicyResponse

    @GET(ApiConstants.LOGOUT)
    suspend fun userLogout(): BaseResponseModel

    @GET(ApiConstants.USER_PROFILE)
    suspend fun userProfile(): ProfileViewModel

    @Multipart
    @POST(ApiConstants.FILE_UPLOAD)
    suspend fun fileUpload(@Part image : ArrayList<MultipartBody.Part>):FileUploadResponse

    @POST(ApiConstants.COMPLETE_PROFILE)
    suspend fun completeProfile(@Body data:CompleteProfileRequestModel):CompleteProfileResponse

}
