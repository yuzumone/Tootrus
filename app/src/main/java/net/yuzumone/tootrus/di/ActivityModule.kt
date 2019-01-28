package net.yuzumone.tootrus.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.MainActivity
import net.yuzumone.tootrus.ui.MainViewModel
import net.yuzumone.tootrus.ui.ProfileActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity

@Module
abstract class ActivityModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [StatusDetailActivityModule::class])
    abstract fun contributeStatusDetailActivity(): StatusDetailActivity

    @ContributesAndroidInjector(modules = [ProfileActivityModule::class])
    abstract fun contributeProfileActivity(): ProfileActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindSettingViewModel(viewModel: MainViewModel): ViewModel
}