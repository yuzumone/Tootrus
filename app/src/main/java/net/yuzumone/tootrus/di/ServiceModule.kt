package net.yuzumone.tootrus.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.yuzumone.tootrus.service.PostStatusService

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributesPostStatusService(): PostStatusService
}