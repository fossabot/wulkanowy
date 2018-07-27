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
        dialogs = repository.dbRepo.messages.map {
            val users = arrayListOf<User>()
            users.add(User(it.senderID.toString(), it.sender, it.sender))
            val messages = Message(it.realId.toString(), it.content ?: it.subject, getDate(getDateAsTick(it.date, "yyyy-MM-dd HH:mm:ss")), users[0])

            Dialog(it.realId.toString(), it.sender ?: "J A", it.sender ?: "JA", users, messages, 1)
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
