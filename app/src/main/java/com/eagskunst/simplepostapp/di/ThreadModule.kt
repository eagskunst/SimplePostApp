package com.eagskunst.simplepostapp.di

import com.eagskunst.simplepostapp.domain.BackgroundScheduler
import com.eagskunst.simplepostapp.domain.MainScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@Module
@InstallIn(SingletonComponent::class)
class ThreadModule {

    @Provides
    fun provideBackgroundScheduler() = BackgroundScheduler {
        Schedulers.io()
    }

    @Provides
    fun provideMainScheduler() = MainScheduler {
        AndroidSchedulers.mainThread()
    }
}
