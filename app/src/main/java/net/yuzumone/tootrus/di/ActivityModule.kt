package net.yuzumone.tootrus.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.yuzumone.tootrus.ui.*
import net.yuzumone.tootrus.ui.conversation.ConversationDialogFragment
import net.yuzumone.tootrus.ui.conversation.ConversationViewModel

@Module
abstract class ActivityModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [StatusDetailActivityModule::class])
    abstract fun contributeStatusDetailActivity(): StatusDetailActivity

    @ContributesAndroidInjector(modules = [ProfileActivityModule::class])
    abstract fun contributeProfileActivity(): ProfileActivity

    @ContributesAndroidInjector(modules = [FavoriteActivityModule::class])
    abstract fun contributeFavoriteActivity(): FavoriteActivity

    @ContributesAndroidInjector
    abstract fun contributesConversationDialogFragment(): ConversationDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindSettingViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConversationViewModel::class)
    abstract fun bindConversationViewModel(viewModel: ConversationViewModel): ViewModel
}