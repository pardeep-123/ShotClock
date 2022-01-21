package com.app.shotclock.di.module

import com.app.shotclock.fragments.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun splashFragment(): SplashFragment


}