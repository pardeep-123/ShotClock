package com.app.shotclock.repo

import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.*
import com.app.shotclock.retrofit.ApiService
import com.app.shotclock.retrofit.ResponseHandler
import javax.inject.Inject

class HomeRepo @Inject constructor(private var apiService: ApiService, private var responseHandler: ResponseHandler) {

    // home api
    suspend fun homeApi(latitude: String,longitude:String): Resource<HomeResponseModel>{
        return try {
            responseHandler.handleResponse(apiService.homeApi(latitude, longitude))
        }catch (e :Exception){
            responseHandler.handleException(e)
        }
    }

    // filter api
    suspend fun filterApi(data: FilterRequestModel): Resource<BaseResponseModel>{
        return try {
            responseHandler.handleResponse(apiService.filterApi(data))
        }catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    // subscription plans
    suspend fun subscriptionPlans():Resource<SubscriptionPlansResponse>{
        return try {
            responseHandler.handleResponse(apiService.subscriptionPlans())
        }catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    // selection done
    suspend fun selectionDone(data: SelectionDoneRequestModel): Resource<SelectionDoneResponse>{
        return try {
            responseHandler.handleResponse(apiService.selectionDone(data))
        }catch (e:Exception){
            responseHandler.handleException(e)
        }
    }

    // request list
    suspend fun requestList(): Resource<RequestListResponseModel>{
        return try {
            responseHandler.handleResponse(apiService.requestList())
        }catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

}