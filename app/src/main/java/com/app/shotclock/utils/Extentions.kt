package com.app.shotclock.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.R
import com.app.shotclock.activities.InitialActivity
import com.app.shotclock.adapters.HeightPopupAdapter
import com.app.shotclock.cache.clearAllData
import com.app.shotclock.cache.clearData
import com.app.shotclock.constants.ApiConstants
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.tapadoo.alerter.Alerter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// to show popup
fun myAlert(ctx: Context, messageRes: String, onClick: () -> Unit, yes: String, no: String) {
    val layoutInflater: LayoutInflater =
        ctx.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val popUp: View = layoutInflater.inflate(R.layout.alert_dialog_logout, null)
    val popUpWindowReport = PopupWindow(
        popUp, ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.MATCH_PARENT, true
    )
    popUpWindowReport.showAtLocation(popUp, Gravity.CENTER, 0, 0)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        popUpWindowReport.elevation = 10f
    }
    val tvLogout: TextView = popUp.findViewById(R.id.tvLogout)
    tvLogout.text = no
    val dialogtext11: TextView = popUp.findViewById(R.id.tvAreYouSure)

    val tvCancel: TextView = popUp.findViewById(R.id.tvCancel)
    tvCancel.text = yes
    dialogtext11.text = messageRes
    tvLogout.setOnClickListener {
        onClick()
        popUpWindowReport.dismiss()

    }
    tvCancel.setOnClickListener {
        popUpWindowReport.dismiss()
    }
}

// normal ok alert popup
//fun normalAlert(ctx: Context, messageRes: String, onClick: () -> Unit, yes: String) {
//    val layoutInflater: LayoutInflater =
//        ctx.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    val popUp: View = layoutInflater.inflate(R.layout.normal_popup, null)
//    val popUpWindowReport = PopupWindow(
//        popUp, ConstraintLayout.LayoutParams.MATCH_PARENT,
//        ConstraintLayout.LayoutParams.MATCH_PARENT, true
//    )
//    popUpWindowReport.showAtLocation(popUp, Gravity.CENTER, 0, 0)
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//        popUpWindowReport.elevation = 10f
//    }
//
//    val dialogText11: TextView = popUp.findViewById(R.id.tvAreYouSure)
//
//    val tvYes: TextView = popUp.findViewById(R.id.tvYes)
//    //tvYes.text = yes
//    dialogText11.setText(messageRes)
//
//    tvYes.setOnClickListener {
//        onClick()
//        popUpWindowReport.dismiss()
//    }
//}

fun openImagePopUp(pos: String?, ctx: Context) {

    val popup: View
    val layoutInflater: LayoutInflater =
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    popup = layoutInflater.inflate(R.layout.porflioeimage_popup, null)
    val popupWindow = PopupWindow(
        popup,
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        true
    )
    popupWindow.showAtLocation(popup, Gravity.CENTER, 0, 0)
    popupWindow.isTouchable = false
    popupWindow.isOutsideTouchable = false
    val headImagePopUp: PhotoView = popup.findViewById(R.id.headImagePopUp)
    val backPress: ImageView = popup.findViewById(R.id.backpress)
    backPress.setOnClickListener {
        popupWindow.dismiss()
    }

    Glide.with(ctx).load(ApiConstants.SOCKET_URL + pos).into(headImagePopUp)

}

// for hide keyboard on btn click
fun hideKeyboard(view: View, activity: Activity) {
    val imm =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(view.windowToken, 0)
}

// for showToast
fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

//to set Visibilities
fun View.isVisible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.isGone() {
    this.visibility = View.GONE
}


fun createRequestBody(param: String): RequestBody {
    return param.toRequestBody("text/plain".toMediaTypeOrNull())
}

// to set Date in custom format
fun convertDateStampToTime(timestamp: Long): String? {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = timestamp * 1000
    val outputFormat: DateFormat = SimpleDateFormat("EEEE hh:mm aa")
    //outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return outputFormat.format(cal.time)
}


fun convertDateStampToTime1(timestamp: String): String? {
    val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH)
    // val outputFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
    //outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return simpleDateFormat.format(timestamp.toLong() * 1000L)
}

// to set Date in custom format
fun convertDateToTime(timestamp: Long): String? {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = timestamp * 1000
    val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
    //outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return outputFormat.format(cal.time)
}

// to set time in custom format
fun convertTimeToStampToTime(timestamp: Long): String? {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = timestamp * 1000
    val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
    //outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return outputFormat.format(cal.time)
}

// timeStamp to date
fun convertDateToRatingTime(timestamp: Long): String? {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = timestamp * 1000
    val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    //outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return outputFormat.format(cal.time)
}


// to set time in notification format
fun convertTimeToNotification(timestamp: Long): String? {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = timestamp * 1000
    val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
    //outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return outputFormat.format(cal.time)
}

// automatically date and time formatting by get api
fun getNotificationTime(time_stamp: Long): String {
    var date: Date? = null
    try {
        date = Date(time_stamp * 1000)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    println("dateeee" + date.toString())
    var string_date = ""
    val current = Calendar.getInstance().time
    var diffInSeconds = (current.time - date!!.time) / 1000
    val sec = if (diffInSeconds >= 60) diffInSeconds % 60 else diffInSeconds
    val min = if ((diffInSeconds / 60).also {
            diffInSeconds = it
        } >= 60) diffInSeconds % 60 else diffInSeconds
    val hrs = if ((diffInSeconds / 60).also {
            diffInSeconds = it
        } >= 24) diffInSeconds % 24 else diffInSeconds
    val days = if ((diffInSeconds / 24).also {
            diffInSeconds = it
        } >= 30) diffInSeconds % 30 else diffInSeconds
    val weeks = days / 7
    val months = if ((diffInSeconds / 30).also {
            diffInSeconds = it
        } >= 12) diffInSeconds % 12 else diffInSeconds
    val years = (diffInSeconds / 12).also { diffInSeconds = it }
    when {
        years > 0 -> {
            string_date = if (years == 1L) {
                "1 year"
            } else {
                "$years years"
            }
        }
        months > 0 -> {
            string_date = if (months == 1L) {
                "1 month"
            } else {
                "$months months"
            }
        }
        weeks > 0 -> {
            string_date = if (weeks == 1L) {
                "1 week"
            } else {
                "$weeks Weeks"
            }
        }
        days > 0 -> {
            string_date = if (days == 1L) {
                "1 day"
            } else {
                "$days days"
            }
        }
        hrs > 0 -> {
            string_date = if (hrs == 1L) {
                "1 hour"
            } else {
                "$hrs hours"
            }
        }
        min > 0 -> {
            string_date = if (min == 1L) {
                "1 minute"
            } else {
                "$min minutes"
            }
        }
    }
    string_date = "$string_date ago"
    if (string_date == " ago") {
        string_date = "1 sec" + " ago"
    }
    return string_date
}

fun String.matches(regex: String): Boolean {
    return true
}

fun getUTCDateTimeAsString(): String? {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date())
}

// convert date to timestamp format
fun time_to_timestamp(str_date: String?, pattern: String?): Long {
    var time_stamp: Long = 0
    try {
        val formatter = SimpleDateFormat(pattern)
        //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        val date = formatter.parse(str_date) as Date
        time_stamp = date.time
    } catch (ex: ParseException) {
        ex.printStackTrace()
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
    time_stamp /= 1000
    return time_stamp
}


fun prepareMultiPart(partName: String, image: Any?): MultipartBody.Part {

/* var imageFileBody = MultipartBody.Part.createFormData(partName, "image_"+".jpg", requestBody);
imageArrayBody.add(imageFileBody);*/
    var requestFile: RequestBody? = null
    if (image is File) {

        requestFile = image.asRequestBody("/*".toMediaTypeOrNull())
    } else if (image is ByteArray) {
        requestFile = image.toRequestBody("/*".toMediaTypeOrNull(), 0, image.size)
    }
    return if (image is String) {
        val attachmentEmpty = "".toRequestBody("text/plain".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(partName, "", attachmentEmpty)
    } else
        MultipartBody.Part.createFormData(partName, (image as File).name, requestFile!!)
}

fun time_to_timestampLocal(str_date: String?, pattren: String?): Long {
    var time_stamp: Long = 0
    try {
        val formatter = SimpleDateFormat(pattren)
        //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.timeZone = TimeZone.getDefault()
        val date = formatter.parse(str_date) as Date
        time_stamp = date.time
    } catch (ex: ParseException) {
        ex.printStackTrace()
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
    time_stamp /= 1000
    return time_stamp
}


fun getDateFromUTCTimestamp(mTimestamp: Long, mDateFormate: String?): String? {
    var date: String? = null
    try {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.timeInMillis = mTimestamp * 1000L
        date = android.text.format.DateFormat.format(mDateFormate, cal.timeInMillis).toString()
        val formatter = SimpleDateFormat(mDateFormate)
        formatter.timeZone = TimeZone.getDefault()
        val value = formatter.parse(date)
        val dateFormatter = SimpleDateFormat(mDateFormate)
        dateFormatter.timeZone = TimeZone.getDefault()
        date = dateFormatter.format(value)
        return date
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return date
}

// zuluTime  time convert to string
fun getChatListTime(zuluTime: String): Long {
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val output = SimpleDateFormat("dd/MM/yyyy")

    var d: Date? = null
    try {
        d = input.parse(zuluTime)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val formatted = output.format(d)
    Log.i("DATE", "" + formatted)

    return time_to_timestamp(formatted, "dd/MM/yyyy")
}

fun getNotificationTimeZullu(zuluTime: String): String {
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val output = SimpleDateFormat("hh:mm aa")

    var d: Date? = null
    try {
        d = input.parse(zuluTime)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val formatted = output.format(d)
    Log.i("ZULU DATE", "" + formatted)

    return formatted
}

fun inValidAuth() {
        try {
        clearAllData(App.context!!)
        clearData(App.context!!, "role")
        clearData(App.context!!, "token")
        val intent = Intent(App.context!!, InitialActivity::class.java)
        intent.putExtra("logout", true)
        intent.putExtra("expired", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        App.context?.startActivity(intent)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//fun cardTypeModelSet(): ArrayList<CardTypeModel> {
//    val listOfPattern: ArrayList<CardTypeModel> = ArrayList<CardTypeModel>()
//    val cardTypeModel = CardTypeModel("Visa","^4[0-9]\$","0")
//    listOfPattern.add(cardTypeModel)
//    val cardTypeModel2 = CardTypeModel("Master","^5[1-5]\$","1")
//    listOfPattern.add(cardTypeModel2)
//    return listOfPattern
//}
//------------------------Return Date in String------------------//
fun longToDate(timeInMillis: Long, format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(timeInMillis)
}

fun setPopUpWindow(
    textView: TextView,
    ctx: Context,
    list: ArrayList<String>
) {
    val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view: View = inflater.inflate(R.layout.custom_spinners, null)

    val myPopupWindow = PopupWindow(view, 930, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)
//     myPopupWindow.showAtLocation(text, Gravity.CENTER, 0, 20)
    myPopupWindow.showAsDropDown(textView)

    val rvTextList: RecyclerView = view.findViewById(R.id.rvTextList)
    rvTextList.adapter = HeightPopupAdapter(list, object : HeightPopupAdapter.TextClick {
        override fun clickText(pos: Int) {
            textView.text = list[pos]
            myPopupWindow.dismiss()
        }

    })
}

fun setPopUpWindowTwo(
    textView: TextView,
    ctx: Context,
    list: ArrayList<String>
) {
    val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view: View = inflater.inflate(R.layout.custom_spinner_two, null)

    val myPopupWindow = PopupWindow(view, 932, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)
//     myPopupWindow.showAtLocation(text, Gravity.CENTER, 0, 20)
    myPopupWindow.showAsDropDown(textView)

    val rvTextList: RecyclerView = view.findViewById(R.id.rvTextListTwo)
    rvTextList.adapter = HeightPopupAdapter(list, object : HeightPopupAdapter.TextClick {
        override fun clickText(pos: Int) {
            textView.text = list[pos]
            myPopupWindow.dismiss()
        }

    })
}

fun getBase64FromPath(path: String): String {
    var base64 = ""
    try {
        val file = File(path)
        val buffer = ByteArray(file.length().toInt() + 100)
        val length = FileInputStream(file).read(buffer)
        base64 = android.util.Base64.encodeToString(
            buffer, 0, length,
            android.util.Base64.DEFAULT
        )

    } catch (e: IOException) {
//e.printStackTrace()
    }
    return base64
}

fun isNetworkConnected(): Boolean {
    val cm = App.getInstance().applicationContext
        ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var activeNetwork: NetworkInfo? = null
    if (cm != null) activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}


fun showErrorAlert(context: Activity, msg: String) {
    Alerter.create(context)
        .setIcon(R.drawable.ic_cross)
        .setTitle(context.getString(R.string.error_))
        .setTitleAppearance(R.style.AlertTextAppearanceTitle)
        .setText(msg)
        .setTextAppearance(R.style.AlertTextAppearanceText)
        .setBackgroundColorRes(R.color.red)
        .show()
}