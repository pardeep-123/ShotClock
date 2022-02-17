package com.app.shotclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.*
import com.app.shotclock.repo.LoginSignUpRepo
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class LoginSignUpViewModel @Inject
constructor(private val loginSignUpRepo: LoginSignUpRepo) : ViewModel() {

    // user login
    fun loginUser(data: LoginRequestModel): LiveData<Resource<LoginResponseModel>> {
        return liveData(Dispatchers.IO) {
            // Show Loading
            emit(Resource.loading(null))
            val response = loginSignUpRepo.loginUser(data)
            emit(response)
        }
    }

    // forgot password
    fun forgotPassword(data: ForgotPasswordRequest): LiveData<Resource<BaseResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.forgotPassword(data)
            emit(response)
        }
    }

    // user signup
    fun userSignUp(
        request: Map<String, RequestBody>, file: MultipartBody.Part?
    ): LiveData<Resource<SignUpResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.userSignUp(request, file)
            emit(response)
        }
    }

    // change password
    fun changePassword(data: ChangePassRequestModel): LiveData<Resource<BaseResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.changePassword(data)
            emit(response)
        }
    }

    // terms and conditions
    fun termsConditions(): LiveData<Resource<TermsAndConditionResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.termsConditions()
            emit(response)
        }
    }

    // copy right notice
    fun copyRightNotice(): LiveData<Resource<CopyRightNoticeResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.copyRightNotice()
            emit(response)
        }
    }

    // safe dating policy
    fun safeDatingPolicy(): LiveData<Resource<SafeDatingPolicyResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.safeDatingPolicy()
            emit(response)
        }
    }

    // privacy policy
    fun privacyPolicy(): LiveData<Resource<PrivacyPolicyResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.privacyPolicy()
            emit(response)
        }
    }

    // cookie policy
    fun cookiePolicy(): LiveData<Resource<CookiePolicyResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.cookiePolicy()
            emit(response)
        }
    }

    // user logout
    fun userLogout(): LiveData<Resource<BaseResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.userLogout()
            emit(response)
        }
    }

    // user profile
    fun userProfile(): LiveData<Resource<ProfileViewModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.userProfile()
            emit(response)
        }
    }

}