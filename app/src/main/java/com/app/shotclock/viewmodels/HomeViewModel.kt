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
    fun filterApi(data: FilterRequestModel): LiveData<Resource<HomeResponseModel>>{
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
    fun requestList(): LiveData<Resource<RequestListResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.requestList()
            emit(response)
        }
    }

    // all request list
    fun allRequestList(): LiveData<Resource<AllRequestResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.allRequestList()
            emit(response)
        }
    }

    // get notifications
    fun getNotifications(): LiveData<Resource<GetNotificationResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.getNotifications()
            emit(response)
        }
    }

    // received list
    fun receivedList(): LiveData<Resource<ReceivedListResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.receivedList()
            emit(response)
        }
    }

    // ice breaker question
    fun iceBreakerQuestions(): LiveData<Resource<IceBreakerQuestionResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.iceBreakerQuestions()
            emit(response)
        }
    }

    // cancel request admin
    fun cancelRequestAdmin(data: CancelRequestAdminRequest): LiveData<Resource<BaseResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.cancelRequestAdmin(data)
            emit(response)
        }
    }

    // accept decline request
    fun acceptDeclineRequest(data: AcceptDeclineRequestModel): LiveData<Resource<BaseResponseModel>>{
        return liveData(Dispatchers.IO){
            emit(Resource.loading(null))
            val response = homeRepo.acceptDeclineRequest(data)
            emit(response)
        }
    }

}