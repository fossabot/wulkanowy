package io.github.wulkanowy.ui.main.messages

import com.stfalcon.chatkit.commons.models.IMessage
import io.github.wulkanowy.api.getDate
import io.github.wulkanowy.api.getDateAsTick
import io.github.wulkanowy.api.messages.Messages
import io.github.wulkanowy.data.RepositoryContract
import io.github.wulkanowy.ui.base.BasePresenter
import io.github.wulkanowy.utils.async.AbstractTask
import io.github.wulkanowy.utils.async.AsyncListeners
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import io.github.wulkanowy.data.db.dao.entities.Message as MessageEntity

class MessagesPresenter @Inject constructor(repo: RepositoryContract) : BasePresenter<MessagesContract.View>(repo),
        MessagesContract.Presenter, AsyncListeners.OnFirstLoadingListener, AsyncListeners.OnRefreshListener {

    private lateinit var loadingTask: AbstractTask

    private lateinit var refreshTask: AbstractTask

    private var messages = listOf<IMessage>()

    private var total = 0

    private var senderId = 0

    private var start = 0

    override fun attachView(view: @NotNull MessagesContract.View, senderId: Int, senderName: String) {
        super.attachView(view)
        this.senderId = senderId

        view.setActivityTitle(senderName)

        loadMessages()
    }

    // first load

    private fun loadMessages() {
        loadingTask = AbstractTask()
        loadingTask.setOnFirstLoadingListener(this)
        loadingTask.execute()
    }

    override fun onDoInBackgroundLoading() {
        messages = repository.dbRepo.getMessagesBySender(senderId).map {
            total++
            if (it.content == null) {
                return@map null
            }
            getMappedMessage(it)
        }.filterNotNull().take(1)
    }

    override fun onCanceledLoadingAsync() {
    }

    override fun onEndLoadingAsync(success: Boolean, exception: Exception?) {
        if (success) {
            view.addToEnd(messages)
            view.setTotalMessages(total)
        } else {
            view.showMessage(exception!!.message as String)
            Timber.e(exception)
        }
    }

    // load more

    override fun loadMore(totalItemsCount: Int) {
        start = if (totalItemsCount < 6) 1 else totalItemsCount
        refreshTask = AbstractTask()
        refreshTask.setOnRefreshListener(this)
        refreshTask.execute()
    }

    override fun onDoInBackgroundRefresh() {
        messages = repository.dbRepo.getMessagesBySender(senderId, start, 3).map {
            getMappedMessage(if (it.content == null) {
                repository.syncRepo.syncMessageById(it.messageID)
                repository.dbRepo.getMessageById(it.messageID)
            } else it)
        }
    }

    override fun onCanceledRefreshAsync() {
    }

    override fun onEndRefreshAsync(success: Boolean, exception: Exception?) {
        if (success) {
            view.addToEnd(messages)
        } else {
            view.showMessage(exception!!.message as String)
            Timber.e(exception)
        }
    }

    private fun getMappedMessage(e: MessageEntity): Message {
        val subject = if (e.subject.isNotBlank()) "Temat: " + e.subject + "\n\n" else ""
        return Message(
                e.realId.toString(),
                subject + e.content.trim(),
                getDate(getDateAsTick(e.date, "yyyy-MM-dd HH:mm:ss")),
                User(if (e.folderId != Messages.SENT_FOLDER) e.senderID.toString() else "0", e.sender, e.sender)
        )
    }
}
