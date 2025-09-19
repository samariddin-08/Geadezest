package geadezest.service;

import geadezest.entity.Category;
import geadezest.entity.User;
import geadezest.entity.enums.Role;
import geadezest.entity.enums.Status;
import geadezest.payload.ApiResponse;
import geadezest.payload.CategoryReq;
import geadezest.payload.CategoryRes;
import geadezest.repository.CategoryRepository;
import geadezest.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TestResultRepository testResultRepository;

    public ApiResponse createCategory(User user, CategoryReq categoryRes) {
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            Category category = new Category();
            category.setName(categoryRes.getName());
            category.setDescription(categoryRes.getDescription());
            category.setCreatedBy(user.getUsername());
            category.setEnabled(true);
            category.setStatus(Status.NOT_ACTIVE);
            category.setDurationTime(categoryRes.getDurationTime());
            category.setNextTestTimeDuration(categoryRes.getNextTestWorkTime());
            categoryRepository.save(category);
            return new ApiResponse("Categroy saved", HttpStatus.OK, true, null);
        }
        return new ApiResponse("Kirish ruxsat etilmagan", HttpStatus.NOT_ACCEPTABLE, false, null);
    }

    public ApiResponse allCategories(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            Page<Category> categories = categoryRepository.findAll(pageable);
            List<CategoryRes> categoriesRes = new ArrayList<>();
            for (Category category : categories) {
                CategoryRes categoryRes = CategoryRes.builder()
                        .id(category.getId())
                        .photoId(category.getPhotoId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .questionCount(testResultRepository.countByCategoryId(category.getId()))
                        .moreQuestionCount(category.getMoreQuestionsCount())
                        .durationTime(category.getDurationTime())
                        .nextTestWorkTime(category.getNextTestTimeDuration())
                        .createdBy(category.getCreatedBy())
                        .status(category.getStatus())
                        .deletedBy(category.getDeletedBy())
                        .build();
                categoriesRes.add(categoryRes);
            }
            return new ApiResponse("categoryies", HttpStatus.OK, true, categoriesRes);
        }
        return new ApiResponse("Kirish ruxsat etilmagan", HttpStatus.NOT_ACCEPTABLE, false, null);
    }
    public ApiResponse activeCategories(User user,Integer categoryId) {
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            Optional<Category> byId = categoryRepository.findById(categoryId);
            if (byId.isPresent()) {
                Category category = byId.get();
                category.setStatus(Status.ACTIVE);
                categoryRepository.save(category);
                return new ApiResponse("Category activated",HttpStatus.OK,true,null);
            }
            return new ApiResponse("Category not found",HttpStatus.NOT_FOUND,false,null);
        }
        return new ApiResponse("Kirish ruxsat etilmagan", HttpStatus.NOT_ACCEPTABLE, false, null);
    }
    public ApiResponse deleteCategory(User user,Integer categoryId) {
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            Optional<Category> byId = categoryRepository.findById(categoryId);
            if (byId.isPresent()) {
                Category category = byId.get();
                category.setStatus(Status.DELETED);
                categoryRepository.save(category);
                return new ApiResponse("Category activated",HttpStatus.OK,true,null);
            }
            return new ApiResponse("Category not found",HttpStatus.NOT_FOUND,false,null);
        }
        return new ApiResponse("Kirish ruxsat etilmagan", HttpStatus.NOT_ACCEPTABLE, false, null);
    }

    public ApiResponse editCategory(User user, Integer categoryId,CategoryReq categoryRes) {
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            Optional<Category> byId = categoryRepository.findById(categoryId);
            if (byId.isPresent()) {
                Category category = byId.get();
                category.setName(categoryRes.getName());
                category.setDescription(categoryRes.getDescription());
                category.setCreatedBy(user.getUsername());
                category.setEnabled(true);
                category.setDurationTime(categoryRes.getDurationTime());
                category.setNextTestTimeDuration(categoryRes.getNextTestWorkTime());
                categoryRepository.save(category);
                return new ApiResponse("Category updated", HttpStatus.OK, true, null);
            }
            return new ApiResponse("Category not found", HttpStatus.NOT_FOUND, false, null);
        }
        return new ApiResponse("Kirish ruxsat etilmagan", HttpStatus.NOT_ACCEPTABLE, false, null);
    }

}

