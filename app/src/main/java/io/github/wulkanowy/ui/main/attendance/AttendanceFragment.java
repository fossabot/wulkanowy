package io.github.wulkanowy.ui.main.attendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import io.github.wulkanowy.R;
import io.github.wulkanowy.ui.base.BaseFragment;
import io.github.wulkanowy.ui.base.BasePagerAdapter;
import io.github.wulkanowy.ui.main.OnFragmentIsReadyListener;
import io.github.wulkanowy.ui.main.attendance.tab.AttendanceTabFragment;

public class AttendanceFragment extends BaseFragment implements AttendanceContract.View, AdapterView.OnItemSelectedListener {

    private static final String CURRENT_ITEM_KEY = "CurrentItem";

    @BindView(R.id.attendance_fragment_viewpager)
    ViewPager viewPager;

    @BindView(R.id.attendance_fragment_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.attendance_fragment_details_container)
    View details;

    @BindView(R.id.attendance_fragment_summary_container)
    View summary;

    @Inject
    @Named("Attendance")
    BasePagerAdapter pagerAdapter;

    @Inject
    FlexibleAdapter<AttendanceSummarySubItem> summaryAdapter;

    @BindView(R.id.attendance_fragment_summary_recycler)
    RecyclerView summaryRecyclerView;

    @BindView(R.id.attendance_fragment_summary_value)
    TextView totalAttendance;

    @BindView(R.id.attendance_fragment_subject_chooser)
    AppCompatSpinner subjectChooser;

    ArrayAdapter<String> subjectsAdapter;

    @Inject
    AttendanceContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        injectViews(view);

        presenter.attachView(this, (OnFragmentIsReadyListener) getActivity());

        if (savedInstanceState != null) {
            presenter.setRestoredPosition(savedInstanceState.getInt(CURRENT_ITEM_KEY));
        }

        subjectsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<String>());
        subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectChooser.setAdapter(subjectsAdapter);
        subjectChooser.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        summary.setVisibility(View.INVISIBLE);
        details.setVisibility(View.VISIBLE);

        summaryAdapter.setDisplayHeadersAtStartUp(true);
        summaryRecyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(view.getContext()));
        summaryRecyclerView.setAdapter(summaryAdapter);
        summaryRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.attendance_action_menu, menu);
    }

    @Override
    public void updateSummaryAdapterList(List<AttendanceSummarySubItem> summarySubItems) {
        summaryAdapter.clear();
        summaryAdapter.updateDataSet(summarySubItems);
    }

    @Override
    public void setTotalAttendance(double total) {
        totalAttendance.setText(String.format(Locale.getDefault(), "%.2f%s", total, "%"));
    }

    @Override
    public void setSubjects(String[] items) {
        subjectsAdapter.clear();
        subjectsAdapter.addAll(items);
        subjectsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.reloadStatistics(((TextView) view).getText().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_attendance_summary_switch:
                boolean isDetailsVisible = details.getVisibility() == View.VISIBLE;

                item.setTitle(isDetailsVisible ? R.string.action_title_details : R.string.action_title_summary);
                details.setVisibility(isDetailsVisible ? View.INVISIBLE : View.VISIBLE);
                summary.setVisibility(isDetailsVisible ? View.VISIBLE : View.INVISIBLE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (presenter != null) {
            presenter.onFragmentActivated(menuVisible);
        }
    }

    @Override
    public void setTabDataToAdapter(String date) {
        pagerAdapter.addFragment(AttendanceTabFragment.newInstance(date), date);
    }

    @Override
    public void setAdapterWithTabLayout() {
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void scrollViewPagerToPosition(int position) {
        viewPager.setCurrentItem(position, false);
    }

    @Override
    public void setThemeForTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            tab.setCustomView(R.layout.current_week_tab);
        }
    }

    @Override
    public void setActivityTitle() {
        setTitle(getString(R.string.attendance_text));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_ITEM_KEY, viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
