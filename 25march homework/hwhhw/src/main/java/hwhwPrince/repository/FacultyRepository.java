package hwhwPrince.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hwhwPrince.model.Faculty;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByColorIgnoreCase(String color);

    List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String facultyName, String color);
}