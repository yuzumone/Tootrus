package net.yuzumone.tootrus.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.PostStatusViewModel
import net.yuzumone.tootrus.ui.poststatus.PostStatusDialogFragment

@Module
abstract class PostStatusActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesPostStatusDialogFragment(): PostStatusDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostStatusViewModel::class)
    abstract fun bindPostStatusViewModel(viewModel: PostStatusViewModel): ViewModel
}