package io.github.wulkanowy.ui.main.messages

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

class DialogsPresenter @Inject constructor(repo: RepositoryContract) : BasePresenter<DialogsContract.View>(repo),
        DialogsContract.Presenter, AsyncListeners.OnFirstLoadingListener, AsyncListeners.OnRefreshListener {

    private lateinit var loadingTask: AbstractTask

    private lateinit var refreshTask: AbstractTask

    private lateinit var dialogs: List<Dialog>

    private var isFirstSight: Boolean = false

    override fun attachView(view: @NotNull DialogsContract.View) {
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

    // loading

    override fun onDoInBackgroundLoading() {
        val messagesList = repository.dbRepo.messages

        if (messagesList.isEmpty()) return

        messagesList.sortByDescending { it.date }
        dialogs = messagesList.groupBy { it.senderID }.map {
            val messages = it.value.map {
                val user = User(it.senderID.toString(), it.sender, null)
                Message(it.realId.toString(), it.subject, getDate(getDateAsTick(it.date, "yyyy-MM-dd HH:mm:ss")), user)
            }
            Dialog(it.key.toString(), null, it.value.first().sender, arrayListOf(messages.first().user), messages.first(), 0)
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

    // refresh

    override fun onRefresh() {
        if (view.isNetworkConnected) {
            refreshTask = AbstractTask()
            refreshTask.setOnRefreshListener(this)
            refreshTask.execute()
        } else {
            view.showNoNetworkMessage()
            view.hideRefreshingBar()
        }
    }

    override fun onDoInBackgroundRefresh() {
        repository.syncRepo.syncMessages()
    }

    override fun onCanceledRefreshAsync() {
        if (isViewAttached) {
            view.hideRefreshingBar()
        }
    }

    override fun onEndRefreshAsync(result: Boolean, exception: Exception?) {
        if (result) {
            loadingTask = AbstractTask()
            loadingTask.setOnFirstLoadingListener(this)
            loadingTask.execute()

            view.onRefreshSuccess()
        } else {
            view.showMessage(repository.resRepo.getErrorLoginMessage(exception))
        }
        view.hideRefreshingBar()
    }
}
