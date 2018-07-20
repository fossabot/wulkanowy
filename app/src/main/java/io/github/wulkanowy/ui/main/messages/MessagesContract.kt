package io.github.wulkanowy.ui.main.messages

import com.stfalcon.chatkit.commons.models.IMessage
import io.github.wulkanowy.ui.base.BaseContract
import org.jetbrains.annotations.NotNull

interface MessagesContract {

    interface View : BaseContract.View {

        fun addToStart(message: IMessage)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun attachView(view: @NotNull View, senderId: Int)
    }
}
