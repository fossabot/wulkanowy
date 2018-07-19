package io.github.wulkanowy.ui.main.messages

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsList
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import io.github.wulkanowy.R
import io.github.wulkanowy.ui.base.BaseFragment
import javax.inject.Inject

class MessagesFragment : BaseFragment(), MessagesContract.View, DialogsListAdapter.OnDialogClickListener<Dialog>, DialogsListAdapter.OnDialogLongClickListener<Dialog>, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: MessagesContract.Presenter

    private lateinit var dialogsAdapter: DialogsListAdapter<Dialog>

    private val imageLoader = ImageLoader { imageView, url -> imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_android_black_24dp)) }

    @BindView(R.id.dialogsList)
    lateinit var dialogsList: DialogsList

    @BindView(R.id.messages_fragment_swipe_refresh)
    lateinit var refreshLayout: SwipeRefreshLayout

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

    override fun onDialogLongClick(dialog: Dialog?) {

    }

    override fun onDialogClick(dialog: Dialog?) {

    }
}
