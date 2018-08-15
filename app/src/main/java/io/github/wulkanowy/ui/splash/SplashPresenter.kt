package io.github.wulkanowy.ui.splash

import io.github.wulkanowy.data.repositories.StudentRepository
import io.github.wulkanowy.ui.base.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SplashPresenter @Inject constructor(private val studentRepository: StudentRepository,
                                          composite: CompositeDisposable)
    : BasePresenter<SplashContract.View>(composite), SplashContract.Presenter {

    override fun attachView(view: SplashContract.View) {
        super.attachView(view)
        view.cancelNotifications()

        if (studentRepository.isStudentLoggedIn) {
            view.openMainActivity()
        } else {
            view.openLoginActivity()
        }
    }
}
