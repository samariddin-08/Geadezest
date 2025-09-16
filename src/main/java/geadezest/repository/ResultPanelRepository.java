package geadezest.repository;

import geadezest.entity.ResultPanel;
import geadezest.entity.enums.UserResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultPanelRepository extends JpaRepository<ResultPanel, Integer> {
        @Query("""
        SELECT r FROM ResultPanel r
        WHERE ((:userName IS NULL OR :userName = '') OR LOWER(r.user.firstName) LIKE LOWER(CONCAT('%', :userName, '%')))
          AND ((:categoryName IS NULL OR :categoryName = '') OR LOWER(r.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%')))
          AND (:userResults IS NULL OR r.userResults = :userResults)
        """)
        Page<ResultPanel> search(
                @Param("userName") String userName,
                @Param("categoryName") String categoryName,
                @Param("userResults") UserResults userResults,
                Pageable pageable
        );


}
