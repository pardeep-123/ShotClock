package com.app.shotclock.retrofit

import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.LoginRequestModel
import com.app.shotclock.models.LoginResponseModel
import com.app.shotclock.models.SignUpResponseModel
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

// user login
    @POST(ApiConstants.USER_LOGIN)
    suspend fun loginUser(@Body body: LoginRequestModel): LoginResponseModel

// forgot password
    @FormUrlEncoded
    @POST(ApiConstants.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Field("email")type: String):BaseResponseModel

    // user signup
    @Multipart
    @POST(ApiConstants.USER_SIGNUP)
    suspend fun userSignUp(@PartMap partMap: Map<String,@JvmSuppressWildcards RequestBody>,@Part file : MultipartBody.Part?):SignUpResponseModel
}