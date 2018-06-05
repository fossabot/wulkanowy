package io.github.wulkanowy.ui.main.attendance;

import android.view.View;

import java.util.List;

import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.github.wulkanowy.R;

public class AttendanceSummarySubItem
        extends AbstractSectionableItem<AttendanceSummarySubItem.SubItemViewHolder, AttendanceSummaryHeader> {

    public AttendanceSummarySubItem(AttendanceSummaryHeader header) {
        super(header);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.attendance_summary_subitem;
    }

    @Override
    public SubItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new SubItemViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, SubItemViewHolder holder, int position, List<Object> payloads) {
        holder.onBind();
    }

    static class SubItemViewHolder extends FlexibleViewHolder {

        public SubItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        void onBind() {

        }
    }
}
