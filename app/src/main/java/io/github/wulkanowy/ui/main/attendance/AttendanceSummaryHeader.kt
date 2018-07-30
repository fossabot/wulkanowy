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
import io.github.wulkanowy.utils.getMonthNameByNumber
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.util.*

class AttendanceSummaryHeader(private val month: Int, private val total: Double) : AbstractHeaderItem<AttendanceSummaryHeader.HeaderViewHolder>() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || javaClass != other.javaClass) return false

        val that = other as AttendanceSummaryHeader?

        return EqualsBuilder()
                .append(month, that!!.month)
                .append(total, that.total)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(month)
                .toHashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.attendance_summary_header
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<*>>): HeaderViewHolder {
        return HeaderViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<*>>, holder: HeaderViewHolder, position: Int, payloads: List<Any>) {
        holder.onBind(month, total)
    }

    class HeaderViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        @BindView(R.id.attendance_summary_header_name)
        lateinit var name: TextView

        @BindView(R.id.attendance_summary_header_total)
        lateinit var total: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun onBind(headerTitle: Int, totalNumber: Double) {
            name.text = getMonthNameByNumber(headerTitle)
            total.text = String.format(Locale.getDefault(), "%.2f%s", totalNumber, "%")
        }
    }
}
