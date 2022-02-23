package com.app.shotclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.EditProfileRequestModel
import com.app.shotclock.models.EditProfileResponse
import com.app.shotclock.models.FileUploadResponse
import com.app.shotclock.models.ProfileViewModel
import com.app.shotclock.repo.ProfileRepo
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
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
    fun editProfile(data: EditProfileRequestModel): LiveData<Resource<EditProfileResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = profileRepo.editProfile(data)
            emit(response)
        }
    }


    // file upload
    fun fileUpload(image: ArrayList<MultipartBody.Part>): LiveData<Resource<FileUploadResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = profileRepo.fileUpload(image)
            emit(response)
        }
    }
}