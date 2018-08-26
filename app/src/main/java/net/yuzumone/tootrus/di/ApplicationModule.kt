package net.yuzumone.tootrus.di

import android.content.Context
import dagger.Module
import dagger.Provides
import net.yuzumone.tootrus.App

@Module
class ApplicationModule {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

}