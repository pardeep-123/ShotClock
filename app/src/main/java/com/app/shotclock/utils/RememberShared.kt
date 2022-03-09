package com.app.shotclock.utils

import android.content.Context
import android.preference.PreferenceManager
import com.app.shotclock.R

class RememberShared(val context: Context) {

    fun setString(key: String, value: String) {

        //Store in SharedPreference
        val preference=context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor=preference?.edit()
        editor?.putString(key,value)
        editor?.apply()

    }

    fun getString(key: String): String {
        //Retrieve from SharedPreference
        val preference=context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        return preference.getString(key,"").toString()
    }

    fun clearShared() {
        val preference=context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.clear().apply()

    }
}