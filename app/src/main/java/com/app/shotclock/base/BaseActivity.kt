package com.app.shotclock.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

//    @Inject
//    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

    }

    //    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
//        return supportFragmentInjector
//    }
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}