package hwtest.example;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import hwhwPrince.controller.FacultyController;
import hwhwPrince.model.Faculty;
import hwhwPrince.repository.FacultyRepository;
import hwhwPrince.service.FacultyService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void getInfoFacultyTest() throws Exception {
        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ok"));
    }

    @Test
    void readFacultyTest() throws Exception {

        Faculty faculty = new Faculty(1L, "St1", "Purple");

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("St1"))
                .andExpect(jsonPath("$.color").value("Purple"));
    }
}