package geadezest.controller;

import geadezest.entity.User;
import geadezest.entity.enums.UserResults;
import geadezest.payload.ApiResponse;
import geadezest.service.ControlPanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/control")
public class ControlPanelController {
    private final ControlPanelService controlPanelService;


    @GetMapping("/panel")
    public ResponseEntity<ApiResponse> panel(@AuthenticationPrincipal User user) {
        ApiResponse apiResponse = controlPanelService.controlPanel(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping("/users/results")
    public ResponseEntity<ApiResponse> results(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) UserResults results,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page
    ) {
        ApiResponse userResults = controlPanelService.getUserResults(fullName, categoryName, results, page, size);
        return ResponseEntity.status(userResults.getStatus()).body(userResults);
    }

    @PostMapping("/set/result")
    public ResponseEntity<ApiResponse> bekorQilish(@AuthenticationPrincipal User user,
                                                   @RequestParam Integer resultId,
                                                   @RequestParam UserResults userResults,
                                                   @RequestParam double amaliyScore) {
        ApiResponse apiResponse = controlPanelService.setResult(user, resultId,userResults,amaliyScore);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    @GetMapping("/view/result")
    public ResponseEntity<ApiResponse> viewResult(
            @AuthenticationPrincipal User user,
            @RequestParam Integer resultId
    ){
        ApiResponse apiResponse = controlPanelService.viewResult(user, resultId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }




}
