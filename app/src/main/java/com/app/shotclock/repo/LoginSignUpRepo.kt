package com.app.shotclock.repo

import com.app.shotclock.retrofit.ApiService
import com.app.shotclock.retrofit.ResponseHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSignUpRepo @Inject
constructor(val apiService: ApiService, val responseHandler: ResponseHandler) {

}


