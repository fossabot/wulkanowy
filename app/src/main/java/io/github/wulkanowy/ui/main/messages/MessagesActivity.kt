package io.github.wulkanowy.ui.main.messages

import android.os.Bundle
import butterknife.BindView
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import io.github.wulkanowy.R
import io.github.wulkanowy.ui.base.BaseActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        injectViews()
        val senderId = intent.getIntExtra(SENDER_ID_KEY, 0)
        val senderName = intent.getStringExtra(SENDER_NAME_KEY)
        presenter.attachView(this, senderId, senderName)

        messagesAdapter = MessagesListAdapter("0", null)
        messagesAdapter.setLoadMoreListener(this)
        messagesList.setAdapter(messagesAdapter)
    }

    override fun setActivityTitle(senderName: String) {
        title = senderName
    }

    override fun setTotalMessages(total: Int) {
        messagesTotal = total
    }

    override fun addToEnd(message: List<IMessage>) {
        messagesAdapter.addToEnd(message, false)
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        if (page <= messagesTotal) {
            presenter.loadMore(page)
        }
    }
}
