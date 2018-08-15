package io.github.wulkanowy.ui.splash

import dagger.Binds
import dagger.Module
import io.github.wulkanowy.di.scopes.PerActivity

@Module
internal abstract class SplashModule {

    @PerActivity
    @Binds
    abstract fun provideSplashPresenter(splashPresenter: SplashPresenter): SplashContract.Presenter
}
