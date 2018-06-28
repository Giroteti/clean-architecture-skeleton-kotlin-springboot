package api.infrastructure.http.routing

import api.domain.course.EnrolmentFailureCourseFullException
import api.domain.course.EnrolmentFailureStudentAlreadyEnrolledException
import api.infrastructure.http.presentation.EnrolmentFailureJSONPresenter
import api.usecases.EnrolmentFailureCourseNotFoundException
import api.usecases.EnrolmentFailureStudentNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


@ControllerAdvice(assignableTypes = [Controller::class])
class ControllerAdvice(private val presenter: EnrolmentFailureJSONPresenter) {

    @ExceptionHandler(EnrolmentFailureCourseNotFoundException::class)
    fun enrolmentFailureCourseNotFound(e: EnrolmentFailureCourseNotFoundException)
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            presenter.presentCourseNotFound(e)
    )

    @ExceptionHandler(EnrolmentFailureStudentNotFoundException::class)
    fun enrolmentFailureStudentNotFound(e: EnrolmentFailureStudentNotFoundException)
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            presenter.presentStudentNotFound(e)
    )

    @ExceptionHandler(EnrolmentFailureCourseFullException::class)
    fun enrolmentFailureCourseIsFull(e: EnrolmentFailureCourseFullException)
            = ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            presenter.presentCourseFull(e)
    )

    @ExceptionHandler(EnrolmentFailureStudentAlreadyEnrolledException::class)
    fun enrolmentFailureStudentAlreadyEnrolled(e: EnrolmentFailureStudentAlreadyEnrolledException)
            = ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            presenter.presentStudentAlreadyEnrolled(e)
    )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(e: MethodArgumentTypeMismatchException) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            presenter.presentArgumentTypeMismatch(e)
    )

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingArgument(e: MissingServletRequestParameterException) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            presenter.presentMissingArgument(e)
    )
}