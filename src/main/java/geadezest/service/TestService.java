package geadezest.service;


import geadezest.entity.*;
import geadezest.entity.enums.Question_type;
import geadezest.entity.enums.Test_difficulty;
import geadezest.payload.ApiResponse;
import geadezest.payload.OptionDTO;
import geadezest.payload.TestDTO;
import geadezest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;





@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;


    public ApiResponse createsTest( TestDTO dto, Question_type type, Test_difficulty difficulty) {
//        if (user.getRole().equals(Role.ROLE_USER)){
//            return new ApiResponse("kirish huquqi yuq",HttpStatus.BAD_REQUEST,false,null);
//        }
//        Optional<Category> byNameIgnoreCase = categoryRepository.findByNameIgnoreCase(dto.getCategory());
//        if (byNameIgnoreCase.isPresent()) {
//            return new ApiResponse("Category not found",HttpStatus.NOT_FOUND,false,null);
//        }
//        Category category = byNameIgnoreCase.get();
        Test test = new Test();
        test.setQuestion(dto.getQuestion());
        test.setCategory(null);
        test.setQuestion_type(type);
        test.setDifficulty(difficulty);



        switch (type) {
            case ONE_TRUE_ANSWER -> {
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


            case MORE_TRUE_ANSWER -> {
                test.setOptions(dto.getOptions().stream()
                        .map(opt -> new Option(opt.getAnswer(), opt.isCorrect(), test))
                        .toList());
            }

            case CALCULATED_ANSWER -> {
                if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
                    return new ApiResponse("CALCULATED_ANSWER uchun options yubormang",
                            HttpStatus.BAD_REQUEST, false, null);
                }

            }

            case ALL_TRUE_ANSWER -> {
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
//        try {
//            Photo photo = new Photo();
//            photo.setName(file.getOriginalFilename());
//            photo.setType(file.getContentType());
//            photo.setData(ImageUtils.compressImage(file.getBytes()));
//            photoRepository.save(photo);
//            test.setPhotoId(photo.getId());
//        }catch (Exception e){
//            return new ApiResponse("Rasm yuklashda xatolik",HttpStatus.BAD_REQUEST,false,null);
//        }

        testRepository.save(test);
        return new ApiResponse("Test created", HttpStatus.OK, true, null);
    }

//    public ApiResponse createTest(User user,MultipartFile file, TestDTO test, Question_type question_type, Test_difficulty test_difficulty) {
//        if (user.getRole().equals(Role.ROLE_USER)){
//            return new ApiResponse("kirish huquqi yuq",HttpStatus.BAD_REQUEST,false,null);
//        }
//        if (!testRepository.existsByNameIgnoreCase(test.getName())){
//            return new ApiResponse(
//                    "bu savol oldindan mavjud yoki nomini uzgartirib saqlang",
//                    HttpStatus.BAD_REQUEST,false,null);
//        }
//        Test testEntity = new Test();
//        testEntity.setName(test.getName());
//        testEntity.setQuestion(test.getQuestion());
//        testEntity.setCategory(test.getCategory());
//        testEntity.setQuestion_type(question_type);
//        testEntity.setDifficulty(test_difficulty);
//
//        try {
//            Photo photo = new Photo();
//            photo.setName(file.getOriginalFilename());
//            photo.setType(file.getContentType());
//            photo.setData(ImageUtils.compressImage(file.getBytes()));
//            photoRepository.save(photo);
//            testEntity.setPhotoId(photo.getId());
//        }catch (Exception e){
//            return new ApiResponse("Rasm yuklashda xatolik",HttpStatus.BAD_REQUEST,false,null);
//        }
//
//        testRepository.save(testEntity);
//        return new ApiResponse("Created test successfully",HttpStatus.OK,false,null);
//    }
//
//
//public ApiResponse addOptions(Integer testId, List<String> optionTexts) {
//    Test test = testRepository.findById(testId).orElseThrow();
//    if (test.getQuestion_type() == Question_type.CALCULATED_ANSWER) {
//        return new ApiResponse("CALCULATED_ANSWER uchun option qo'shish mumkin emas", HttpStatus.BAD_REQUEST, false, null);
//    }
//    List<Option> savedOpts = new ArrayList<>();
//    for (String t : optionTexts) {
//        Option o = new Option(); o.setText(t.trim());
//        o.setQuestion(test);
//        savedOpts.add(optionRepository.save(o));
//    }
//    return new ApiResponse("Options added", HttpStatus.OK, true, savedOpts);
//}
//
//    public ApiResponse addAnswers(Integer testId, List<Integer> optionIds, List<String> calculatedAnswers) {
//        Test test = testRepository.findById(testId).orElseThrow();
//        switch (test.getQuestion_type()) {
//            case ONE_TRUE_ANSWER -> {
//                if (optionIds == null || optionIds.size() != 1)
//                    throw new RuntimeException("ONE_TRUE_ANSWER uchun 1 ta optionId kerak");
//                Option opt = optionRepository.findById(optionIds.get(0)).orElseThrow();
//                opt.setCorrect(true);
//                optionRepository.save(opt);
//            }
//            case MORE_TRUE_ANSWER -> {
//                if (optionIds == null || optionIds.isEmpty()) throw new RuntimeException("至少一个 correct option");
//                for (Integer id : optionIds) {
//                    Option o = optionRepository.findById(id).orElseThrow();
//                    o.setCorrect(true);
//                    optionRepository.save(o);
//                }
//            }
//            case ALL_TRUE_ANSWER -> {
//                List<Option> opts = optionRepository.findAllByQuestion_Id(testId);
//                for (Option o : opts) { o.setCorrect(true); }
//                optionRepository.saveAll(opts);
//            }
//            case CALCULATED_ANSWER -> {
//                for (String ans : calculatedAnswers) {
//                    Answer a = new Answer();
//                    a.setTextAnswer(ans);
//                    a.setTest(test);
//                    answerRepository.save(a);
//                }
//            }
//        }
//        return new ApiResponse("Answers added", HttpStatus.OK, true, null);
//    }

}
