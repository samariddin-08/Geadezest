package geadezest.controller;
import geadezest.entity.User;
import geadezest.entity.enums.Question_type;
import geadezest.entity.enums.Test_difficulty;
import geadezest.payload.ApiResponse;
import geadezest.payload.TestDTO;
import geadezest.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> create(
            @AuthenticationPrincipal User user,
            @RequestParam Question_type type,
            @RequestParam Test_difficulty difficulty,
            @RequestBody TestDTO dtoJson
    )  {
        ApiResponse test = testService.createsTest(user,dtoJson, type, difficulty);
        return ResponseEntity.status(test.getStatus()).body(test);
    }

    @PostMapping(value = "/set/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> setPhoto(
            @RequestParam MultipartFile file,
            @RequestParam Integer test_id
    ){
        ApiResponse apiResponse = testService.setPhoto(file, test_id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTests(
            @RequestParam (required = false) String question,
            @RequestParam (required = false) String categoryName,
            @RequestParam (required = false) Question_type type,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page
    ){
        ApiResponse allTests = testService.getAllTests(question, categoryName, type, size, page);
        return ResponseEntity.status(allTests.getStatus()).body(allTests);
    }
}
