package hwhwPrince.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hwhwPrince.model.Avatar;

import java.util.Optional;

public interface AvatarRepository  extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(Long studentId);
}