package com.app.shotclock.repo

import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.EditProfileRequestModel
import com.app.shotclock.models.FileUploadResponse
import com.app.shotclock.models.LoginResponseModel
import com.app.shotclock.models.ProfileViewModel
import com.app.shotclock.retrofit.ApiService
import com.app.shotclock.retrofit.ResponseHandler
import com.app.shotclock.utils.inValidAuth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepo @Inject constructor(private val apiService: ApiService,private val responseHandler: ResponseHandler){

    // user profile
    suspend fun userProfile(): Resource<ProfileViewModel> {
        return try {
            responseHandler.handleResponse(apiService.userProfile())
        } catch (e: Exception) {
            try {
                if ((e as HttpException).code() == 401)
                    inValidAuth()
            } catch (e: Exception) {
                e.toString()
            }
            responseHandler.handleException(e)
        }
    }

    // edit profile
    suspend fun editProfile(data: EditProfileRequestModel): Resource<LoginResponseModel> {
        return try {
            responseHandler.handleResponse(apiService.editProfile(data))
        } catch (e: Exception) {
            try {
                if ((e as HttpException).code() == 401)
                    inValidAuth()
            } catch (e: Exception) {
                e.toString()
            }
            responseHandler.handleException(e)
        }
    }

    // file upload
    suspend fun fileUpload(image: ArrayList<MultipartBody.Part>, partMap: Map<String, RequestBody>): Resource<FileUploadResponse> {
        return try {
            responseHandler.handleResponse(apiService.fileUpload(image,partMap))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}