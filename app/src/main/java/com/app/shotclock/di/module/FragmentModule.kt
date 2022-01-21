package com.app.shotclock.di.module

import com.app.shotclock.fragments.LoginFragment
import com.app.shotclock.fragments.SplashFragment
import com.app.shotclock.fragments.WalkThroughFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun splashFragment(): SplashFragment

    @ContributesAndroidInjector
    internal abstract fun walkThroughFragment(): WalkThroughFragment

    @ContributesAndroidInjector
    internal abstract fun loginFragment(): LoginFragment


}