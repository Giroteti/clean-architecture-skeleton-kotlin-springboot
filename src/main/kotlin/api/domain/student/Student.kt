package api.domain.student

data class Student(
        val id: StudentId,
        val firstName: String,
        val lastName: String)

data class StudentId(val id : Long)