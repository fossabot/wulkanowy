package io.github.wulkanowy.ui.main.messages

import com.stfalcon.chatkit.commons.models.IMessage
import io.github.wulkanowy.ui.base.BaseContract
import org.jetbrains.annotations.NotNull

interface MessagesContract {

    interface View : BaseContract.View {

        fun addToEnd(message: List<IMessage>)

        fun setActivityTitle(senderName: String)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun attachView(view: @NotNull View, senderId: Int, senderName: String)

        fun loadMore(page: Int, totalItemsCount: Int)
    }
}
