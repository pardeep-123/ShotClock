package com.app.shotclock.utils

import android.content.Context
import android.preference.PreferenceManager

class RememberShared(val context: Context) {

    fun setString(key: String, value: String) {

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getString(key: String): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(key, "").toString()
    }

    fun clearShared() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        val editor = prefs.edit()
        editor.clear().apply()
    }
}