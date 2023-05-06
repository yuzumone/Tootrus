package net.yuzumone.tootrus.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.profile.*

@Module
abstract class ProfileActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributesProfileStatusesFragment(): ProfileStatusesFragment

    @ContributesAndroidInjector
    abstract fun contributesProfileMediaStatusesFragment(): ProfileMediaStatusesFragment

    @ContributesAndroidInjector
    abstract fun contributesProfileFollowingsFragment(): ProfileFollowingsFragment

    @ContributesAndroidInjector
    abstract fun contributesProfileFollowersFragment(): ProfileFollowersFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel
}
