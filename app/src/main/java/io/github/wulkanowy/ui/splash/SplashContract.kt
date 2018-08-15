package io.github.wulkanowy.ui.splash

import io.github.wulkanowy.ui.base.BaseContract

interface SplashContract {

    interface View : BaseContract.View {

        fun openLoginActivity()

        fun openMainActivity()

        fun cancelNotifications()

        //void setCurrentThemeMode(int mode);
    }

    interface Presenter : BaseContract.Presenter<View>
}
