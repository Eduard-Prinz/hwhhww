package hwtest.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
        import hwhwPrince.controller.StudentController;
import hwhwPrince.model.Student;
import hwhwPrince.repository.StudentRepository;
import hwhwPrince.service.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    List<Student> savedStudents;

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentService studentService;

    private static final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        Student student1 = new Student(1, "St1", 10);
        Student student2 = new Student(2, "St2", 20);
        List<Student> studentsList = List.of(student1, student2);

        savedStudents = studentRepository.saveAll(studentsList);
    }

    @Test
    void contextLoadsTest() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void testGetStudent() {
        String response = this.restTemplate.getForObject("/student", String.class);
        assertNotNull(response);
        assertFalse(response.contains("OK"));
    }
    @Test
    void infoStudentTest() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isEqualTo("Ok");
    }

    @Test
    void createStudentTest() throws JsonProcessingException, JSONException {
        String expected = mapper.writeValueAsString(savedStudents.get(0));
        Long studentId = savedStudents.get(0).getId();

        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/student/" + savedStudents.get(0).getId(),
                        String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals((MediaType.APPLICATION_JSON), response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

    @Test
    void editStudentTest() throws Exception {
        Student student = new Student(1, "St1", 15);

        HttpEntity<Student> entity = new HttpEntity<>(student);
        student.setId(savedStudents.get(0).getId());

        ResponseEntity<Student> response = restTemplate.exchange("/student/edit", HttpMethod.PUT, entity, Student.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(student, response.getBody());
    }

    @Test
    void readStudentTest() throws JsonProcessingException, JSONException {

        String expected = mapper.writeValueAsString(savedStudents.get(0));

        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/student/" + savedStudents.get(0).getId(),
                        String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    void deleteStudentTest() throws Exception {
        int studentId = 1;

        this.restTemplate.delete("http://localhost:" + port + "/student/delete/" + studentId);

        ResponseEntity<Student> responseEntity = this.restTemplate.getForEntity
                ("http://localhost:" + port + "/student/" + studentId, Student.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}