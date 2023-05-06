package net.yuzumone.tootrus.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.favorite.FavoriteFragment
import net.yuzumone.tootrus.ui.favorite.FavoriteViewModel

@Module
abstract class FavoriteActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesFavoriteFragment(): FavoriteFragment

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel
}
