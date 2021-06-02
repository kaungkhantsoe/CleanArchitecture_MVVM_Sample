package com.kks.myfirstcleanarchitectureapp.ui

import android.app.Application
import com.kks.myfirstcleanarchitectureapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}