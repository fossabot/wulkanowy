package io.github.wulkanowy.ui.main.messages

import io.github.wulkanowy.ui.base.BaseContract

interface MessagesContract {

    interface View : BaseContract.View {

        fun isMenuVisible(): Boolean

        fun setActivityTitle()

        fun setDialogs(dialogs: List<Dialog>)

        fun onRefresh()

        fun hideRefreshingBar()

        fun onRefreshSuccess()
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun onFragmentVisible(isVisible: Boolean)

        override fun attachView(view: View)

        fun onRefresh()
    }
}
