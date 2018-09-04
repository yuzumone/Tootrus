package net.yuzumone.tootrus.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.MainActivity
import net.yuzumone.tootrus.ui.MainViewModel
import net.yuzumone.tootrus.ui.PostStatusActivity

@Module
abstract class ActivityModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [PostStatusActivityModule::class])
    abstract fun contributePostStatusActivity(): PostStatusActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindSettingViewModel(viewModel: MainViewModel): ViewModel
}