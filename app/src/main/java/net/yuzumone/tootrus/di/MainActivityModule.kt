package net.yuzumone.tootrus.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import net.yuzumone.tootrus.ui.oauth.OAuthViewModel
import net.yuzumone.tootrus.ui.top.timeline.TimelineFragment
import net.yuzumone.tootrus.ui.top.timeline.TimelineViewModel

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesOAuthFragment(): OAuthFragment

    @ContributesAndroidInjector
    abstract fun contributesTimelineFragment(): TimelineFragment

    @Binds
    @IntoMap
    @ViewModelKey(OAuthViewModel::class)
    abstract fun bindOAuthViewModel(viewModel: OAuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel::class)
    abstract fun bindTimelineViewModel(viewModel: TimelineViewModel): ViewModel
}