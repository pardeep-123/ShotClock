package com.app.shotclock.repo

import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.retrofit.ApiService
import com.app.shotclock.retrofit.ResponseHandler
import javax.inject.Inject

class HomeRepo @Inject constructor(private var apiService: ApiService, private var responseHandler: ResponseHandler) {

    // home api
    suspend fun homeApi(latitude: String,longitude:String): Resource<BaseResponseModel>{
        return try {
            responseHandler.handleResponse(apiService.homeApi(latitude, longitude))
        }catch (e :Exception){
            responseHandler.handleException(e)
        }
    }

}