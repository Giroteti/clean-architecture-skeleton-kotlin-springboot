package api

import api.infrastructure.db.course.Course
import api.infrastructure.db.course.CourseCRUDRepository
import api.infrastructure.db.course.CourseH2Repository
import api.infrastructure.db.student.Student
import api.infrastructure.db.student.StudentCRUDRepository
import api.infrastructure.db.student.StudentH2Repository
import api.usecases.EnrolStudentToCourse
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["api.infrastructure.db"])
@EntityScan("api.infrastructure.db")
@SpringBootApplication(scanBasePackages = ["api"])
class Application {

    private val log = LoggerFactory.getLogger(Application::class.java)

    @Bean
    fun init(studentRepository: StudentCRUDRepository, courseRepository: CourseCRUDRepository) = CommandLineRunner {
        // save a couple of students
        studentRepository.save(Student(id = 1L, firstName = "Jack", lastName = "Wood"))
        studentRepository.save(Student(id = 2L, firstName = "Maria", lastName = "Bauer"))
        studentRepository.save(Student(id = 3L, firstName = "Bob", lastName = "Miller"))
        studentRepository.save(Student(id = 4L, firstName = "Jeanette", lastName = "Smith"))
        studentRepository.save(Student(id = 5L, firstName = "Peter", lastName = "Lane"))
        studentRepository.save(Student(id = 6L, firstName = "John", lastName = "Clark"))

        // save a couple of courses
        courseRepository.save(Course(id = 1L, name = "Clean architecture 101"))
        courseRepository.save(Course(id = 2L, name = "Test Driven Development level 1"))
        courseRepository.save(Course(id = 3L, name = "CQRS for beginners"))
        courseRepository.save(Course(id = 4L, name = "Mob programming basics"))
        courseRepository.save(Course(id = 5L, name = "Peer code review : getting started"))
    }

    @Bean
    fun provideEnrolStudentToCourseUseCase(
            studentH2Repository: StudentH2Repository,
            courseH2Repository: CourseH2Repository
    ): EnrolStudentToCourse = EnrolStudentToCourse(
            studentRepository = studentH2Repository,
            courseRepository = courseH2Repository
    )

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
