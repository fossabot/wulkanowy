package io.github.wulkanowy.ui.main.messages

import io.github.wulkanowy.api.getDate
import io.github.wulkanowy.api.getDateAsTick
import io.github.wulkanowy.data.RepositoryContract
import io.github.wulkanowy.ui.base.BasePresenter
import io.github.wulkanowy.utils.async.AbstractTask
import io.github.wulkanowy.utils.async.AsyncListeners
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MessagesPresenter @Inject constructor(repo: RepositoryContract) : BasePresenter<MessagesContract.View>(repo), MessagesContract.Presenter,
        AsyncListeners.OnFirstLoadingListener {

    private lateinit var loadingTask: AbstractTask

    private lateinit var dialogs: List<Dialog>

    private var isFirstSight: Boolean = false

    override fun onDoInBackgroundLoading() {
//        repository.syncRepo.syncMessages()
        dialogs = repository.dbRepo.messages.map {
            val users = arrayListOf<User>()
            users.add(User(it.senderID.toString(), it.sender, it.sender))
            val messages = Message(it.realId.toString(), it.content, getDate(getDateAsTick(it.date, "yyyy-MM-dd HH:mm:ss")), users[0])

            Dialog(it.realId.toString(), it.sender ?: "", it.sender ?: "", users, messages, 1)
        }
    }

    override fun onCanceledLoadingAsync() {
    }

    override fun onEndLoadingAsync(success: Boolean, exception: Exception?) {
        if (success) {
            view.setDialogs(dialogs)
        } else {
            view.showMessage(exception!!.message as String)
            Timber.e(exception)
        }
    }

    override fun attachView(view: MessagesContract.View) {
        super.attachView(view)

        if (getView().isMenuVisible()) {
            getView().setActivityTitle()
        }

        if (!isFirstSight) {
            isFirstSight = true
        }

        loadMessages()
    }

    override fun onFragmentVisible(isVisible: Boolean) {
        if (isVisible) {
            view.setActivityTitle()
        }
    }

    private fun loadMessages() {
        loadingTask = AbstractTask()
        loadingTask.setOnFirstLoadingListener(this)
        loadingTask.execute()
    }

    override fun detachView() {
        isFirstSight = false
        super.detachView()
    }
}
