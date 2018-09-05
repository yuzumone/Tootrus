package net.yuzumone.tootrus.di

import android.content.Context
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.MastodonClient
import dagger.Module
import dagger.Provides
import net.yuzumone.tootrus.App
import net.yuzumone.tootrus.data.mastodon.*
import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.data.prefs.SharedPreferenceStorage
import okhttp3.OkHttpClient
import javax.inject.Named
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
    @Named("client")
    fun providesMastodonClient(
            okHttpClientBuilder: OkHttpClient.Builder,
            gson: Gson,
            preferenceStorage: PreferenceStorage): MastodonClient {
        return MastodonClient.Builder(
                instanceName = preferenceStorage.instanceName ?: "",
                okHttpClientBuilder = okHttpClientBuilder,
                gson = gson)
                .accessToken(preferenceStorage.accessToken ?: "").build()
    }

    @Singleton
    @Provides
    @Named("streaming_client")
    fun providesMastodonStreamingClient(
            okHttpClientBuilder: OkHttpClient.Builder,
            gson: Gson,
            preferenceStorage: PreferenceStorage): MastodonClient {
        return MastodonClient.Builder(
                instanceName = preferenceStorage.instanceName ?: "",
                okHttpClientBuilder = okHttpClientBuilder,
                gson = gson)
                .accessToken(preferenceStorage.accessToken ?: "")
                .useStreamingApi().build()
    }

    @Singleton
    @Provides
    fun providesOAuthRepository(repository: DefaultOAuthRepository): OAuthRepository =
            repository

    @Singleton
    @Provides
    fun providesTimelineRepository(repository: DefaultTimelineRepository): TimelineRepository =
            repository

    @Singleton
    @Provides
    fun providesStatusRepository(repository: DefaultStatusRepository): StatusRepository =
            repository

    @Singleton
    @Provides
    fun providesStreamRepository(repository: DefaultStreamRepository): StreamRepository =
            repository
}