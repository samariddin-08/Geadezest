package geadezest.controller;

import geadezest.entity.User;

import geadezest.payload.ApiResponse;
import geadezest.service.WorkingTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/work/test")
public class TestWork {
    private final WorkingTest workingTest;


    @GetMapping("/view")
    public ResponseEntity<ApiResponse> testWork(){
        ApiResponse categories = workingTest.categories();
        return ResponseEntity.status(categories.getStatus()).body(categories);
    }

    @GetMapping("/start")
    public ResponseEntity<ApiResponse>getAllTests(
            @AuthenticationPrincipal User user,
            @RequestParam Integer categoryId) {
        ApiResponse test = workingTest.test(user,categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(test);
    }

    @PostMapping("/set/answer")
    public ResponseEntity<ApiResponse>setTestAnswer(
            @AuthenticationPrincipal User user,
            @RequestParam  Integer testId,
            @RequestParam (required = false)List<Integer> optionsId,
            @RequestParam (required = false)String answer
            ) {
        ApiResponse apiResponse = workingTest.submitAnswer(user,testId, optionsId, answer);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/result")
    public ResponseEntity<ApiResponse> result(@AuthenticationPrincipal User user,
    @RequestParam Integer categoryId
    ) {
        ApiResponse apiResponse = workingTest.finishTest(categoryId, user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
