package api.infrastructure.http.presentation

import api.usecases.StudentEnrolledToCourseEvent
import org.springframework.stereotype.Component

@Component
class EnrolmentSuccessJSONPresenter {
    fun present(event : StudentEnrolledToCourseEvent) = StudentEnrolledToCourseJSONResponse(
            studentId = event.studentId.id,
            courseId = event.courseId.id
    )
}

class StudentEnrolledToCourseJSONResponse(private val studentId: Long, private val courseId : Long)  {
    val message = "Student #$studentId successfully enrolled to class #$courseId"
}