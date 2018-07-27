package io.github.wulkanowy.ui.main.messages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.amulyakhare.textdrawable.TextDrawable
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsList
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.stfalcon.chatkit.utils.DateFormatter
import io.github.wulkanowy.R
import io.github.wulkanowy.ui.base.BaseFragment
import java.util.*
import javax.inject.Inject

class DialogsFragment : BaseFragment(), DialogsContract.View, DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog>, SwipeRefreshLayout.OnRefreshListener, DateFormatter.Formatter {

    @Inject
    lateinit var presenter: DialogsContract.Presenter

    private lateinit var dialogsAdapter: DialogsListAdapter<Dialog>

    @BindView(R.id.dialogsList)
    lateinit var dialogsList: DialogsList

    @BindView(R.id.messages_fragment_swipe_refresh)
    lateinit var refreshLayout: SwipeRefreshLayout

    private val imageLoader = ImageLoader { imageView, name ->
        val letters = name?.split(" ")?.map { it.substring(0, 1) }
        imageView.setImageDrawable(TextDrawable.builder().buildRect(letters?.getOrElse(0) {"J"} + letters?.getOrElse(1) {"A"}, Color.DKGRAY))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        injectViews(view)

        presenter.attachView(this)

        return view
    }

    override fun setActivityTitle() {
        setTitle(getString(R.string.messages_text))
    }

    override fun setDialogs(dialogs: List<Dialog>) {
        dialogsAdapter.setItems(dialogs)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (::presenter.isInitialized) {
            presenter.onFragmentVisible(menuVisible)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialogsAdapter = DialogsListAdapter(imageLoader)

        dialogsAdapter.setOnDialogClickListener(this)
        dialogsAdapter.setOnDialogLongClickListener(this)

        dialogsList.setAdapter(dialogsAdapter)
        dialogsAdapter.setDatesFormatter(this)

        refreshLayout.setColorSchemeResources(android.R.color.black)
        refreshLayout.setOnRefreshListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onRefresh() {
        presenter.onRefresh()
    }

    override fun hideRefreshingBar() {
        refreshLayout.isRefreshing = false
    }

    override fun onRefreshSuccess() {
        showMessage(R.string.sync_completed)
    }

    override fun format(date: Date?): String {
        return when {
            DateFormatter.isToday(date) -> DateFormatter.format(date, DateFormatter.Template.TIME)
            DateFormatter.isYesterday(date) -> getString(R.string.date_header_yesterday)
            else -> DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR)
        }
    }

    override fun onDialogLongClick(dialog: Dialog?) {

    }

    override fun onDialogClick(dialog: Dialog?) {
        context!!.startActivity(Intent(context, MessagesActivity::class.java)
                .putExtra(MessagesActivity.SENDER_ID_KEY, dialog!!.users[0].id.toInt())
                .putExtra(MessagesActivity.SENDER_NAME_KEY, dialog.dialogName)
        )
    }
}
