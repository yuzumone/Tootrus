package net.yuzumone.tootrus.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import net.yuzumone.tootrus.ui.oauth.OAuthViewModel
import net.yuzumone.tootrus.ui.top.TopFragment
import net.yuzumone.tootrus.ui.top.TopViewModel
import net.yuzumone.tootrus.ui.top.home.HomeTimelineFragment
import net.yuzumone.tootrus.ui.top.local.LocalTimelineFragment
import net.yuzumone.tootrus.ui.top.notification.NotificationFragment

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesOAuthFragment(): OAuthFragment

    @ContributesAndroidInjector
    abstract fun contributesTopFragment(): TopFragment

    @ContributesAndroidInjector
    abstract fun contributesTimelineFragment(): HomeTimelineFragment

    @ContributesAndroidInjector
    abstract fun contributesNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun contributesLocalTimelineFragment(): LocalTimelineFragment

    @Binds
    @IntoMap
    @ViewModelKey(OAuthViewModel::class)
    abstract fun bindOAuthViewModel(viewModel: OAuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopViewModel::class)
    abstract fun bindTopViewModel(viewModel: TopViewModel): ViewModel
}
