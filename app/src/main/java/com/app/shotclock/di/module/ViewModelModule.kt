package com.app.shotclock.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.shotclock.di.key.ViewModelKey
import com.app.shotclock.di.viewmodelprovider.ViewModelFactory
import com.app.shotclock.viewmodels.HomeViewModel
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.app.shotclock.viewmodels.ProfileViewModels
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
    /*
     * This method basically says
     * inject this object into a Map using the @IntoMap annotation,
     * with the  MovieListViewModel.class as key,
     * and a Provider that will build a MovieListViewModel
     * object.
     *
     * */

    @Binds
    @IntoMap
    @ViewModelKey(LoginSignUpViewModel::class)
    protected abstract fun loginSignUpViewModel(loginSignUpViewModel: LoginSignUpViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModels::class)
    protected abstract fun profileViewModels(profileViewModels: ProfileViewModels): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    protected abstract fun homeViewModel(homeViewModel: HomeViewModel): ViewModel
}