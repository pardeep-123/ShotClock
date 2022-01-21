package com.app.shotclock.cache

import android.content.Context
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.utils.Prefs
import retrofit2.http.Body

fun getUser(context: Context): Body? {
    return Prefs.with(context).getObject(CacheConstants.USER_DATA, Body::class.java)
}

fun saveUser(context: Context, user: Body) {
    Prefs.with(context).save(CacheConstants.USER_DATA, user)
}

fun getToken(context: Context): String? {
    return Prefs.with(context).getString(CacheConstants.USER_TOKEN, "")
}

fun saveToken(context: Context, token: String) {
    Prefs.with(context).save(CacheConstants.USER_TOKEN, token)
}


fun saveString(context: Context, key: String, value: String) {
    Prefs.with(context).save(key, value)
}

fun getMyString(context: Context, key: String): String? {
    return Prefs.with(context).getString(key, "")
}

fun clearData(context: Context, key: String) {
    Prefs.with(context).remove(key)
}

fun clearAllData(context: Context) {
    Prefs.with(context).removeAll()
}
