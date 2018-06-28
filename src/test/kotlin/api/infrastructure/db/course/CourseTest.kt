package api.infrastructure.db.course

import api.domain.course.CourseId
import api.domain.student.Student
import api.domain.student.StudentId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CourseTest {

    @Test
    fun `should instanciate through domain entity dehydration`()
    {
        val output = Course.fromDomainEntity(api.domain.course.Course(
                CourseId(1L),
                "name",
                mutableListOf(
                        Student(
                                StudentId(2L),
                                "firstName",
                                "lastName"
                        )
                )
        ))

        assertThat(output).isEqualToComparingFieldByFieldRecursively(
                Course(
                        1L,
                        "name",
                        listOf(
                                api.infrastructure.db.student.Student(
                                        2L,
                                        "firstName",
                                        "lastName"
                                )
                        )
                )
        )
    }

    @Test
    fun `should hydrate domain entity`()
    {
        // given
        val underTest = Course(
                1L,
                "course name",
                listOf(
                        api.infrastructure.db.student.Student(
                                2L,
                                "firstName",
                                "lastName"
                        )
                )
        )

        // when
        val output = underTest.toDomainEntity()

        // then
        assertThat(output).isEqualToComparingFieldByFieldRecursively(
                api.domain.course.Course(
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
        )
    }
}