package io.github.wulkanowy.ui.main.attendance;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.wulkanowy.ui.base.BaseContract;
import io.github.wulkanowy.ui.main.OnFragmentIsReadyListener;
import io.github.wulkanowy.ui.main.grades.GradesSummarySubItem;

public interface AttendanceContract {

    interface View extends BaseContract.View {

        void setActivityTitle();

        void scrollViewPagerToPosition(int position);

        void updateSummaryAdapterList(List<AttendanceSummarySubItem> summarySubItems);

        void setTabDataToAdapter(String date);

        void setAdapterWithTabLayout();

        boolean isMenuVisible();

        void setThemeForTab(int position);
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void onFragmentActivated(boolean isVisible);

        void attachView(@NonNull View view, OnFragmentIsReadyListener listener);

        void setRestoredPosition(int position);
    }
}
