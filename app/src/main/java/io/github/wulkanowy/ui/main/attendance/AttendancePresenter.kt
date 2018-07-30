package io.github.wulkanowy.ui.main.attendance

import io.github.wulkanowy.data.RepositoryContract
import io.github.wulkanowy.ui.base.BasePresenter
import io.github.wulkanowy.ui.main.OnFragmentIsReadyListener
import io.github.wulkanowy.utils.async.AbstractTask
import io.github.wulkanowy.utils.async.AsyncListeners
import io.github.wulkanowy.utils.getFirstDayOfCurrentWeek
import io.github.wulkanowy.utils.getMondaysFromCurrentSchoolYear
import java.util.*
import javax.inject.Inject

class AttendancePresenter @Inject
internal constructor(repository: RepositoryContract) : BasePresenter<AttendanceContract.View>(repository), AttendanceContract.Presenter, AsyncListeners.OnFirstLoadingListener {

    private var loadingTask: AbstractTask? = null

    private var dates: List<String> = ArrayList()

    private val summarySubItems = ArrayList<AttendanceSummarySubItem>()

    private var listener: OnFragmentIsReadyListener? = null

    private var positionToScroll = 0

    private var isFirstSight = false

    override fun attachView(view: AttendanceContract.View, listener: OnFragmentIsReadyListener) {
        super.attachView(view)
        this.listener = listener

        if (view.isMenuVisible) {
            view.setActivityTitle()
        }

        if (dates.isEmpty()) {
            dates = getMondaysFromCurrentSchoolYear()
        }

        if (positionToScroll == 0) {
            positionToScroll = dates.indexOf(getFirstDayOfCurrentWeek())
        }

        if (!isFirstSight) {
            isFirstSight = true

            loadingTask = AbstractTask()
            loadingTask!!.setOnFirstLoadingListener(this)
            loadingTask!!.execute()
        }
    }

    override fun onFragmentActivated(isVisible: Boolean) {
        if (isVisible) {
            view.setActivityTitle()
        }
    }

    override fun onDoInBackgroundLoading() {
        for (date in dates) {
            view.setTabDataToAdapter(date)
        }

        val subjects = repository.dbRepo.attendanceSubjects
        val types = repository.dbRepo.getAttendanceStatistics(subjects[0].realId)

        if (types.isEmpty()) { return }

        types.sortByDescending { it.order }

        types.groupBy { it.month }.map {
            val summaryHeader = AttendanceSummaryHeader(it.key)
            it.value.mapTo(summarySubItems) {
                AttendanceSummarySubItem(summaryHeader, Pair(it.name, it.value))
            }
        }
    }

    override fun onCanceledLoadingAsync() {
        //do nothing
    }

    override fun onEndLoadingAsync(result: Boolean, exception: Exception?) {
        if (result) {
            view.setAdapterWithTabLayout()
            view.setThemeForTab(positionToScroll)
            view.scrollViewPagerToPosition(positionToScroll)
            view.updateSummaryAdapterList(summarySubItems)
            listener!!.onFragmentIsReady()
        }
    }

    override fun setRestoredPosition(position: Int) {
        this.positionToScroll = position
    }

    override fun detachView() {
        isFirstSight = false

        if (loadingTask != null) {
            loadingTask!!.cancel(true)
            loadingTask = null
        }
        super.detachView()
    }
}
