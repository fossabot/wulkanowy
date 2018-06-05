package io.github.wulkanowy.ui.main.attendance;

import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.github.wulkanowy.R;
import io.github.wulkanowy.data.db.dao.entities.Subject;

class AttendanceSummaryHeader extends AbstractHeaderItem<AttendanceSummaryHeader.HeaderViewHolder> {

    private Subject subject;

    public AttendanceSummaryHeader(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AttendanceSummaryHeader that = (AttendanceSummaryHeader) o;

        return new EqualsBuilder()
                .append(subject, that.subject)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(subject)
                .toHashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.attendance_summary_header;
    }

    @Override
    public HeaderViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new HeaderViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, HeaderViewHolder holder, int position, List<Object> payloads) {
        holder.onBind(subject);
    }

    static class HeaderViewHolder extends FlexibleViewHolder {

        @BindView(R.id.attendance_summary_header_name)
        TextView name;

        HeaderViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        void onBind(Subject item) {
            name.setText(item.getName());
        }
    }
}
