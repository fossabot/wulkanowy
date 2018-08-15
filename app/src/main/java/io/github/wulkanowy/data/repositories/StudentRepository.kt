package io.github.wulkanowy.data.repositories

import io.github.wulkanowy.data.db.entities.Student
import io.github.wulkanowy.data.repositories.local.StudentLocal
import io.github.wulkanowy.data.repositories.remote.StudentRemote
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(private val local: StudentLocal,
                                            private val remote: StudentRemote) {

    val isStudentLoggedIn: Boolean
        get() = local.isStudentLoggedIn

    fun getConnectedStudents(email: String, password: String): Single<List<Student>> {
        return remote.getConnectedStudents(email, password)
    }

    fun save(student: Student) {
        local.save(student)
    }

    fun getCurrentStudent(): Single<Student> = local.getCurrentStudent()
}