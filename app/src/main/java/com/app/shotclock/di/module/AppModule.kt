package com.app.shotclock.di.module

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
class AppModule() {


    // --- REPOSITORY INJECTION ---
//    @Provides
//    @Singleton
//    fun provideSignUpRepo(apiService: ApiService): SignUpRepo {
//        return SignUpRepo(apiService)
//    }


    @Provides
    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    fun provideSharedPrefrence(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


//    @Provides
//    @Singleton
//    fun providerInterceptor(context: Context): AppInterceptor {
//        return AppInterceptor(context)
//    }

//    @Provides
//    @Singleton
//    internal fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient {
//
//        val okHttpClient = OkHttpClient.Builder()
//        okHttpClient.connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
//        okHttpClient.readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
//        okHttpClient.writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
//        val logInterceptor = HttpLoggingInterceptor()
//        logInterceptor.level = (HttpLoggingInterceptor.Level.BODY)
//        okHttpClient.addInterceptor(interceptor)
//        okHttpClient.addInterceptor(logInterceptor)
//        return okHttpClient.build()
//    }

//    @Singleton
//    @Provides
//    fun provideRetrofit(okHttpClient: OkHttpClient): ApiService {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(ApiConstants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
////            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .client(okHttpClient)
//            .build()
//
//        return retrofit.create(ApiService::class.java)
//
//    }

    @Singleton
    @Provides
    fun provideAudioManager(context: Context): AudioManager {

        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }


}