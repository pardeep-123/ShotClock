package com.app.shotclock.repo

import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.EditProfileRequestModel
import com.app.shotclock.models.EditProfileResponse
import com.app.shotclock.models.FileUploadResponse
import com.app.shotclock.models.ProfileViewModel
import com.app.shotclock.retrofit.ApiService
import com.app.shotclock.retrofit.ResponseHandler
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepo @Inject constructor(private val apiService: ApiService,private val responseHandler: ResponseHandler){

    // user profile
    suspend fun userProfile(): Resource<ProfileViewModel> {
        return try {
            responseHandler.handleResponse(apiService.userProfile())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // edit profile
    suspend fun editProfile(data: EditProfileRequestModel):Resource<EditProfileResponse>{
        return try {
            responseHandler.handleResponse(apiService.editProfile(data))
        }catch (e: Exception){
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

}