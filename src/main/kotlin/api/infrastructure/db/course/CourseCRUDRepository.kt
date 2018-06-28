package api.infrastructure.db.course

import org.springframework.data.repository.CrudRepository

interface CourseCRUDRepository : CrudRepository<Course, Long>