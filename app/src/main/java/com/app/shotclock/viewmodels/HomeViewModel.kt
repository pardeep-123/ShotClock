package com.app.shotclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.repo.HomeRepo
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepo: HomeRepo) : ViewModel() {
    // home api
    fun homeApi(latitude: String, longitude: String): LiveData<Resource<BaseResponseModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val response = homeRepo.homeApi(latitude, longitude)
            emit(response)
        }
    }
}