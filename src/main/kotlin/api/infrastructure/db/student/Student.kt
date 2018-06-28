package api.infrastructure.db.student

import api.domain.student.StudentId
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Student(
        @Id
        val id: Long,
        val firstName: String,
        val lastName: String) {

    companion object {
        fun fromDomainEntity(student : api.domain.student.Student): Student = Student(
                id = student.id.id,
                firstName = student.firstName,
                lastName = student.lastName
        )
    }

    fun toDomainEntity(): api.domain.student.Student = api.domain.student.Student(
            id = StudentId(id),
            firstName = firstName,
            lastName = lastName
    )
}
