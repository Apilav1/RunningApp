package com.runningapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.runningapp.db.RunningDatabase
import com.runningapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.runningapp.other.Constants.KEY_NAME
import com.runningapp.other.Constants.KEY_WEIGHT
import com.runningapp.other.Constants.RUNNING_DATABASE_NAME
import com.runningapp.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class) //we tell dagger to install this component inside ApplicationComponent class (Base Application) and also we tell him this way how long this will live
object AppModule {

    //manual to hilt on how to create running db
    @Singleton //to provide just one unique instance of db
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context //it inserts application context for this app object
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    //because we need DAO to interact with db and not running db instance
    //also we don't need to pass db argument to this class since dagger now knows how to create RunningDatabase instance
    //and the dagger will call these functions by itself in classes that dagger generates for us (java(generated))
    @Singleton
    @Provides
    fun provideRunDao(db: RunningDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(KEY_NAME, "") ?: "" //strange behavior on kotlin

    @Singleton
    @Provides
    fun provideWeight(sharedPreferences: SharedPreferences) =
        sharedPreferences.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPreferences: SharedPreferences) =
        sharedPreferences.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}