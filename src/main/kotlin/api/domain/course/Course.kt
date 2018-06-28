package api.domain.course

import api.domain.student.Student
import api.domain.student.StudentId

class Course(
        val id: CourseId,
        val name: String,
        val enrolledStudents: MutableList<Student> = mutableListOf()
) {
    companion object {
        const val MAX_NUMBER_OF_STUDENTS_PER_COURSE = 5
    }
    fun enrolStudent(student: Student) {
        if (enrolledStudents.contains(student)) {
            throw EnrolmentFailureStudentAlreadyEnrolledException(
                    studentId = student.id,
                    courseId = this.id
            )
        }
        if (enrolledStudents.size < MAX_NUMBER_OF_STUDENTS_PER_COURSE) {
            this.enrolledStudents.add(student)
        } else {
            throw EnrolmentFailureCourseFullException(
                    studentId = student.id,
                    courseId = this.id
            )
        }
    }
}

class EnrolmentFailureStudentAlreadyEnrolledException(val courseId: CourseId, val studentId: StudentId) : RuntimeException()

class EnrolmentFailureCourseFullException(val courseId: CourseId, val studentId: StudentId) : RuntimeException()

data class CourseId(val id : Long)