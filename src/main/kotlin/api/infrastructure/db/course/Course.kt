package api.infrastructure.db.course

import api.domain.course.Course
import api.domain.course.CourseId
import api.infrastructure.db.student.Student
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Course(
        @Id
        val id : Long,
        val name : String,
        @ManyToMany
        val enrolledStudents: List<Student> = emptyList()
) {

    companion object {
        fun fromDomainEntity(course : Course) : api.infrastructure.db.course.Course {
            return api.infrastructure.db.course.Course(
                    id=course.id.id,
                    name = course.name,
                    enrolledStudents = course.enrolledStudents.map{Student.fromDomainEntity(it)}
            )
        }
    }

    fun toDomainEntity(): Course {
        return api.domain.course.Course(
                id = CourseId(id),
                name = name,
                enrolledStudents = enrolledStudents.map { it.toDomainEntity() }.toMutableList()
        )
    }
}
