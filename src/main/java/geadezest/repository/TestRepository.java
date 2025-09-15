package geadezest.repository;

import geadezest.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
//    long countByCategory_Id(Integer categoryId);


}
