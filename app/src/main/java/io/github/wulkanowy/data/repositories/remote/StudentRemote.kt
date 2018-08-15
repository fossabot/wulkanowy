package io.github.wulkanowy.data.repositories.remote

import io.github.wulkanowy.api.Vulcan
import io.github.wulkanowy.api.login.AccountPermissionException
import io.github.wulkanowy.data.db.entities.Student
import io.reactivex.Single
import org.apache.commons.lang3.StringUtils.stripAccents
import javax.inject.Inject

class StudentRemote @Inject constructor(private val api: Vulcan) {

    fun getConnectedStudents(email: String, password: String): Single<List<Student>> {
        return Single.fromCallable {
            initApi(email, password)
            var students: List<Student> = listOf()

            for (symbol in getSymbols()) {
                initApi(email, password, symbol)
                try {
                    for (school in api.schools) {
                        initApi(email, password, symbol, school.id)

                        students += api.studentAndParent.students.map {
                            Student(
                                    email = email,
                                    password = password,
                                    symbol = symbol,
                                    studentId = it.id.toLong(),
                                    studentName = it.name,
                                    schoolId = school.id.toLong(),
                                    schoolName = school.name
                            )
                        }
                    }
                } catch (e: AccountPermissionException) {
                    continue
                }
            }
            return@fromCallable students
        }
    }

    private fun initApi(email: String, password: String, symbol: String = "Deafult",
                        schoolId: String? = null, studentId: String? = null) {
        api.apply {
            logout()
            setCredentials(email, password, symbol, schoolId, studentId, null)
        }
    }

    private fun getSymbols(): List<String> {
        return api.symbols.map {
            stripAccents(it.replace("[\\s \\W]".toRegex(), ""))
        }
    }
}