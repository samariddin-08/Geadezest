package geadezest.repository;

import geadezest.entity.District;
import geadezest.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region,Integer> {
    List<Region> findByDistricts(List<District> districts);
}
