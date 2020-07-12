package com.runningapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

  /*  override fun onCreate() {
        here hilt will create all components that need to be injected and they will exist throughout app's life
        super.onCreate()
    }*/
}