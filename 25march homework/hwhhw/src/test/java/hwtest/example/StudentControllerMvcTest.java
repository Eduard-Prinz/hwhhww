package hwtest.example;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import hwhwPrince.controller.StudentController;
import hwhwPrince.model.Faculty;
import hwhwPrince.model.Student;
import hwhwPrince.repository.StudentRepository;
import hwhwPrince.service.StudentService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;


    @Test
    void getInfoStudentTest() throws Exception {
        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ok"));
    }

    @Test
    void readStudentTest() throws Exception {

        Student student = new Student(1L, "St1", 10);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("St1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(10));

    }


    @Test
    void getStudentsByAgeBetweenTest() throws Exception {
        Student student = new Student(1L, "St1", 10);
        Student student2 = new Student(2L, "St2", 20);

        when(studentRepository.findByAgeBetween(anyInt(), anyInt())).thenReturn(Arrays.asList(student, student2));

        mockMvc.perform(get("/student/findByAgeBetween")
                        .param("min", "10")
                        .param("max", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json
                        ("[{\"name\":\"St1\",\"age\":10},{\"name\":\"St2\",\"age\":20}]"));
    }

    @Test
    void getStudentFacultyTest() throws Exception {
        Student student = new Student(1L, "St1", 22);
        Faculty faculty = new Faculty(1L, "St2", "Purple");
        student.setFaculty(faculty);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/1/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"St2\",\"color\":\"Purple\"}"));
    }
}