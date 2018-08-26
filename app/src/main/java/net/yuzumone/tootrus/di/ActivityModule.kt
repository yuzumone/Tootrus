package net.yuzumone.tootrus.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.yuzumone.tootrus.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}