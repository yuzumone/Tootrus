package net.yuzumone.tootrus.di

import android.content.Context
import dagger.Module
import dagger.Provides
import net.yuzumone.tootrus.App
import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.data.prefs.SharedPreferenceStorage
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
            SharedPreferenceStorage(context)
}