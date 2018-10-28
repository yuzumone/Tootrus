package net.yuzumone.tootrus.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.profile.ProfileFragment
import net.yuzumone.tootrus.ui.profile.ProfileViewModel

@Module
abstract class ProfileActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesProfileFragment(): ProfileFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel
}