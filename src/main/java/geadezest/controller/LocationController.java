package geadezest.controller;

import geadezest.entity.User;
import geadezest.payload.ApiResponse;
import geadezest.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {


    private final LocationService regionService;

    @PostMapping("/add/region")
    public ResponseEntity<ApiResponse> addRegion(
            @AuthenticationPrincipal User user,
            @RequestParam String regionName
    ) {
        ApiResponse response = regionService.AddRegion(user, regionName);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/add/district")
    public ResponseEntity<ApiResponse> addDistrict(
            @AuthenticationPrincipal User user,
            @RequestParam String districtName,
            @RequestParam Integer regionId
    ) {
        ApiResponse response = regionService.addDistrict(user, districtName, regionId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/district/{districtId}")
    public ResponseEntity<ApiResponse> deleteDistrict(
            @AuthenticationPrincipal User user,
            @PathVariable Integer districtId
    ) {
        ApiResponse response = regionService.deleteDistrict(user, districtId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/region/{regionId}")
    public ResponseEntity<ApiResponse> deleteRegion(
            @AuthenticationPrincipal User user,
            @PathVariable Integer regionId
    ) {
        ApiResponse response = regionService.deleteRegion(user, regionId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/district//edit/{districtId}")
    public ResponseEntity<ApiResponse> editDistrict(
            @AuthenticationPrincipal User user,
            @PathVariable Integer districtId,
            @RequestParam String districtName
    ) {
        ApiResponse response = regionService.editDistrict(user, districtId, districtName);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/region/edit/{regionId}")
    public ResponseEntity<ApiResponse> editRegion(
            @AuthenticationPrincipal User user,
            @PathVariable Integer regionId,
            @RequestParam String regionName
    ) {
        ApiResponse response = regionService.editRegion(user, regionId, regionName);
        return new ResponseEntity<>(response, response.getStatus());
    }
    @GetMapping("/regions")
    public ResponseEntity<ApiResponse> getRegions(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse response = regionService.getRegion(user, page, size);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/districts")
    public ResponseEntity<ApiResponse> getDistricts(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse response = regionService.getDistrict(user, page, size);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
