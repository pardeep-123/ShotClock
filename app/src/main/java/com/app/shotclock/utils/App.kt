package com.app.shotclock.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.app.shotclock.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector, LifecycleObserver {

    var shared: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
//    private var mSocketManager: SocketManager? = null
//
    companion object {
        var context: Context? = null

        var app: App? = null

        fun getInstance(): App {
            return app!!
        }
    }
//
//    fun getSocketManager(): SocketManager? {
//        if (mSocketManager == null) {
//            mSocketManager = SocketManager()
//        } else {
//            return mSocketManager
//        }
//        return mSocketManager
//    }

    override fun onCreate() {
        super.onCreate()
        app = this

        context = this

        ProcessLifecycleOwner.get().lifecycle
            .addObserver(this)

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
        initializePrefs()
    }

    @SuppressLint("CommitPrefEdits")
    fun initializePrefs() {
        shared = getSharedPreferences("myprefs", MODE_PRIVATE)
        editor = shared?.edit()
    }

    fun setString(key: String?, value: String?) {
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(key: String?): String? {
        return shared?.getString(key, "")
    }
    fun clearShared() {
        editor?.run {
            clear()
            apply()
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        Log.d("Lifecycle", "Returning to foreground…")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        Log.d("Lifecycle", "Moving to background…")

    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}