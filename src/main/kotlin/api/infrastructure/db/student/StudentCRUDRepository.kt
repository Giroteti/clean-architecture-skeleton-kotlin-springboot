package api.infrastructure.db.student

import org.springframework.data.repository.CrudRepository

interface StudentCRUDRepository : CrudRepository<Student, Long>
