package com.app.shotclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.LoginRequestModel
import com.app.shotclock.models.LoginResponseModel
import com.app.shotclock.models.SignUpResponseModel
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
    fun forgotPassword(type: String): LiveData<Resource<BaseResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.forgotPassword(type)
            emit(response)
        }
    }

    // user signup
    fun userSignUp(
        request: Map<String, RequestBody>, file: MultipartBody.Part?): LiveData<Resource<SignUpResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = loginSignUpRepo.userSignUp(request, file)
            emit(response)
        }
    }

}