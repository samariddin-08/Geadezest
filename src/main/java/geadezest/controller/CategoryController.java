package geadezest.controller;

import geadezest.entity.User;
import geadezest.payload.ApiResponse;
import geadezest.payload.CategoryReq;
import geadezest.security.CurrentUser;
import geadezest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@CurrentUser User user,
                                                      @RequestBody CategoryReq categoryReq) {
        ApiResponse response = categoryService.createCategory(user, categoryReq);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> allCategories(@CurrentUser User user,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        ApiResponse response = categoryService.allCategories(user, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<ApiResponse> activateCategory(@CurrentUser User user,
                                                        @PathVariable Integer id) {
        ApiResponse response = categoryService.activeCategories(user, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@CurrentUser User user,
                                                      @PathVariable Integer id) {
        ApiResponse response = categoryService.deleteCategory(user, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse> editCategory(@CurrentUser User user,
                                                    @PathVariable Integer id,
                                                    @RequestBody CategoryReq categoryReq) {
        ApiResponse response = categoryService.editCategory(user, id, categoryReq);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}