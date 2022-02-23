package com.app.shotclock.repo

import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.*
import com.app.shotclock.retrofit.ApiService
import com.app.shotclock.retrofit.ResponseHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun forgotPassword(data: ForgotPasswordRequest): Resource<BaseResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.forgotPassword(data))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // user signup
    suspend fun userSignUp(
        request: Map<String, RequestBody>
    ): Resource<SignUpResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.userSignUp(request))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // change password
    suspend fun changePassword(data: ChangePassRequestModel): Resource<BaseResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.changePassword(data))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // terms and conditions
    suspend fun termsConditions(): Resource<TermsAndConditionResponse> {
        return try {
            responseHandler.handleResponse(apiService.termsConditions())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // copy right notice
    suspend fun copyRightNotice(): Resource<CopyRightNoticeResponse> {
        return try {
            responseHandler.handleResponse(apiService.copyRightNotice())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // safe dating policy
    suspend fun safeDatingPolicy(): Resource<SafeDatingPolicyResponse> {
        return try {
            responseHandler.handleResponse(apiService.safeDatingPolicy())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // privacy policy
    suspend fun privacyPolicy(): Resource<PrivacyPolicyResponse> {
        return try {
            responseHandler.handleResponse(apiService.privacyPolicy())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // cookie Policy
    suspend fun cookiePolicy(): Resource<CookiePolicyResponse> {
        return try {
            responseHandler.handleResponse(apiService.cookiePolicy())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // user logout
    suspend fun userLogout(): Resource<BaseResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.userLogout())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // file upload
    suspend fun fileUpload(image: ArrayList<MultipartBody.Part>): Resource<FileUploadResponse> {
        return try {
            responseHandler.handleResponse(apiService.fileUpload(image))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // complete profile
    suspend fun completeProfile(data:CompleteProfileRequestModel): Resource<CompleteProfileResponse>{
        return try {
            responseHandler.handleResponse(apiService.completeProfile(data))
        }catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

}


