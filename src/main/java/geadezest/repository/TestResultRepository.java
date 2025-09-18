package geadezest.repository;

import geadezest.entity.Option;
import geadezest.entity.TestResult;
import geadezest.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
    List<TestResult> findByUser(User user);
    Optional<TestResult> findByUserr(User user);

    Integer countByCategoryId(Integer categoryId);
}
