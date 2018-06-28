package api.infrastructure.db.student


import api.domain.student.StudentId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class StudentH2RepositoryTest {
    @Mock
    private lateinit var repository : StudentCRUDRepository
    @InjectMocks
    private lateinit var underTest : StudentH2Repository

    @Test
    fun `should find student by id`()
    {
        // given
        val rawId = 1L
        val id = StudentId(rawId)
        given(repository.findById(rawId)).willReturn(
                Optional.of(Student(
                        rawId,
                        "firstName",
                        "lastName"
                ))
        )
        // when
        val output = underTest.fetchStudentById(id)
        // then
        assertThat(output).isEqualTo(
                api.domain.student.Student(
                        id,
                        "firstName",
                        "lastName"
                )
        )
    }

    @Test
    fun `should return null when not found`()
    {
        // given
        val rawId = 1L
        val id = StudentId(rawId)
        given(repository.findById(rawId)).willReturn(Optional.empty())

        // when
        val output = underTest.fetchStudentById(id)
        // then
        assertThat(output).isNull()
    }
}