package io.github.wulkanowy.ui.login

import dagger.Binds
import dagger.Module
import io.github.wulkanowy.di.scopes.PerActivity

@Module
internal abstract class LoginModule {

    @PerActivity
    @Binds
    abstract fun provideLoginPresenter(loginPresenter: LoginPresenter): LoginContract.Presenter
}
