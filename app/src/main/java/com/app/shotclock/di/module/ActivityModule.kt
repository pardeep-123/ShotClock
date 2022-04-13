package com.app.shotclock.di.module


import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.activities.InitialActivity
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.videocallingactivity.CallConnectActivity
import com.app.shotclock.videocallingactivity.IncomingCallActivity
import com.app.shotclock.videocallingactivity.VideoCallActivity
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

    @get:ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract val homeActivity: HomeActivity

    @get:ContributesAndroidInjector
    internal abstract val incomingCallActivity: IncomingCallActivity

    @get:ContributesAndroidInjector
    internal abstract val videoCallActivity: VideoCallActivity

    @get:ContributesAndroidInjector
    internal abstract val callConnectActivity: CallConnectActivity

}