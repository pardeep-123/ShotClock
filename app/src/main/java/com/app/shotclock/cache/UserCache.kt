package com.app.shotclock.cache

import android.content.Context
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.constants.CacheConstants.USER_AUTH
import com.app.shotclock.models.Body
import com.app.shotclock.utils.Prefs

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


fun saveString(context: Context, value: String) {
    Prefs.with(context).save(USER_AUTH, value)
}

fun getMyString(context: Context): String? {
    return Prefs.with(context).getString(USER_AUTH, "")
}


fun saveChatString(context: Context, value: String) {
    Prefs.with(context).save("statusChat", value)
}

fun getChatString(context: Context): String? {
    return Prefs.with(context).getString("statusChat", "")
}

fun saveIsLoginString(context: Context, value: String) {
    Prefs.with(context).save("isLogin", value)
}

fun getIsLoginString(context: Context): String? {
    return Prefs.with(context).getString("isLogin", "")
}

fun saveIsSocialLoginString(context: Context, value: String) {
    Prefs.with(context).save("isSocialLogin", value)
}

fun getIsSocialLoginString(context: Context): String? {
    return Prefs.with(context).getString("isSocialLogin", "")
}


fun clearData(context: Context, key: String) {
    Prefs.with(context).remove(key)
}

fun clearAllData(context: Context) {
    Prefs.with(context).removeAll()
}
