package net.yuzumone.tootrus.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.detail.StatusDetailFragment
import net.yuzumone.tootrus.ui.detail.StatusDetailViewModel

@Module
abstract class StatusDetailActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesStatusDetailFragment(): StatusDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(StatusDetailViewModel::class)
    abstract fun bindStatusDetailViewModel(viewModel: StatusDetailViewModel): ViewModel
}
