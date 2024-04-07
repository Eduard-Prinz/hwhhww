package hwhwPrince.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import hwhwPrince.model.Faculty;
import hwhwPrince.model.Student;
import hwhwPrince.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student createStudent(Student student) {
        return repository.save(student);
    }

    public Student readStudent(long id) {
        return repository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Student editStudent(Student student) {
        if (repository.existsById(student.getId())) {
            return repository.save(student);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
    }

    public void removeStudent(long id) {
        repository.deleteById(id);
    }

    public Collection<Student> getStudents() {
        return repository.findAll();
    }

    public List<Student> getStudentByAge(Integer age) {
        return repository.findAllByAge(age);
    }

    public List<Student> getStudentsByAgeBetween(Integer min, Integer max) {
        return repository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(long studentId) {
        Optional<Student> studentOptional = repository.findById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return student.getFaculty();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: "
                    + studentId);
        }
    }
}