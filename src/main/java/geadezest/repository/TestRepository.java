package geadezest.repository;

import geadezest.entity.Test;
import geadezest.entity.enums.Question_type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {


    @Query("""
    SELECT t FROM Test t
    WHERE (:name IS NULL OR :name = '' OR LOWER(t.question) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:categoryName IS NULL OR :categoryName = '' OR LOWER(t.category.name) = LOWER(:categoryName))
      AND (:questionType IS NULL OR t.question_type = :questionType)
    """)
    Page<Test> getAll(
            @Param("name") String name,
            @Param("categoryName") String categoryName,
            @Param("questionType") Question_type questionType,
            Pageable pageable
    );


    List<Test> findAllByCategory_Id(Integer categoryId);

    int countByCategory_Id(Integer categoryId);
}
