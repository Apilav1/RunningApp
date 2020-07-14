package com.runningapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication: Application() {

   override fun onCreate() {
        super.onCreate()
       //here hilt will create all components that need to be injected and they will exist throughout app's life

       //for logs
       Timber.plant(Timber.DebugTree())
   }
}