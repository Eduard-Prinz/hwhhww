package hwtest.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import hwhwPrince.controller.FacultyController;
import hwhwPrince.model.Faculty;
import hwhwPrince.repository.FacultyRepository;
import hwhwPrince.service.FacultyService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    List<Faculty> savedFaculties;

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Faculty faculty1 = new Faculty(1, "St1", "Purple");
        Faculty faculty2 = new Faculty(2, "St2", "Green");
        List<Faculty> facultiesList = List.of(faculty1, faculty2);

        savedFaculties = facultyRepository.saveAll(facultiesList);
    }

    @Test
    void contextLoadsTest() throws Exception {
        boolean facultyControllerExists = facultyController != null;
        assertThat(facultyControllerExists).isNotNull();
    }

    @Test
    void testGetFaculty() {
        String response = this.restTemplate.getForObject("/faculty", String.class);
        assertNotNull(response);
        assertFalse(response.contains("Ok!"));
    }

    @Test
    void infoFacultyTest() throws Exception {
        String response = this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class);

        assertThat(response).isNotNull();

        assertThat(response).isEqualTo("Ok");
    }

    @Test
    void createFacultyTest() throws JsonProcessingException, JSONException {

        Faculty faculty = new Faculty(1, "St1", "Black");

        String expected = mapper.writeValueAsString(faculty);

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty/create", faculty, Faculty.class);

        assertEquals(HttpStatus.OK , response.getStatusCode());


    }
    @Test
    void readFacultyTest() throws JsonProcessingException, JSONException {

        String expected = mapper.writeValueAsString(savedFaculties.get(0));

        ResponseEntity<String> response =
                restTemplate.getForEntity
                        ("http://localhost:" + port + "/faculty/" + savedFaculties.get(0).getId(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    void editFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1, "St1", "Purple");

        facultyRepository.save(faculty);

        HttpEntity<Faculty> entity = new HttpEntity<>(faculty);

        faculty.setId(savedFaculties.get(0).getId());

        ResponseEntity<Faculty> response = restTemplate.exchange
                ("/faculty/edit", HttpMethod.PUT, entity, Faculty.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(faculty, response.getBody());
    }

    @Test
    void deleteFacultyTest() throws Exception {
        int facultyId = 1;

        this.restTemplate.delete("http://localhost:" + port + "/faculty/delete/" + facultyId);

        ResponseEntity<Faculty> responseEntity = this.restTemplate.getForEntity
                ("http://localhost:" + port + "/faculty/" + facultyId, Faculty.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}