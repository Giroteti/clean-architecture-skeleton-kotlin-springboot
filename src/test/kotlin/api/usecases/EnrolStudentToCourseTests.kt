package api.usecases

import api.domain.course.*
import api.domain.student.Student
import api.domain.student.StudentId
import api.domain.student.StudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class EnrolStudentToCourseTests {
    @Mock
    private lateinit var courseRepository: CourseRepository
    @Mock
    private lateinit var studentRepository: StudentRepository
    @Captor
    private lateinit var captor: ArgumentCaptor<Course>
    @InjectMocks
    private lateinit var underTest: EnrolStudentToCourse

    @Test
    fun `should enrol a student to course`() {
        // given
        val studentId = StudentId(1L)
        val courseId = CourseId(2L)
        val courseName = "Chemistry 101"
        val studentToEnrol = Student(
                id = studentId,
                firstName = "John",
                lastName = "Doe"
        )
        given(studentRepository.fetchStudentById(studentId)).willReturn(studentToEnrol)
        given(courseRepository.fetchCourseById(courseId)).willReturn(
                Course(
                        id = courseId,
                        name = courseName,
                        enrolledStudents = mutableListOf()
                )
        )
        val expectedSavedCourse = Course(
                id = courseId,
                name = courseName,
                enrolledStudents = mutableListOf(
                        studentToEnrol
                )
        )

        // when
        val output = underTest.handle(EnrolStudentToCourseCommand(
                studentId,
                courseId
        ))

        // then
        then(courseRepository).should().save(capture(captor))
        assertThat(captor.value).isEqualToComparingFieldByField(expectedSavedCourse)
        assertThat(output).isEqualToComparingFieldByField(
                StudentEnrolledToCourseEvent(
                        courseId = courseId,
                        studentId = studentId
                )
        )
    }

    @Test
    fun `should fail when student already enrolled`()
    {
        // given
        val studentId = StudentId(1L)
        val courseId = CourseId(2L)
        val courseName = "Chemistry 101"
        val studentToEnrol = Student(
                id = studentId,
                firstName = "John",
                lastName = "Doe"
        )
        given(studentRepository.fetchStudentById(studentId)).willReturn(studentToEnrol)
        given(courseRepository.fetchCourseById(courseId)).willReturn(
                Course(
                        id = courseId,
                        name = courseName,
                        enrolledStudents = mutableListOf(
                                studentToEnrol
                        )
                )
        )

        // when/then
        assertThrows(EnrolmentFailureStudentAlreadyEnrolledException::class.java, {
            underTest.handle(EnrolStudentToCourseCommand(
                    studentId,
                    courseId
            ))
        })
    }

    @Test
    fun `should fail when course is full`() {
        // given
        val studentId = StudentId(1L)
        val courseId = CourseId(2L)
        val courseName = "Chemistry 101"
        val dummyStudent = Student(
                id = studentId,
                firstName = "Mack",
                lastName = "Page"
        )
        val studentToEnrol = Student(
                id = studentId,
                firstName = "John",
                lastName = "Doe"
        )
        given(studentRepository.fetchStudentById(studentId)).willReturn(studentToEnrol)
        given(courseRepository.fetchCourseById(courseId)).willReturn(
                Course(
                        id = courseId,
                        name = courseName,
                        enrolledStudents = mutableListOf(
                                dummyStudent,
                                dummyStudent,
                                dummyStudent,
                                dummyStudent,
                                dummyStudent
                        )
                )
        )

        // when/then
        assertThrows(EnrolmentFailureCourseFullException::class.java, {
            underTest.handle(EnrolStudentToCourseCommand(
                    studentId,
                    courseId
            ))
        })
    }

    @Test
    fun `should fail when course not found`() {
        // given
        val studentId = StudentId(1L)
        val courseId = CourseId(2L)
        given(courseRepository.fetchCourseById(courseId)).willThrow(EnrolmentFailureCourseNotFoundException(
                courseId = courseId,
                studentId = studentId
        ))

        // when/then
        assertThrows(EnrolmentFailureCourseNotFoundException::class.java, {
            underTest.handle(EnrolStudentToCourseCommand(
                    studentId,
                    courseId
            ))
        })
    }

    @Test
    fun `should fail when student not found`() {
        val studentId = StudentId(1L)
        val courseId = CourseId(2L)
        val courseName = "Chemistry 101"
        given(studentRepository.fetchStudentById(studentId)).willThrow(EnrolmentFailureStudentNotFoundException(
                studentId = studentId,
                courseId = courseId
        ))
        given(courseRepository.fetchCourseById(courseId)).willReturn(
                Course(
                        id = courseId,
                        name = courseName,
                        enrolledStudents = mutableListOf()
                )
        )
        // when/then
        assertThrows(EnrolmentFailureStudentNotFoundException::class.java, {
            underTest.handle(EnrolStudentToCourseCommand(
                    studentId,
                    courseId
            ))
        })
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}
