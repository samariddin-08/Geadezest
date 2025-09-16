package geadezest.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import geadezest.entity.User;
import geadezest.entity.enums.Question_type;
import geadezest.entity.enums.Test_difficulty;
import geadezest.payload.ApiResponse;
import geadezest.payload.TestDTO;
import geadezest.payload.UserDTO;
import geadezest.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> create(
            @RequestParam Question_type type,
            @RequestParam Test_difficulty difficulty,
            @RequestBody TestDTO dtoJson
    )  {

        ApiResponse test = testService.createsTest(dtoJson, type, difficulty);
        return ResponseEntity.status(test.getStatus()).body(test);
    }

}
