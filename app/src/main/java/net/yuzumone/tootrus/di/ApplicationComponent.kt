package net.yuzumone.tootrus.di

import dagger.Component
import dagger.android.AndroidInjector
import net.yuzumone.tootrus.App
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ActivityModule::class,
        ServiceModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
