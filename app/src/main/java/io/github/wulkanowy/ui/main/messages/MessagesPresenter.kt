package io.github.wulkanowy.ui.main.messages

import com.stfalcon.chatkit.commons.models.IMessage
import io.github.wulkanowy.api.getDate
import io.github.wulkanowy.api.getDateAsTick
import io.github.wulkanowy.data.RepositoryContract
import io.github.wulkanowy.ui.base.BasePresenter
import io.github.wulkanowy.utils.async.AbstractTask
import io.github.wulkanowy.utils.async.AsyncListeners
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MessagesPresenter @Inject constructor(repo: RepositoryContract) : BasePresenter<MessagesContract.View>(repo),
        MessagesContract.Presenter, AsyncListeners.OnFirstLoadingListener {

    private lateinit var loadingTask: AbstractTask

    private lateinit var message: IMessage

    private var senderId = 0

    override fun attachView(view: @NotNull MessagesContract.View, senderId: Int) {
        super.attachView(view)
        this.senderId = senderId

        loadMessages()
    }

    private fun loadMessages() {
        loadingTask = AbstractTask()
        loadingTask.setOnFirstLoadingListener(this)
        loadingTask.execute()
    }

    override fun onDoInBackgroundLoading() {
        repository.syncRepo.syncMessageBySender(senderId)
        val mes = repository.dbRepo.getMessagesBySender(senderId)[0]

        val user = User(mes.senderID.toString(), mes.sender, mes.sender)

        message = Message(
                mes.realId.toString(),
                mes.content ?: "NPE",
                getDate(getDateAsTick(mes.date, "yyyy-MM-dd HH:mm:ss")),
                user
        )
    }

    override fun onCanceledLoadingAsync() {
    }

    override fun onEndLoadingAsync(success: Boolean, exception: Exception?) {
        if (success) {
            view.addToStart(message)
        } else {
            view.showMessage(exception!!.message as String)
            Timber.e(exception)
        }
    }
}
