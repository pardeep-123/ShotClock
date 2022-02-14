package com.app.shotclock.retrofit

import com.app.shotclock.models.BaseResponseModel


object ResponseValidator {

    fun <T : BaseResponseModel> isResponseValid(model: T): Boolean {

        if (model.code == 200) {
            return true
        }

        return false

    }

}