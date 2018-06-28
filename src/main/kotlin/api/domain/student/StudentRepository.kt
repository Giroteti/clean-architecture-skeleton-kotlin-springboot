package api.domain.student

interface StudentRepository {
    fun fetchStudentById(id : StudentId) : Student?
}