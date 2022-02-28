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

    @ContributesAndroidInjector
    internal abstract fun editProfileFragment(): EditProfileFragment

    @ContributesAndroidInjector
    internal abstract fun myRequestFragment(): MyRequestsFragment

    @ContributesAndroidInjector
    internal abstract fun notificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    internal abstract fun messageFragment(): MessageFragment

    @ContributesAndroidInjector
    internal abstract fun chatFragment(): ChatFragment

    @ContributesAndroidInjector
    internal abstract fun changePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector
    internal abstract fun cookiePolicyFragment(): CookiePolicyFragment

    @ContributesAndroidInjector
    internal abstract fun copyrightPolicyFragment(): CopyrightPolicyFragment

    @ContributesAndroidInjector
    internal abstract fun privacyPolicyFragment(): PrivacyPolicyFragment

    @ContributesAndroidInjector
    internal abstract fun safeDatingPolicy(): SafeDatingPolicyFragment

    @ContributesAndroidInjector
    internal abstract fun termsConditionsFragment(): TermsConditionsFragment

    @ContributesAndroidInjector
    internal abstract fun subscriptionFragment(): SubscriptionFragment

    @ContributesAndroidInjector
    internal abstract fun videoCallFragment(): VideoCallFragment

    @ContributesAndroidInjector
    internal abstract fun iceBreakerQuestionsFragment() : IcebreakerQuestionsFragment

    @ContributesAndroidInjector
    internal abstract fun speedDateSession(): SpeedDateSessionFragment

}