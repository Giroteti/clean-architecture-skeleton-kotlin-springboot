package api.infrastructure.http.routing

import api.domain.course.CourseId
import api.domain.student.StudentId
import api.infrastructure.http.presentation.EnrolmentSuccessJSONPresenter
import api.usecases.EnrolStudentToCourse
import api.usecases.EnrolStudentToCourseCommand
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
        private val enrolStudentToCourse: EnrolStudentToCourse,
        private val enrolmentSuccessPresenter: EnrolmentSuccessJSONPresenter
) {

    @PostMapping("/enrol-student-to-course")
    fun enrolStudentToClass(
            @RequestParam("studentId") studentRawId: Long,
            @RequestParam("courseId") courseRawId: Long
    ) = enrolmentSuccessPresenter.present(
            enrolStudentToCourse.handle(
                    EnrolStudentToCourseCommand(
                            StudentId(studentRawId),
                            CourseId(courseRawId)
                    )
            )
    )

}