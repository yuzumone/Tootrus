package net.yuzumone.tootrus.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import net.yuzumone.tootrus.ui.oauth.OAuthViewModel
import net.yuzumone.tootrus.ui.top.TopFragment
import net.yuzumone.tootrus.ui.top.TopViewModel
import net.yuzumone.tootrus.ui.top.local.LocalTimelineFragment
import net.yuzumone.tootrus.ui.top.local.LocalTimelineViewModel
import net.yuzumone.tootrus.ui.top.notification.NotificationFragment
import net.yuzumone.tootrus.ui.top.timeline.TimelineFragment

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesOAuthFragment(): OAuthFragment

    @ContributesAndroidInjector
    abstract fun contributesTopFragment(): TopFragment

    @ContributesAndroidInjector
    abstract fun contributesTimelineFragment(): TimelineFragment

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

    @Binds
    @IntoMap
    @ViewModelKey(LocalTimelineViewModel::class)
    abstract fun bindLocalTimelineViewModel(viewModel: LocalTimelineViewModel): ViewModel
}