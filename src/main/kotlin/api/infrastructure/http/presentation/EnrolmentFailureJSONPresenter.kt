package api.infrastructure.http.presentation

import api.domain.course.EnrolmentFailureCourseFullException
import api.domain.course.EnrolmentFailureStudentAlreadyEnrolledException
import api.usecases.EnrolmentFailureCourseNotFoundException
import api.usecases.EnrolmentFailureStudentNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.sql.Timestamp
import java.util.*

@Component
class EnrolmentFailureJSONPresenter {
    fun presentCourseNotFound(e: EnrolmentFailureCourseNotFoundException) =
            EnrolmentFailureJSONResponse(
                    message = "Cannot enrol student #${e.studentId.id} to course #${e.courseId.id} : course not found",
                    code = "ERR-1"
            )

    fun presentStudentNotFound(e: EnrolmentFailureStudentNotFoundException) =
            EnrolmentFailureJSONResponse(
                    message = "Cannot enrol student #${e.studentId.id} to course #${e.courseId.id} : student not found",
                    code = "ERR-2"
            )

    fun presentCourseFull(e: EnrolmentFailureCourseFullException) =
            EnrolmentFailureJSONResponse(
                    message = "Cannot enrol student #${e.studentId.id} to course #${e.courseId.id} : course is full",
                    code = "ERR-3"
            )

    fun presentStudentAlreadyEnrolled(e: EnrolmentFailureStudentAlreadyEnrolledException) =
            EnrolmentFailureJSONResponse(
                    message = "Cannot enrol student #${e.studentId.id} to course #${e.courseId.id} : student is already enrolled",
                    code = "ERR-4"
            )

    fun presentArgumentTypeMismatch(e: MethodArgumentTypeMismatchException): ArgumentConversionFailureJSONResponse {
        val name = e.name
        val type: String? = e.requiredType?.simpleName
        val value = e.value
        val message = String.format(
                "Failed to convert '%s' to required type '%s' for  argument '%s'",
                value,
                type,
                name
        )
        return ArgumentConversionFailureJSONResponse(
                timestamp = Timestamp(Calendar.getInstance().time.time).toString(),
                message = message,
                code = "BAD-REQUEST-ERR-1"

        )
    }

    fun presentMissingArgument(e: MissingServletRequestParameterException): ArgumentConversionFailureJSONResponse {
        val name = e.parameterName
        val type: String? = e.parameterType
        val message = String.format(
                "Required %s parameter '%s' is not present",
                type,
                name
        )
        return ArgumentConversionFailureJSONResponse(
                timestamp = Timestamp(Calendar.getInstance().time.time).toString(),
                message = message,
                code = "BAD-REQUEST-ERR-2"
        )
    }
}

data class EnrolmentFailureJSONResponse(val message: String, val code: String)
data class ArgumentConversionFailureJSONResponse(
        val timestamp : String,
        val message: String,
        val code : String
        ) {
    val error = "Bad Request"
    val status = 400
    val path = "/enrol-student-to-course"
}