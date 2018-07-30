package io.github.wulkanowy.ui.main.attendance

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.github.wulkanowy.R

class AttendanceSummarySubItem(header: AttendanceSummaryHeader, val type: Pair<String, Int>) : AbstractSectionableItem<AttendanceSummarySubItem.SubItemViewHolder, AttendanceSummaryHeader>(header) {

    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun getLayoutRes(): Int {
        return R.layout.attendance_summary_subitem
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<*>>): SubItemViewHolder {
        return SubItemViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<*>>, holder: SubItemViewHolder, position: Int, payloads: List<Any>) {
        holder.onBind(type)
    }

    class SubItemViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        @BindView(R.id.attendance_summary_subitem_name)
        lateinit var name: TextView

        @BindView(R.id.attendance_summary_subitem_value)
        lateinit var value: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun onBind(type: Pair<String, Int>) {
            name.text = type.first
            value.text = type.second.toString()
        }
    }
}
