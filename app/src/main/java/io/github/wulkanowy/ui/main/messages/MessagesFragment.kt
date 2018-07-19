package io.github.wulkanowy.ui.main.messages

import android.os.Bundle
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

class MessagesFragment : BaseFragment(), MessagesContract.View, DialogsListAdapter.OnDialogClickListener<Dialog>, DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    @Inject
    lateinit var presenter: MessagesContract.Presenter

    private lateinit var dialogsAdapter: DialogsListAdapter<Dialog>

    private val imageLoader = ImageLoader { imageView, url -> imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_android_black_24dp)) }

    @BindView(R.id.dialogsList)
    lateinit var dialogsList: DialogsList

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onDialogLongClick(dialog: Dialog?) {

    }

    override fun onDialogClick(dialog: Dialog?) {

    }
}
