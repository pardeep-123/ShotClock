package com.app.shotclock.retrofit

import android.util.MalformedJsonException
import com.app.shotclock.base.BaseError
import com.app.shotclock.genericdatacontainer.Resource
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

open class ResponseHandler
@Inject
constructor() {
    fun <T : Any> handleResponse(data: T): Resource<T> {
        return Resource.success(data)
    }

    // TODO HANDLE ALL ERROR CASES

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> Resource.error(getErrorMessage(e), null)
            is MalformedJsonException -> Resource.error(getErrorMessage(46456,e), null)
          //  is SocketTimeoutException -> Resource.error(getErrorMessage(25345,e), null)
            is IOException -> Resource.error(getErrorMessage(403,e), null)
            is com.google.gson.JsonSyntaxException -> Resource.error(getErrorMessage(0,e), null)
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE,e), null)
        }
    }
//else -> Resource.error(getErrorMessage(Int.MAX_VALUE), null)
    private fun getErrorMessage(e: HttpException): String {
      return  getErrorMsg(e.response()?.errorBody()!!).message
    }

    private fun getErrorMessage(code: Int , e:java.lang.Exception): String {
        return when (code) {
            404 -> "Not found"
            403 -> "Internet Connection Not Found"
            else -> e.toString()
        }
    }

    fun getErrorMsg(responseBody: ResponseBody): BaseError {

        try {
            val jsonObject = JSONObject(responseBody.string())

            return BaseError(400,jsonObject.getString("message"))


        } catch (e: Exception) {
            return BaseError(400, e.message!!)
        }

    }

}