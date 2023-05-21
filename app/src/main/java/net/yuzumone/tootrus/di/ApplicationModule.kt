package net.yuzumone.tootrus.di

import android.content.Context
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.MastodonClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.yuzumone.tootrus.App
import net.yuzumone.tootrus.data.mastodon.*
import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.data.prefs.SharedPreferenceStorage
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
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
    fun providesPreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Singleton
    @Provides
    @InterceptorMastodonClient
    fun providesMastodonClient(
        okHttpClientBuilder: OkHttpClient.Builder,
        gson: Gson,
        preferenceStorage: PreferenceStorage
    ): MastodonClient {
        return MastodonClient.Builder(
            instanceName = preferenceStorage.instanceName ?: "",
            okHttpClientBuilder = okHttpClientBuilder,
            gson = gson
        )
            .accessToken(preferenceStorage.accessToken ?: "").build()
    }

    @Singleton
    @Provides
    @InterceptorMastodonStreamingClient
    fun providesMastodonStreamingClient(
        okHttpClientBuilder: OkHttpClient.Builder,
        gson: Gson,
        preferenceStorage: PreferenceStorage
    ): MastodonClient {
        return MastodonClient.Builder(
            instanceName = preferenceStorage.instanceName ?: "",
            okHttpClientBuilder = okHttpClientBuilder,
            gson = gson
        )
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

    @Singleton
    @Provides
    fun providesNotificationRepository(repository: DefaultNotificationRepository): NotificationRepository =
        repository

    @Singleton
    @Provides
    fun providesPublicRepository(repository: DefaultPublicRepository): PublicRepository =
        repository

    @Singleton
    @Provides
    fun providesAccountRepository(repository: DefaultAccountRepository): AccountRepository =
        repository

    @Singleton
    @Provides
    fun providesMediaRepository(repository: DefaultMediaRepository): MediaRepository =
        repository

    @Singleton
    @Provides
    fun providesFavoriteRepository(repository: DefaultFavoriteRepository): FavoriteRepository =
        repository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InterceptorMastodonClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InterceptorMastodonStreamingClient