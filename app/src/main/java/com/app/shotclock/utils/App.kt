package com.app.shotclock.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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

        fun getInstance(): App {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        context = this
        lifecycleHandler = AppLifecycleHandler(this)

        ProcessLifecycleOwner.get().lifecycle
            .addObserver(this)

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
        initializePrefs()
        registerLifecycleHandler(lifecycleHandler!!)
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

    private fun registerLifecycleHandler(lifecycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifecycleHandler)
        registerComponentCallbacks(lifecycleHandler)
    }

    override fun onAppForegrounded() {
        if (!SocketManager.isConnected()) {
            SocketManager.initSocket()
        }
    }

    override fun onAppBackgrounded() {
        SocketManager.onDisConnect()
    }
}