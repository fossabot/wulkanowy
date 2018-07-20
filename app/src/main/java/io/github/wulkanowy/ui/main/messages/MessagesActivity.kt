package io.github.wulkanowy.ui.main.messages

import android.os.Bundle
import butterknife.BindView
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import io.github.wulkanowy.R
import io.github.wulkanowy.ui.base.BaseActivity
import javax.inject.Inject

class MessagesActivity : BaseActivity(), MessagesContract.View {

    companion object {
        const val SENDER_ID_KEY = "SENDER_ID_KEY"
    }

    @Inject
    lateinit var presenter: MessagesContract.Presenter

    private lateinit var messagesAdapter: MessagesListAdapter<IMessage>

    @BindView(R.id.messages_list)
    lateinit var messagesList: MessagesList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        injectViews()
        presenter.attachView(this, intent.getIntExtra(SENDER_ID_KEY, 0))

        messagesAdapter = MessagesListAdapter("0", null)
        messagesList.setAdapter(messagesAdapter)
    }

    override fun addToStart(message: IMessage) {
        messagesAdapter.addToStart(message, false)
    }

}
