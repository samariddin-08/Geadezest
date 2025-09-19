package geadezest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import geadezest.entity.User;
import geadezest.payload.ApiResponse;
import geadezest.payload.UserDTO;
import geadezest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @PostMapping(value = "/set/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> setPhoto( @AuthenticationPrincipal User user,
                                                 @RequestParam MultipartFile file) {
        ApiResponse apiResponse = userService.setPhoto(user, file);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping(value = "/edit")
    public ResponseEntity<ApiResponse> edit_profile(
            @AuthenticationPrincipal User user,
            @RequestBody UserDTO userDTO) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ApiResponse apiResponse = userService.editProfile( user, userDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/profile")
    @Operation(summary = "User malumotlarin kurish uchun api")
    public ResponseEntity<ApiResponse> profile(@AuthenticationPrincipal User user) {
        ApiResponse profile = userService.getProfile(user);
        return ResponseEntity.status(profile.getStatus()).body(profile);
    }


    @GetMapping("/usersGet")
    @Operation(summary = "qidirish ism buyicha")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> usersGet(
            @RequestParam (required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse response = userService.getAllUsers(name,page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Integer id) {
        ApiResponse response = userService.getUserById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUsers(
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse response = userService.get(district, region, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/view/results")
    public ResponseEntity<ApiResponse> viewResults(@AuthenticationPrincipal User user) {
        ApiResponse apiResponse = userService.viewResults(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}