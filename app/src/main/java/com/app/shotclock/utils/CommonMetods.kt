package com.app.shotclock.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommonMetods {

    fun time_to_timestamp(str_date: String?, pattren: String?): Long {
        var time_stamp: Long = 0
        try {
            val formatter = SimpleDateFormat(pattren)
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
            val date: Date = formatter.parse(str_date) as Date
            time_stamp = date.getTime()
        } catch (ex: ParseException) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        time_stamp = time_stamp / 1000
        return time_stamp
    }
    fun hideKeyboard(view: View, activity: Activity){
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}