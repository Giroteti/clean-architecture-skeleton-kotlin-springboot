package api.domain.course

interface CourseRepository {
    fun fetchCourseById(id : CourseId) : Course?
    fun save(course: Course) : Course
}