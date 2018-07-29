package io.github.wulkanowy.ui.main.attendance

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.github.wulkanowy.R
import io.github.wulkanowy.data.db.dao.entities.AttendanceSubject
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

class AttendanceSummaryHeader(private val name: String) : AbstractHeaderItem<AttendanceSummaryHeader.HeaderViewHolder>() {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true

        if (o == null || javaClass != o.javaClass) return false

        val that = o as AttendanceSummaryHeader?

        return EqualsBuilder()
                .append(name, that!!.name)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.attendance_summary_header
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<*>>): HeaderViewHolder {
        return HeaderViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<*>>, holder: HeaderViewHolder, position: Int, payloads: List<Any>) {
        holder.onBind(name)
    }

    class HeaderViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        @BindView(R.id.attendance_summary_header_name)
        lateinit var name: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun onBind(headerTitle: String) {
            name.text = headerTitle
        }
    }
}
