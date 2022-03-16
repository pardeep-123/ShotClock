package com.app.shotclock.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.app.shotclock.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector, LifecycleObserver,
    AppLifecycleHandler.AppLifecycleDelegates {

    var shared: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private var lifecycleHandler: AppLifecycleHandler? = null

    //    private var mSocketManager: SocketManager? = null
//
    companion object {
        var context: Context? = null

        var app: App? = null

        var mSocketManager: SocketManager? = null
        lateinit var mInstance: App
        fun getInstance(): App {
            return mInstance
        }
    }

    fun getSocketManager(): SocketManager? {
        mSocketManager = if (mSocketManager == null) {
            SocketManager()
        } else {
            return mSocketManager
        }
        return mSocketManager
    }

    override fun onCreate() {
        super.onCreate()

        mInstance = this

        mSocketManager = getSocketManager()
        context = this
        lifecycleHandler = AppLifecycleHandler(this)

        ProcessLifecycleOwner.get().lifecycle
            .addObserver(this)

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
        initializePrefs()
        registerLifeCycleHandler(lifecycleHandler!!)
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

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onAppBackgrounded() {
        Log.e("Application", "Background")
//        mSocketManager!!.disconnectAll()
    }

    override fun onAppForegrounded() {
        Log.e("Application", "Foreground")
        if (!mSocketManager!!.isConnected() || mSocketManager!!.getmSocket() == null) {
            mSocketManager!!.init()
        }
    }

    private fun registerLifeCycleHandler(lifeCycleHandler: AppLifecycleHandler?) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }


}