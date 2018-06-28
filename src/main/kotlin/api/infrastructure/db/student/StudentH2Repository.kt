package api.infrastructure.db.student

import api.domain.student.Student
import api.domain.student.StudentId
import api.domain.student.StudentRepository
import org.springframework.stereotype.Component

@Component
class StudentH2Repository(private val crudRepository: StudentCRUDRepository) : StudentRepository {
    override fun fetchStudentById(id: StudentId): Student? {
        return crudRepository.findById(id.id).orElse(null)?.toDomainEntity()
    }
}