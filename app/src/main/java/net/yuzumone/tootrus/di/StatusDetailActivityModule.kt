package net.yuzumone.tootrus.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.detail.StatusDetailViewModel

@Module
abstract class StatusDetailActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(StatusDetailViewModel::class)
    abstract fun bindStatusDetailViewModel(viewModel: StatusDetailViewModel): ViewModel
}