package io.github.wulkanowy.ui.main.messages

import android.graphics.Color
import android.os.Bundle
import butterknife.BindView
import com.amulyakhare.textdrawable.TextDrawable
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import io.github.wulkanowy.R
import io.github.wulkanowy.ui.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class MessagesActivity : BaseActivity(), MessagesContract.View, MessagesListAdapter.OnLoadMoreListener {

    companion object {
        const val SENDER_ID_KEY = "SENDER_ID_KEY"
        const val SENDER_NAME_KEY = "SENDER_NAME_KEY"
    }

    @Inject
    lateinit var presenter: MessagesContract.Presenter

    private lateinit var messagesAdapter: MessagesListAdapter<IMessage>

    private var messagesTotal = 0

    @BindView(R.id.messages_list)
    lateinit var messagesList: MessagesList

    private val imageLoader = ImageLoader { imageView, name ->
        val letters = name?.split(" ")?.map { it.substring(0, 1) }
        imageView.setImageDrawable(TextDrawable.builder().buildRect(letters?.getOrElse(0) {"J"} + letters?.getOrElse(1) {"A"}, Color.DKGRAY))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        injectViews()
        val senderId = intent.getIntExtra(SENDER_ID_KEY, 0)
        val senderName = intent.getStringExtra(SENDER_NAME_KEY)
        presenter.attachView(this, senderId, senderName)

        messagesAdapter = MessagesListAdapter("0", imageLoader)
        messagesAdapter.setLoadMoreListener(this)
        messagesList.setAdapter(messagesAdapter)
    }

    override fun setActivityTitle(senderName: String) {
        title = senderName
    }

    override fun setTotalMessages(total: Int) {
        messagesTotal = total
        Timber.d("Total messages: $total")
    }

    override fun addToEnd(message: List<IMessage>) {
        messagesAdapter.addToEnd(message, false)
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        Timber.i("onLoadMore: page: $page itemsCount: $totalItemsCount total: $messagesTotal")
        if (totalItemsCount <= messagesTotal) {
            presenter.loadMore(totalItemsCount)
        }
    }
}
