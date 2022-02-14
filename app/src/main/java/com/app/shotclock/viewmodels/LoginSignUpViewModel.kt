package com.app.shotclock.viewmodels

import androidx.lifecycle.ViewModel
import com.app.shotclock.repo.LoginSignUpRepo
import javax.inject.Inject

class LoginSignUpViewModel @Inject
constructor(val loginSignUpRepo: LoginSignUpRepo) : ViewModel() {

}