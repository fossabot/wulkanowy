package io.github.wulkanowy.ui.main.attendance

import io.github.wulkanowy.data.RepositoryContract
import io.github.wulkanowy.data.db.dao.entities.AttendanceSubject
import io.github.wulkanowy.data.db.dao.entities.AttendanceType
import io.github.wulkanowy.ui.base.BasePresenter
import io.github.wulkanowy.ui.main.OnFragmentIsReadyListener
import io.github.wulkanowy.utils.async.AbstractTask
import io.github.wulkanowy.utils.async.AsyncListeners
import io.github.wulkanowy.utils.calculateAttendanceFromTypes
import io.github.wulkanowy.utils.getFirstDayOfCurrentWeek
import io.github.wulkanowy.utils.getMondaysFromCurrentSchoolYear
import java.util.*
import javax.inject.Inject

class AttendancePresenter @Inject
internal constructor(repository: RepositoryContract) : BasePresenter<AttendanceContract.View>(repository),
        AttendanceContract.Presenter, AsyncListeners.OnFirstLoadingListener, AsyncListeners.OnRefreshListener {

    private var loadingTask: AbstractTask? = null

    private var refreshTask: AbstractTask? = null

    private var isFirstSight = false

    private lateinit var listener: OnFragmentIsReadyListener

    private var dates: List<String> = ArrayList()

    private var positionToScroll = 0

    private val summarySubItems = ArrayList<AttendanceSummarySubItem>()

    private var totalAttendance = 0.0

    private var subjects = mutableListOf<AttendanceSubject>()

    private var subjectName = "Wszystkie"

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

    override fun reloadStatistics(name: String) {
        subjectName = name
        refreshTask = AbstractTask()
        refreshTask!!.setOnRefreshListener(this)
        refreshTask!!.execute()
    }

    // first load
    override fun onDoInBackgroundLoading() {
        for (date in dates) {
            view.setTabDataToAdapter(date)
        }

        subjects = repository.dbRepo.attendanceSubjects

        val types = repository.dbRepo.getAttendanceStatistics(-1)

        if (types.isEmpty()) return

        updateSummary(types)
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
            view.setTotalAttendance(totalAttendance)
            view.setSubjects(subjects.map {
                it.name
            }.toTypedArray())
        }
        listener.onFragmentIsReady()
    }

    // refresh
    override fun onDoInBackgroundRefresh() {
        summarySubItems.clear()
        val subjectId = subjects.first { it.name == subjectName }.realId
        var types = repository.dbRepo.getAttendanceStatistics(subjectId)

        if (types.isEmpty()) {
            repository.syncRepo.syncAttendanceStatistics(subjectId)
            types = repository.dbRepo.getAttendanceStatistics(subjectId)
        }

        updateSummary(types)
    }

    override fun onCanceledRefreshAsync() {
        //do nothing
    }

    override fun onEndRefreshAsync(result: Boolean, exception: java.lang.Exception?) {
        if (result) {
            view.updateSummaryAdapterList(summarySubItems)
            view.setTotalAttendance(totalAttendance)
        }
        listener.onFragmentIsReady()
    }

    private fun updateSummary(types: MutableList<AttendanceType>) {
        totalAttendance = calculateAttendanceFromTypes(types)

        types.sortByDescending { it.order }

        types.groupBy { it.month }.map {
            val summaryHeader = AttendanceSummaryHeader(it.key, calculateAttendanceFromTypes(it.value))
            it.value.mapTo(summarySubItems) {
                AttendanceSummarySubItem(summaryHeader, Pair(it.name, it.value))
            }
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
