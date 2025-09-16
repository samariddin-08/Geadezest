package geadezest.repository;

import geadezest.entity.User;
import jakarta.validation.constraints.Email;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(@Email String email, Integer id);

    List<User> findByFirstNameContainingIgnoreCase(String name);
}
