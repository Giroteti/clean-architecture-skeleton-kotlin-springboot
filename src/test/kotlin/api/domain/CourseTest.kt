package api.domain

import api.domain.course.Course
import api.domain.course.CourseId
import api.domain.course.EnrolmentFailureCourseFullException
import api.domain.course.EnrolmentFailureStudentAlreadyEnrolledException
import api.domain.student.Student
import api.domain.student.StudentId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CourseTest {
    private val studentToEnrol = Student(
            StudentId(2L),
            "firstName",
            "lastName"
    )

    @Test
    fun `should enrol student`() {
        // given
        val underTest = Course(
                CourseId(1L),
                "course name",
                mutableListOf()
        )
        val expected = Course(
                CourseId(1L),
                "course name",
                mutableListOf(
                        studentToEnrol
                )
        )

        // when
        underTest.enrolStudent(
                studentToEnrol
        )

        // then
        assertThat(underTest).isEqualToComparingFieldByFieldRecursively(expected)
    }

    @Test
    fun `should fail enrolling student if already enrolled`() {
        // given
        val underTest = Course(
                CourseId(1L),
                "course name",
                mutableListOf(
                        studentToEnrol
                )
        )

        // when/then
        Assertions.assertThrows(EnrolmentFailureStudentAlreadyEnrolledException::class.java, {
            underTest.enrolStudent(studentToEnrol)
        })
    }

    @Test
    fun `should fail enrolling student if too many students already enrolled`()
    {
        // given
        val dummyStudent = Student(
                StudentId(3L),
                "firstName",
                "lastName"
        )
        val underTest = Course(
                CourseId(1L),
                "course name",
                mutableListOf(
                        dummyStudent,
                        dummyStudent,
                        dummyStudent,
                        dummyStudent,
                        dummyStudent
                )
        )

        // when/then
        Assertions.assertThrows(EnrolmentFailureCourseFullException::class.java, {
            underTest.enrolStudent(studentToEnrol)
        })
    }
}