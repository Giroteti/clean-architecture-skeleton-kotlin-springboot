package api.usecases

import api.domain.course.CourseId
import api.domain.course.CourseRepository
import api.domain.student.StudentId
import api.domain.student.StudentRepository

class EnrolStudentToCourse(val studentRepository: StudentRepository, val courseRepository: CourseRepository) {
    fun handle(command: EnrolStudentToCourseCommand): StudentEnrolledToCourseEvent {
        val course = courseRepository.fetchCourseById(command.courseId)
                ?: throw EnrolmentFailureCourseNotFoundException(
                courseId = command.courseId,
                studentId = command.studentId
        )
        val student = studentRepository.fetchStudentById(command.studentId)
                ?: throw EnrolmentFailureStudentNotFoundException(
                courseId = command.courseId,
                studentId = command.studentId
        )

        course.enrolStudent(student)
        courseRepository.save(course)
        return StudentEnrolledToCourseEvent(
                studentId = command.studentId,
                courseId = command.courseId
        )
    }
}

data class EnrolStudentToCourseCommand(val studentId: StudentId, val courseId: CourseId)
data class StudentEnrolledToCourseEvent(val studentId: StudentId, val courseId: CourseId)

class EnrolmentFailureCourseNotFoundException(val courseId: CourseId, val studentId: StudentId) : RuntimeException()
class EnrolmentFailureStudentNotFoundException(val courseId: CourseId, val studentId: StudentId) : RuntimeException()
