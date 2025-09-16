package geadezest.repository;

import geadezest.entity.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(@Email String email, Integer id);

    @Query("""
select u from users u 
where (:name is null or :name = '' or lower(u.firstName)like lower(concat('%', :name, '%') ) )
""")
    Page<User> all(@Param("name")String name,
            Pageable pageable);
}
