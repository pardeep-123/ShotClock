package com.app.shotclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.*
import com.app.shotclock.repo.ProfileRepo
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileViewModels @Inject constructor(private val profileRepo: ProfileRepo) : ViewModel() {

    // user profile
    fun userProfile(): LiveData<Resource<ProfileViewModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = profileRepo.userProfile()
            emit(response)
        }
    }

    // edit profile
    fun editProfile(data: EditProfileRequestModel): LiveData<Resource<LoginResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = profileRepo.editProfile(data)
            emit(response)
        }
    }


    // file upload
    fun fileUpload(image: ArrayList<MultipartBody.Part>,partMap: Map<String, RequestBody>): LiveData<Resource<FileUploadResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = profileRepo.fileUpload(image,partMap)
            emit(response)
        }
    }
}