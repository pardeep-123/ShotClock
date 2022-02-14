package com.app.shotclock.retrofit


import android.app.Activity
import android.content.Intent
import com.app.shotclock.activities.InitialActivity
import com.app.shotclock.base.BaseError
import com.app.shotclock.cache.clearAllData
import com.app.shotclock.cache.clearData
import okhttp3.ResponseBody
import org.json.JSONObject

fun getErrorMessage(responseBody: ResponseBody): BaseError {

    try {
        val jsonObject = JSONObject(responseBody.string())

        return BaseError(jsonObject.getInt("statusCode"), jsonObject.getString("message"))

    } catch (e: Exception) {
        return BaseError(101, e.message!!)
    }

}


fun getErrorMessage(jsonObject: JSONObject): BaseError {


    try {
        return BaseError(jsonObject.getInt("statusCode"), jsonObject.getString("message"))

    } catch (e: Exception) {
        return BaseError(101, e.message!!)
    }
}


fun tackleError(activity: Activity, errorBody: BaseError) {


    when (errorBody.code) {

        401 -> {

            // showAlert(activity, errorBody.message,activity.getString(R.string.ok)) {

            activity.startActivity(Intent(activity, InitialActivity::class.java))
            activity.finishAffinity()
            clearAllData(activity)
            clearData(activity, "role")
            clearData(activity, "token")
        }

    }


    // 400 -> {
    //   showAlert(activity, errorBody.message,activity.getString(R.string.ok),{})


}


// }
// when(errorBody.)


//}