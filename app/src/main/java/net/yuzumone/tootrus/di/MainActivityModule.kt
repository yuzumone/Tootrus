package net.yuzumone.tootrus.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import net.yuzumone.tootrus.ui.oauth.OAuthViewModel

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesOAuthFragment(): OAuthFragment

    @Binds
    @IntoMap
    @ViewModelKey(OAuthViewModel::class)
    abstract fun bindSettingViewModel(viewModel: OAuthViewModel): ViewModel
}