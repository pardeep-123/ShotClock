package com.app.shotclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.*
import com.app.shotclock.repo.HomeRepo
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepo: HomeRepo) : ViewModel() {
    // home api
    fun homeApi(latitude: String, longitude: String): LiveData<Resource<HomeResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.homeApi(latitude, longitude)
            emit(response)
        }
    }

    // filter api
    fun filterApi(data: FilterRequestModel): LiveData<Resource<BaseResponseModel>>{
        return liveData(Dispatchers.IO){
            emit(Resource.loading(null))
            val response = homeRepo.filterApi(data)
            emit(response)
        }
    }

    // subscription plans
    fun subscriptionPlans():LiveData<Resource<SubscriptionPlansResponse>>{
        return liveData(Dispatchers.IO){
            emit(Resource.loading(null))
            val response = homeRepo.subscriptionPlans()
            emit(response)
        }
    }

    // selection done
    fun selectionDone(data: SelectionDoneRequestModel): LiveData<Resource<SelectionDoneResponse>>{
        return liveData(Dispatchers.IO){
            emit(Resource.loading(null))
            val response = homeRepo.selectionDone(data)
            emit(response)
        }
    }

    // request list
    fun requestList(): LiveData<Resource<RequestListResponseModel>>{
        return liveData(Dispatchers.IO){
            emit(Resource.loading(null))
            val response = homeRepo.requestList()
            emit(response)
        }
    }

}