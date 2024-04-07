package hwhwPrince.controller;

import hwhwPrince.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hwhwPrince.model.Faculty;
import hwhwPrince.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<String> getInfoFaculty() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/create")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = service.createFaculty(faculty);
        return ResponseEntity.ok(createdFaculty);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Faculty> readFaculty(@PathVariable Long id) {
        Faculty faculty = service.readFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/edit")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editFaculty = service.editFaculty(faculty);
        return ResponseEntity.ok().body(editFaculty);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity removeFaculty(@PathVariable Long id) {
        service.removeFaculty(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam("facultyName") String facultyName,
                                         @RequestParam("color") String color) {
        return service.searchFaculties(facultyName, color);
    }

    @GetMapping("/{facultyId}/students")
    public ResponseEntity<List<Student>> getFacultyStudents(@PathVariable("facultyId") Long facultyId) {
        List<Student> students = service.getFacultyStudents(facultyId);
        return ResponseEntity.ok().body(students);
    }
}