package com.app.shotclock.di.module


import com.app.shotclock.activities.InitialActivity
import com.app.shotclock.base.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {


//    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class,InitialFragmentBuilder::class])
//    abstract  fun bindInitialRiderActivity(): InitialRiderActivity

    @get:ContributesAndroidInjector
    internal abstract val baseActivity: BaseActivity

    @get:ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract val initialActivity: InitialActivity

//    @get:ContributesAndroidInjector(modules = [FragmentModule::class])
//    internal abstract val homeActivity: HomeActivity

//    @get:ContributesAndroidInjector(modules = [FragmentModule::class])
//    internal abstract val homeActivity: HomeActivity
//
//    HomeActivity
//    @get:ContributesAndroidInjector
//    internal abstract val providerMapFragment: ProviderMapFragment


}