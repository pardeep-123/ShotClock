package com.app.shotclock.repo

import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.LoginRequestModel
import com.app.shotclock.models.LoginResponseModel
import com.app.shotclock.models.SignUpResponseModel
import com.app.shotclock.retrofit.ApiService
import com.app.shotclock.retrofit.ResponseHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSignUpRepo @Inject
constructor(private val apiService: ApiService, private val responseHandler: ResponseHandler) {
    // login user
    suspend fun loginUser(data: LoginRequestModel): Resource<LoginResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.loginUser(data))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // forgot password
    suspend fun forgotPassword(type: String): Resource<BaseResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.forgotPassword(type))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // user signup
    suspend fun userSignUp(request: Map<String, RequestBody>, file: MultipartBody.Part?): Resource<SignUpResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.userSignUp(request, file))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


}


