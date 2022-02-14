package com.app.shotclock.retrofit

import android.content.Context
import com.app.shotclock.cache.getUser
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        val token: String? = getUser(context)?.authKey

        val headers:Headers

        if (token != null){
            headers = request.headers.newBuilder()
                .add("Content-Type", "application/json")
                .add("Accept", "application/json")
                .add("auth_key", token)
                .add("security_key","appointme11")
                .build()
        }else{
            headers = request.headers.newBuilder()
                .add("Content-Type", "application/json")
                .add("Accept", "application/json")
                .add("security_key","appointme11")
                .build()
        }

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }
}