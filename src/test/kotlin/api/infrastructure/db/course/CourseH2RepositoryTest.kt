package api.infrastructure.db.course

import api.domain.course.CourseId
import api.domain.student.Student
import api.domain.student.StudentId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.only
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class CourseH2RepositoryTest {
    @Mock
    private lateinit var repository: CourseCRUDRepository
    @InjectMocks
    private lateinit var underTest: CourseH2Repository

    @Test
    fun `should find course by id`() {
        // given
        val courseId = CourseId(1L)
        given(repository.findById(courseId.id)).willReturn(
                Optional.of(Course(
                        1L,
                        "course name",
                        emptyList()
                ))
        )
        // when
        val output = underTest.fetchCourseById(courseId)

        // then
        assertThat(output).isEqualToComparingFieldByFieldRecursively(
                api.domain.course.Course(
                        courseId,
                        "course name",
                        mutableListOf()
                )
        )
    }

    @Test
    fun `should return null when course not found`() {
        // given
        val courseId = CourseId(1L)
        given(repository.findById(courseId.id)).willReturn(
                Optional.empty()
        )
        // when
        val output = underTest.fetchCourseById(courseId)

        // then
        assertThat(output).isNull()
    }

    @Test
    fun `should save course`()
    {
        // given
        val inputOutput = api.domain.course.Course(
                CourseId(1L),
                "course name",
                mutableListOf(
                        Student(
                                StudentId(2L),
                                "firstName",
                                "lastName"
                        )
                )
        )
        val dbEntity = Course.fromDomainEntity(inputOutput)
        given(repository.save(dbEntity)).willReturn(dbEntity)

        // when
        val output = underTest.save(inputOutput)

        // then
        then(repository).should(only()).save(dbEntity)
        assertThat(output).isEqualToComparingFieldByFieldRecursively(inputOutput)
    }
}