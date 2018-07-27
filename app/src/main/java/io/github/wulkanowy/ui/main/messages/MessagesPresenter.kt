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
import io.github.wulkanowy.data.db.dao.entities.Message as MessageEntity

class MessagesPresenter @Inject constructor(repo: RepositoryContract) : BasePresenter<MessagesContract.View>(repo),
        MessagesContract.Presenter, AsyncListeners.OnFirstLoadingListener, AsyncListeners.OnRefreshListener {

    private lateinit var loadingTask: AbstractTask

    private lateinit var refreshTask: AbstractTask

    private var messages = listOf<IMessage>()

    private var total = 0

    private var senderId = 0

    private var pageStart = 0

    private var pageLimit = 1

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
        val allMessages = repository.dbRepo.getMessagesBySender(senderId).map {
            if (it.content == null) {
                return@map null
            }
            getMappedMessage(it)
        }

        if (allMessages.filterNotNull().isEmpty()) { //
            repository.syncRepo.syncAllFirstMessagesFromSenders()
        }

        total = allMessages.size

        messages = allMessages.filterNotNull()
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
            Timber.d("loaded $total")
        }
    }

    // load more

    override fun loadMore(page: Int, totalItemsCount: Int) {
        pageStart = page
        pageLimit = totalItemsCount
        refreshTask = AbstractTask()
        refreshTask.setOnRefreshListener(this)
        refreshTask.execute()
    }

    override fun onDoInBackgroundRefresh() {
        val allMessages = repository.dbRepo.getMessagesBySender(senderId, pageStart, pageLimit).map {
            getMappedMessage(if (it.content == null) {
                repository.syncRepo.syncMessageById(it.realId)
                repository.dbRepo.getMessageById(it.realId)
            } else it)
        }

        total += allMessages.size

        messages = allMessages
    }

    override fun onCanceledRefreshAsync() {
    }

    override fun onEndRefreshAsync(success: Boolean, exception: Exception?) {
        if (success) {
            view.addToEnd(messages)
            view.setTotalMessages(total)
            total -= 1
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
                User(e.senderID.toString(), e.sender, e.sender)
        )
    }
}
