package geadezest.repository;

import geadezest.entity.Test;
import geadezest.entity.User;
import geadezest.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    List<UserAnswer> findAllByUserIdAndTest_CategoryId(Integer userId, Integer categoryId);

    boolean existsByUserIdAndTest(Integer userId, Test test);
}


