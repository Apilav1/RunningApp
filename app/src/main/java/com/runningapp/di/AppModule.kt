package com.runningapp.di

import android.content.Context
import androidx.room.Room
import com.runningapp.db.RunningDatabase
import com.runningapp.other.Constants.RUNNING_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class) //we tell dagger to install this component inside ApplicationComponent class (Base Application)
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
}