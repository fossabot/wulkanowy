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

    private var messages = listOf<IMessage>()

    private var senderId = 0

    override fun attachView(view: @NotNull MessagesContract.View, senderId: Int, senderName: String) {
        super.attachView(view)
        this.senderId = senderId

        view.setActivityTitle(senderName)

        loadMessages()
    }

    override fun loadMore(page: Int, totalItemsCount: Int) {
        view.showMessage("page: $page totalItemsCount: $totalItemsCount")
    }

    private fun loadMessages() {
        loadingTask = AbstractTask()
        loadingTask.setOnFirstLoadingListener(this)
        loadingTask.execute()
    }

    override fun onDoInBackgroundLoading() {
        messages = repository.dbRepo.getMessagesBySender(senderId).map {
            if (it.content == null) {
                return@map null
            }
            val subject = if (it.subject.isNotBlank()) "Temat: " + it.subject + "\n\n" else ""
            Message(
                    it.realId.toString(),
                    subject + it.content.trim(),
                    getDate(getDateAsTick(it.date, "yyyy-MM-dd HH:mm:ss")),
                    User(it.senderID.toString(), it.sender, it.sender)
            )
        }.filterNotNull()
    }

    override fun onCanceledLoadingAsync() {
    }

    override fun onEndLoadingAsync(success: Boolean, exception: Exception?) {
        if (success) {
            view.addToEnd(messages)
        } else {
            view.showMessage(exception!!.message as String)
            Timber.e(exception)
        }
    }
}
