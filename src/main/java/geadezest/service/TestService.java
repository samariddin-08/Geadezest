package geadezest.service;
import geadezest.entity.*;
import geadezest.entity.enums.Question_type;
import geadezest.entity.enums.Role;
import geadezest.entity.enums.Test_difficulty;
import geadezest.payload.ApiResponse;
import geadezest.payload.OptionDTO;
import geadezest.payload.TestDTO;
import geadezest.payload.TestRes;
import geadezest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

 import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final PhotoRepository photoRepository;
    private final CategoryRepository categoryRepository;


    public ApiResponse setPhoto(MultipartFile file,Integer testId) {
        Optional<Test> byId = testRepository.findById(testId);
        if(byId.isPresent()) {
            Test test = byId.get();
            try {
                Photo photo = new Photo();
                photo.setName(file.getOriginalFilename());
                photo.setType(file.getContentType());
                photo.setData(ImageUtils.compressImage(file.getBytes()));
                photoRepository.save(photo);
                test.setPhotoId(photo.getId());
                testRepository.save(test);
            }catch(Exception e) {
                return new ApiResponse("Rasm yuklashda xatolik",HttpStatus.BAD_REQUEST,false,null);
            }
        }
        return new ApiResponse("Test topilmadi",HttpStatus.NOT_FOUND,false,null);
    }

    public ApiResponse createsTest( User user,TestDTO dto, Question_type type, Test_difficulty difficulty) {
        if (user.getRole().equals(Role.ROLE_USER)){
            return new ApiResponse("kirish huquqi yuq",HttpStatus.BAD_REQUEST,false,null);
        }
        Optional<Category> byId = categoryRepository.findById(dto.getCategoryId());
        if (!byId.isPresent()) {
            return new ApiResponse("Category not found",HttpStatus.NOT_FOUND,false,null);
        }
        Category category = byId.get();
        Test test = new Test();
        test.setQuestion(dto.getQuestion());
        test.setCategory(category);
        test.setQuestion_type(type);
        test.setDifficulty(difficulty);

        switch (type) {
            case BIR_TUGRI_JAVOBLI -> {
                if (dto.getOptions().size() == 4) {
                    long correctCount = dto.getOptions().stream().filter(OptionDTO::isCorrect).count();
                    if (correctCount != 1) {
                        return new ApiResponse("ONE_TRUE_ANSWER uchun faqat 1 ta to‘g‘ri javob bo‘lishi kerak",
                                HttpStatus.BAD_REQUEST, false, null);
                    }

                    test.setOptions(dto.getOptions().stream()
                            .map(opt -> new Option(opt.getAnswer(), opt.isCorrect(), test))
                            .toList());

                } else {
                    return new ApiResponse("ONE_TRUE_ANSWER uchun faqat 4 ta variant bo‘lishi kerak!",
                            HttpStatus.BAD_REQUEST, false, null);
                }
            }


            case KUP_TUGRRI_JAVOBLI -> {
                test.setOptions(dto.getOptions().stream()
                        .map(opt -> new Option(opt.getAnswer(), opt.isCorrect(), test))
                        .toList());
            }

            case HISOBLANGAN_NATIJA -> {
                if (dto.getOptions() == null || dto.getOptions().isEmpty()) {
                    return new ApiResponse("hisoblangan natija uchun kamida 1 ta option yuboring",
                            HttpStatus.BAD_REQUEST, false, null);
                }

                if (dto.getOptions().size() != 1) {
                    return new ApiResponse("hisoblangan natija uchun faqat bitta option yuborish mumkin",
                            HttpStatus.BAD_REQUEST, false, null);
                }

                if (!dto.getOptions().get(0).isCorrect()) {
                    return new ApiResponse("hisoblanagn natija  uchun yuborilgan option 'true' bo‘lishi kerak",
                            HttpStatus.BAD_REQUEST, false, null);
                }

                test.setOptions(dto.getOptions().stream()
                        .map(opt -> new Option(opt.getAnswer(), opt.isCorrect(), test))
                        .toList());
            }


            case HAMMASI_TUGRI -> {
                boolean allTrue = dto.getOptions().stream().allMatch(OptionDTO::isCorrect);
                if (!allTrue) {
                    return new ApiResponse("ALL_TRUE_ANSWER uchun barcha optionlar correct=true bo‘lishi kerak",
                            HttpStatus.BAD_REQUEST, false, null);
                }
                test.setOptions(dto.getOptions().stream()
                        .map(opt -> new Option(opt.getAnswer(), true, test))
                        .toList());
            }

           default -> {

               return new ApiResponse("Unknown question type", HttpStatus.BAD_REQUEST, false, null);
           }
        }


        testRepository.save(test);
        return new ApiResponse("Test created", HttpStatus.OK, true, "id -> "+test.getId());
    }


    public ApiResponse getAllTests(String name,String categoryName,Question_type  questionType,int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Test> all = testRepository.getAll(name,categoryName,questionType,pageable);
        if(all.getTotalElements() == 0) {
            return new ApiResponse("No Tests found", HttpStatus.NOT_FOUND, false, null);
        }
        List<TestRes> testResList = new ArrayList<>();
        for (Test test : all.getContent()) {
            TestRes testRes = new TestRes();
            testRes.setId(test.getId());
            testRes.setPhotoId(test.getPhotoId());
            testRes.setQuestion(test.getQuestion());
            testRes.setCategoryName(test.getCategory().getName());
            testRes.setQuestion_type(test.getQuestion_type());
            testRes.setTest_difficulty(test.getDifficulty());
            testRes.setCreatedBy(test.getCreatedBy());
            testResList.add(testRes);
        }

        return new ApiResponse("Barcha testlar", HttpStatus.OK, true,  testResList);

    }

}
