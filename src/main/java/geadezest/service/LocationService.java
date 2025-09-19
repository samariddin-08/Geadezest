package geadezest.service;

import geadezest.entity.District;
import geadezest.entity.Region;
import geadezest.entity.User;
import geadezest.entity.enums.Role;
import geadezest.payload.ApiResponse;
import geadezest.payload.RegionDTO;
import geadezest.repository.DistrictRepository;
import geadezest.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;

    public ApiResponse AddRegion(User user,String regionName) {
        if ((user.getRole().equals(Role.ROLE_USER))){
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST,false, null);
        }
        Region region = new Region();
        region.setName(regionName);
        regionRepository.save(region);
        return new ApiResponse("Saqlandi", HttpStatus.CREATED,true,"Id -> " +region.getId());
    }

    public ApiResponse addDistrict(User user,String districtName,Integer regionId) {
        if ((user.getRole().equals(Role.ROLE_USER))){
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST,false, null);
        }
        Optional<Region> byId = regionRepository.findById(regionId);
        if (byId.isPresent()){
            Region region = byId.get();
            District district = new District();
            district.setName(districtName);
            district.setRegion(region);
            region.setDistricts(List.of(district));
            districtRepository.save(district);
            regionRepository.save(region);
            return new ApiResponse("Saqlandi", HttpStatus.OK,true,null);
        }
        return new ApiResponse("Topilmadi", HttpStatus.NOT_FOUND,true,null);
    }
    public ApiResponse deleteDistrict(User user,Integer districtId) {
        if ((user.getRole().equals(Role.ROLE_USER))){
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST,false, null);
        }
        Optional<District> byId = districtRepository.findById(districtId);
        if (byId.isPresent()){
            District district = byId.get();
            districtRepository.delete(district);
            return new ApiResponse("Uchirildi", HttpStatus.OK,true,null);
        }
        return new ApiResponse("Topilmadi", HttpStatus.NOT_FOUND,false,null);
    }

    public ApiResponse deleteRegion(User user,Integer regionId) {
        if ((user.getRole().equals(Role.ROLE_USER))){
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST,false, null);
        }
        Optional<Region> byId = regionRepository.findById(regionId);
        if (byId.isPresent()){
            Region region = byId.get();
            regionRepository.delete(region);
            return new ApiResponse("Uchirildi", HttpStatus.OK,true,null);
        }
        return new ApiResponse("Topilmadi", HttpStatus.NOT_FOUND,false,null);
    }
    public ApiResponse editDistrict(User user,Integer districtId,String districtName) {
        if ((user.getRole().equals(Role.ROLE_USER))){
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST,false, null);
        }
        Optional<District> byId = districtRepository.findById(districtId);
        if (byId.isPresent()){
            District district = byId.get();
            district.setName(districtName);
            districtRepository.save(district);
            return new ApiResponse("Tahrirlandi", HttpStatus.OK,true,null);
        }
        return new ApiResponse("Topilmadi", HttpStatus.NOT_FOUND,false,null);
    }
    public ApiResponse editRegion(User user,Integer regionId,String regionName) {
        if ((user.getRole().equals(Role.ROLE_USER))){
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST,false, null);
        }
        Optional<Region> byId = regionRepository.findById(regionId);
        if (byId.isPresent()){
            Region region = byId.get();
            region.setName(regionName);
            regionRepository.save(region);
            return new ApiResponse("Tahrirlandi", HttpStatus.OK,true,null);
        }
        return new ApiResponse("Topilmadi", HttpStatus.NOT_FOUND,false,null);
    }


    public ApiResponse getRegion(User user,int page,int size) {
        if ((user.getRole().equals(Role.ROLE_USER))) {
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST, false, null);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Region> all = regionRepository.findAll(pageable);
        if (all.getTotalElements() == 0) {
            return new ApiResponse("mavjud emas", HttpStatus.NOT_FOUND,false,null);
        }
        Map<Integer,String> regionNames = new HashMap<>();
        for (Region region : all.getContent()) {
            regionNames.put(region.getId(), region.getName());
        }
        return new ApiResponse("Regions", HttpStatus.OK,true,regionNames);
    }
    public ApiResponse getDistrict(User user,int  page,int size) {
        if ((user.getRole().equals(Role.ROLE_USER))) {
            return new ApiResponse("Kirishga ruxsat yuq", HttpStatus.BAD_REQUEST, false, null);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<District> all = districtRepository.findAll(pageable);
        if (all.getTotalElements() == 0) {
            return new ApiResponse("mavjud emas", HttpStatus.NOT_FOUND,false,null);
        }
        List<RegionDTO> regionDTOs = new ArrayList<>();
        for (District district : all.getContent()) {
            RegionDTO regionDTO = new RegionDTO();
            regionDTO.setId(district.getId());
            regionDTO.setDistrictName(district.getName());
            regionDTO.setRegionName(district.getRegion().getName());
            regionDTOs.add(regionDTO);
        }
        return new ApiResponse("Districts", HttpStatus.OK,true,regionDTOs);
    }
}
