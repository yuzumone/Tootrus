package net.yuzumone.tootrus.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import net.yuzumone.tootrus.App
import net.yuzumone.tootrus.data.mastodon.DefaultOAuthRepository
import net.yuzumone.tootrus.data.mastodon.OAuthRepository
import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.data.prefs.SharedPreferenceStorage
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesOkHttpClientBuilder(): OkHttpClient.Builder =
            OkHttpClient.Builder()

    @Singleton
    @Provides
    fun providesGson(): Gson =
            Gson()

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
            SharedPreferenceStorage(context)

    @Singleton
    @Provides
    fun providesOAuthRepository(repository: DefaultOAuthRepository): OAuthRepository =
            repository
}