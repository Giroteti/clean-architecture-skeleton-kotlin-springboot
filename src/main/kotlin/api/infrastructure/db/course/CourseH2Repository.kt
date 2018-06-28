package api.infrastructure.db.course

import api.domain.course.Course
import api.domain.course.CourseId
import api.domain.course.CourseRepository
import org.springframework.stereotype.Component

@Component
class CourseH2Repository(private val crudRepository: CourseCRUDRepository) : CourseRepository  {
    override fun fetchCourseById(id: CourseId): Course? {
        return crudRepository.findById(id.id).orElse(null)?.toDomainEntity()
    }

    override fun save(course: Course): Course {
        val dbCourse = api.infrastructure.db.course.Course.fromDomainEntity(course)
        return crudRepository.save(dbCourse).toDomainEntity()
    }
}