package com.app.shotclock.di.module

import com.app.shotclock.fragments.*
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

    @ContributesAndroidInjector
    internal abstract fun loginPhoneFragment(): LoginPhoneFragment

    @ContributesAndroidInjector
    internal abstract fun forgotPassFragment(): ForgotPasswordFragment

    @ContributesAndroidInjector
    internal abstract fun otpFragment(): OtpFragment

    @ContributesAndroidInjector
    internal abstract fun signUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    internal abstract fun completeProfileFragment(): CompleteProfileFragment

    @ContributesAndroidInjector
    internal abstract fun homeFragment(): HomeFragment

    @ContributesAndroidInjector
    internal abstract fun profileFragment(): ProfileFragment

}