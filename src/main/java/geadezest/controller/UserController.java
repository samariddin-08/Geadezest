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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> edit_profile(@AuthenticationPrincipal User user,
                                                    @RequestPart("userDTO") String userDTOJson,
                                                    @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        UserDTO userDTO = mapper.readValue(userDTOJson, UserDTO.class);


        ApiResponse apiResponse = userService.editProfile(file, user, userDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @GetMapping("/profile")
    @Operation(summary = "User malumotlarin kurish uchun api")
    public ResponseEntity<ApiResponse> profile(@AuthenticationPrincipal User user) {
        ApiResponse profile = userService.getProfile(user);
        return ResponseEntity.status(profile.getStatus()).body(profile);
    }
}
