package com.app.shotclock.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SerializeObjectCommon {
    //convert a data class to a map
    fun <T> T.serializeToMap(): Map<String, String> {
        return convert()
    }

    //convert an object of type I to type O
    inline fun <I, reified O> I.convert(): O {
        val json = Gson().toJson(this)
        return Gson().fromJson(json, object : TypeToken<O>() {}.type)
    }
}